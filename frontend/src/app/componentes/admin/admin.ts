import { Component, computed, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { NgClass } from '@angular/common';
import { toSignal } from '@angular/core/rxjs-interop';
import { map } from 'rxjs';
import { SolicitudesService } from '../../servicios/solicitudes';
import { SolicitudResumen } from '../../modelos/solicitudes';

@Component({
  selector: 'app-admin',
  imports: [RouterLink, NgClass],
  templateUrl: './admin.html',
  styleUrl: './admin.css'
})
export class Admin {
  private readonly solicitudesService = inject(SolicitudesService);

  solicitudes = toSignal(
    this.solicitudesService.listar().pipe(
      map(res => res.contenido ?? [])
    ),
    { initialValue: [] as SolicitudResumen[] }
  );

  totalSolicitudes = computed(() => this.solicitudes().length);
  registradas = computed(() => this.solicitudes().filter(s => s.estado === 'REGISTRADA').length);
  enAtencion = computed(() => this.solicitudes().filter(s => s.estado === 'EN_ATENCION').length);
  atendidas = computed(() => this.solicitudes().filter(s => s.estado === 'ATENDIDA').length);
  cerradas = computed(() => this.solicitudes().filter(s => s.estado === 'CERRADA').length);
  altaPrioridad = computed(() => this.solicitudes().filter(s => s.prioridad === 'ALTA').length);

  pct(value: number, total: number): number {
    if (total === 0) return 0;
    return Math.round((value / total) * 100);
  }

  badgeEstado(estado: string): string {
    const mapa: Record<string, string> = {
      REGISTRADA: 'badge-registrada',
      CLASIFICADA: 'badge-clasificada',
      EN_ATENCION: 'badge-en-atencion',
      ATENDIDA: 'badge-atendida',
      CERRADA: 'badge-cerrada',
      CANCELADA: 'badge-cancelada',
    };
    return mapa[estado] ?? 'badge-registrada';
  }
}