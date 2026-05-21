import { Injectable, computed, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, tap, throwError } from 'rxjs';
import { LoginRequest, TokenResponse, UserRole } from '../../shared/models/auth';

const TOKEN_KEY = 'token';
const ROLES_KEY = 'roles';
const LEGACY_TOKEN_KEY = 'auth_token';
const LEGACY_ROLES_KEY = 'auth_roles';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8081/api/auth/login';

  readonly isAuthenticated = signal(Boolean(this.getToken()));
  readonly roles = signal<UserRole[]>(this.readStoredRoles());
  readonly primaryRole = computed(() => this.roles()[0] ?? null);
  readonly userEmail = computed(() => this.extractSubject(this.getToken()));
  readonly sessionLabel = computed(() => {
    const role = this.primaryRole();
    return role ? this.formatRole(role) : 'Sin sesion';
  });

  login(credentials: LoginRequest) {
    return this.http.post<TokenResponse>(this.apiUrl, credentials).pipe(
      tap((response) => {
        localStorage.setItem(TOKEN_KEY, response.token);
        localStorage.removeItem(LEGACY_TOKEN_KEY);
        localStorage.removeItem(LEGACY_ROLES_KEY);

        const roles = this.extractRolesFromToken(response.token);
        localStorage.setItem(ROLES_KEY, JSON.stringify(roles));
        this.roles.set(roles);
        this.isAuthenticated.set(true);
      }),
      catchError((error) => {
        const message = error?.error?.message || 'Credenciales invalidas';
        return throwError(() => new Error(message));
      })
    );
  }

  getToken(): string | null {
    return localStorage.getItem(TOKEN_KEY) ?? localStorage.getItem(LEGACY_TOKEN_KEY);
  }

  getRoles(): UserRole[] {
    return this.roles();
  }

  private readStoredRoles(): UserRole[] {
    const rawRoles = localStorage.getItem(ROLES_KEY) ?? localStorage.getItem(LEGACY_ROLES_KEY);

    if (!rawRoles) return [];

    try {
      const parsedRoles: unknown = JSON.parse(rawRoles);
      return Array.isArray(parsedRoles)
        ? parsedRoles.map((role) => this.normalizeRole(role)).filter((role): role is UserRole => Boolean(role))
        : [];
    } catch {
      localStorage.removeItem(ROLES_KEY);
      localStorage.removeItem(LEGACY_ROLES_KEY);
      return [];
    }
  }

  private extractRolesFromToken(token: string): UserRole[] {
    try {
      const [, payload] = token.split('.');
      if (!payload) return [];
      const normalizedPayload = payload.replace(/-/g, '+').replace(/_/g, '/');
      const decoded = JSON.parse(atob(normalizedPayload)) as { roles?: unknown };
      if (!Array.isArray(decoded.roles)) return [];
      return decoded.roles.map((role) => this.normalizeRole(role)).filter((role): role is UserRole => Boolean(role));
    } catch {
      return [];
    }
  }

  hasAnyRole(expectedRoles: readonly string[]): boolean {
    const currentRoles = this.roles();
    return expectedRoles.some((role) => currentRoles.includes(role.replace('ROLE_', '') as UserRole));
  }

  dashboardUrl(): string {
    const role = this.primaryRole();
    const routes: Record<UserRole, string> = {
      ESTUDIANTE: '/dashboard/estudiante',
      FUNCIONARIO: '/dashboard/funcionario',
      ADMINISTRADOR: '/dashboard/admin'
    };
    return role ? routes[role] : '/lista-solicitudes';
  }

  private formatRole(role: string): string {
    const normalizedRole = role.replace('ROLE_', '');
    const labels: Record<string, string> = {
      ADMINISTRADOR: 'Administrador',
      FUNCIONARIO: 'Funcionario',
      ESTUDIANTE: 'Estudiante'
    };
    return labels[normalizedRole] ?? normalizedRole;
  }

  private extractSubject(token: string | null): string | null {
    if (!token) return null;

    try {
      const [, payload] = token.split('.');
      if (!payload) return null;
      const normalizedPayload = payload.replace(/-/g, '+').replace(/_/g, '/');
      const decodedPayload = JSON.parse(atob(normalizedPayload)) as { sub?: unknown };
      return typeof decodedPayload.sub === 'string' ? decodedPayload.sub : null;
    } catch {
      return null;
    }
  }

  logout(): void {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(ROLES_KEY);
    localStorage.removeItem(LEGACY_TOKEN_KEY);
    localStorage.removeItem(LEGACY_ROLES_KEY);
    this.roles.set([]);
    this.isAuthenticated.set(false);
  }

  private normalizeRole(role: unknown): UserRole | null {
    if (typeof role !== 'string') return null;
    const normalized = role.replace('ROLE_', '') as UserRole;
    return ['ESTUDIANTE', 'FUNCIONARIO', 'ADMINISTRADOR'].includes(normalized) ? normalized : null;
  }
}
