import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { LoginRequest, TokenResponse } from '../modelos/auth';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);
  private url = 'http://localhost:8081/api/auth';

  login(credenciales: LoginRequest) {
    return this.http.post<TokenResponse>(`${this.url}/login`, credenciales);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getRoles(): string[] {
    const roles = localStorage.getItem('roles');
    return roles ? JSON.parse(roles) : [];
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('roles');
  }
}