import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { SolicitudDetalle, SolicitudResumen, CrearSolicitudRequest, PaginaResponse } from '../../shared/models/solicitudes';

@Injectable({ providedIn: 'root' })
export class SolicitudesService {
  private readonly http = inject(HttpClient);
  private readonly url = 'http://localhost:8080/api/v1/solicitudes';

  listar() {
    return this.http.get<PaginaResponse<SolicitudResumen>>(this.url);
  }

  crear(solicitud: CrearSolicitudRequest) {
    return this.http.post<SolicitudDetalle>(this.url, solicitud);
  }
}
