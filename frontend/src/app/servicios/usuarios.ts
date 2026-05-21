import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';

export interface CrearUsuarioRequest {
  nombre: string;
  email: string;
  password: string;
  tipo: 'ESTUDIANTE' | 'FUNCIONARIO' | 'ADMINISTRADOR';
}

export interface UsuarioDetalle {
  id: string;
  nombre: string;
  email: string;
  tipo: string;
  estado: string;
}

@Injectable({ providedIn: 'root' })
export class UsuariosService {
  private readonly http = inject(HttpClient);
  private readonly url = 'http://localhost:8081/api/v1/usuarios';

  crear(usuario: CrearUsuarioRequest) {
    return this.http.post<UsuarioDetalle>(this.url, usuario);
  }
}
