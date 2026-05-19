import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs';
import { LoginRequest, TokenResponse } from '../modelos/auth';

const TOKEN_KEY = 'auth_token';
const ROLES_KEY = 'auth_roles';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/api/auth/login';

  readonly isAuthenticated = signal(Boolean(this.getToken()));
  readonly roles = signal<string[]>(this.readStoredRoles());

  login(credentials: LoginRequest) {
    return this.http.post<TokenResponse>(this.apiUrl, credentials).pipe(
      tap((response) => {
        const roles = response.roles ?? [];
        localStorage.setItem(TOKEN_KEY, response.token);
        localStorage.setItem(ROLES_KEY, JSON.stringify(roles));
        this.roles.set(roles);
        this.isAuthenticated.set(true);
      })
    );
  }

  getToken(): string | null {
    return localStorage.getItem(TOKEN_KEY);
  }

  getRoles(): string[] {
    return this.roles();
  }

  private readStoredRoles(): string[] {
    const rawRoles = localStorage.getItem(ROLES_KEY);


    if (!rawRoles) {
      return [];
    }

    try {
      const parsedRoles: unknown = JSON.parse(rawRoles);
      return Array.isArray(parsedRoles) ? parsedRoles.filter((role): role is string => typeof role === 'string') : [];
    } catch {
      localStorage.removeItem(ROLES_KEY);
      return [];
    }
  }

  hasAnyRole(expectedRoles: readonly string[]): boolean {
    const currentRoles = this.roles();
    return expectedRoles.some((role) => currentRoles.includes(role));
  }

  logout(): void {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(ROLES_KEY);
    this.roles.set([]);
    this.isAuthenticated.set(false);
  }
}
