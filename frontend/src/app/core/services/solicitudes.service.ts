import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
<<<<<<< HEAD:frontend/src/app/core/services/solicitudes.service.ts
<<<<<<< HEAD:frontend/src/app/core/services/solicitudes.service.ts
import { SolicitudDetalle, SolicitudResumen, CrearSolicitudRequest, PaginaResponse } from '../../shared/models/solicitudes';
=======
import { SolicitudResumen, SolicitudDetalle, CrearSolicitudRequest, PaginaResponse } from '../modelos/solicitudes';
=======
import { SolicitudResumen, SolicitudDetalle, CrearSolicitudRequest, PaginaResponse, TipoSolicitud, Prioridad } from '../modelos/solicitudes';
>>>>>>> 45d170c (feat: configuracion adicional):frontend/src/app/servicios/solicitudes.ts

export type { SolicitudResumen, SolicitudDetalle, CrearSolicitudRequest, PaginaResponse };
>>>>>>> 1be9452 (feat: registro de usuarios, creacion de solicitudes y dashboard admin):frontend/src/app/servicios/solicitudes.ts

@Injectable({ providedIn: 'root' })
export class SolicitudesService {
  private readonly http = inject(HttpClient);
  private readonly url = 'http://localhost:8081/api/v1/solicitudes';

  listar() {
    return this.http.get<PaginaResponse<SolicitudResumen>>(this.url);
  }

  crear(solicitud: CrearSolicitudRequest) {
    return this.http.post<SolicitudDetalle>(this.url, solicitud);
  }

  clasificar(codigo: string, funcionarioId: string, tipo: TipoSolicitud) {
    return this.http.post<SolicitudDetalle>(`${this.url}/${codigo}/clasificacion`, {
      funcionarioId,
      tipo
    });
  }

  priorizar(codigo: string, funcionarioId: string, nivel: Prioridad, justificacion: string) {
    return this.http.post<SolicitudDetalle>(`${this.url}/${codigo}/prioridad`, {
      funcionarioId,
      nivel,
      justificacion
    });
  }

  iniciarAtencion(codigo: string, funcionarioId: string) {
    return this.http.post<SolicitudDetalle>(`${this.url}/${codigo}/atencion/inicio`, {
      funcionarioId
    });
  }

  finalizarAtencion(codigo: string, funcionarioId: string) {
    return this.http.post<SolicitudDetalle>(`${this.url}/${codigo}/atencion/finalizacion`, {
      funcionarioId
    });
  }
}