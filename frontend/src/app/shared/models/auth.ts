export type UserRole = 'ESTUDIANTE' | 'FUNCIONARIO' | 'ADMINISTRADOR';

export interface LoginRequest {
  email: string;
  password: string;
}

export interface TokenResponse {
  token: string;
  type?: string;
  tipo?: string;
  expireAt?: string;
  expiresIn?: number;
  roles?: UserRole[];
}
