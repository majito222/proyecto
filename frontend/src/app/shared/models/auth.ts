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
<<<<<<< HEAD:frontend/src/app/shared/models/auth.ts
  roles?: UserRole[];
}
=======
  roles?: string[];
}
>>>>>>> 1be9452 (feat: registro de usuarios, creacion de solicitudes y dashboard admin):frontend/src/app/modelos/auth.ts
