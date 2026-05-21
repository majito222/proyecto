import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
<<<<<<< HEAD:frontend/src/app/core/services/solicitudes.service.ts
import { SolicitudDetalle, SolicitudResumen, CrearSolicitudRequest, PaginaResponse } from '../../shared/models/solicitudes';
=======
import { SolicitudResumen, SolicitudDetalle, CrearSolicitudRequest, PaginaResponse } from '../modelos/solicitudes';

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
}