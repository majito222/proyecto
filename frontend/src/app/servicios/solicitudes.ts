import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { SolicitudResumen, CrearSolicitudRequest } from '../modelos/solicitudes';

@Injectable({ providedIn: 'root' })
export class SolicitudesService {
  private http = inject(HttpClient);
  private url = 'http://localhost:8080/api/v1/solicitudes';

  listar() {
    return this.http.get<SolicitudResumen[]>(this.url);
  }

  crear(solicitud: CrearSolicitudRequest) {
    return this.http.post<SolicitudResumen>(this.url, solicitud);
  }
}
