import { Component, inject } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { RouterLink } from '@angular/router';
import { SlicePipe } from '@angular/common';
import { SolicitudesService } from '../../servicios/solicitudes';
import { SolicitudResumen } from '../../modelos/solicitudes';

@Component({
  selector: 'app-lista-solicitudes',
  imports: [RouterLink, SlicePipe],
  templateUrl: './lista-solicitudes.html',
  styleUrl: './lista-solicitudes.css'
})
export class ListaSolicitudes {
  private solicitudesService = inject(SolicitudesService);

  solicitudes = toSignal(
    this.solicitudesService.listar(),
    { initialValue: [] as SolicitudResumen[] }
  );

  badgeEstado(estado: string): string {
    const mapa: Record<string, string> = {
      REGISTRADA: 'bg-secondary',
      CLASIFICADA: 'bg-info text-dark',
      EN_ATENCION: 'bg-primary',
      ATENDIDA: 'bg-success',
      CERRADA: 'bg-dark',
      CANCELADA: 'bg-danger',
    };
    return mapa[estado] ?? 'bg-secondary';
  }

  badgePrioridad(prioridad: string): string {
    const mapa: Record<string, string> = {
      BAJA: 'bg-success',
      MEDIA: 'bg-warning text-dark',
      ALTA: 'bg-danger',
    };
    return mapa[prioridad] ?? 'bg-secondary';
  }
}