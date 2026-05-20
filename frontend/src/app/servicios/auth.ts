import { Injectable, computed, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, tap, throwError } from 'rxjs';
import { LoginRequest, TokenResponse } from '../modelos/auth';

const TOKEN_KEY = 'token';
const ROLES_KEY = 'roles';
const LEGACY_TOKEN_KEY = 'auth_token';
const LEGACY_ROLES_KEY = 'auth_roles';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/api/auth/login';

  readonly isAuthenticated = signal(Boolean(this.getToken()));
  readonly roles = signal<string[]>(this.readStoredRoles());
  readonly primaryRole = computed(() => this.roles()[0] ?? null);
  readonly userEmail = computed(() => this.extractSubject(this.getToken()));
  readonly sessionLabel = computed(() => {
    const role = this.primaryRole();
    return role ? this.formatRole(role) : 'Sin sesion';
  });

  login(credentials: LoginRequest) {
    return this.http.post<TokenResponse>(this.apiUrl, credentials).pipe(
      tap((response) => {
        const roles = response.roles ?? [];
        localStorage.setItem(TOKEN_KEY, response.token);
        localStorage.setItem(ROLES_KEY, JSON.stringify(roles));
        localStorage.removeItem(LEGACY_TOKEN_KEY);
        localStorage.removeItem(LEGACY_ROLES_KEY);
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

  getRoles(): string[] {
    return this.roles();
  }

  private readStoredRoles(): string[] {
    const rawRoles = localStorage.getItem(ROLES_KEY) ?? localStorage.getItem(LEGACY_ROLES_KEY);


    if (!rawRoles) {
      return [];
    }

    try {
      const parsedRoles: unknown = JSON.parse(rawRoles);
      return Array.isArray(parsedRoles) ? parsedRoles.filter((role): role is string => typeof role === 'string') : [];
    } catch {
      localStorage.removeItem(ROLES_KEY);
      localStorage.removeItem(LEGACY_ROLES_KEY);
      return [];
    }
  }

  hasAnyRole(expectedRoles: readonly string[]): boolean {
    const currentRoles = this.roles();
    return expectedRoles.some((role) => currentRoles.includes(role));
  }

  private formatRole(role: string): string {
    const labels: Record<string, string> = {
      ADMINISTRADOR: 'Administrador',
      FUNCIONARIO: 'Funcionario',
      ESTUDIANTE: 'Estudiante'
    };

    return labels[role] ?? role;
  }

  private extractSubject(token: string | null): string | null {
    if (!token) {
      return null;
    }

    try {
      const [, payload] = token.split('.');
      if (!payload) {
        return null;
      }

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
}
