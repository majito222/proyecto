import { Component, inject } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { RouterLink } from '@angular/router';
import { catchError, map, of } from 'rxjs';
import { Card } from 'primeng/card';
import { Tag } from 'primeng/tag';
import { SolicitudesService } from '../../servicios/solicitudes';
import { EstadoSolicitud, Prioridad } from '../../modelos/solicitudes';

type TagSeverity = 'success' | 'warn' | 'danger';

@Component({
  selector: 'app-lista-solicitudes',
  imports: [Card, Tag, RouterLink],
  templateUrl: './lista-solicitudes.html',
  styleUrl: './lista-solicitudes.css'
})
export class ListaSolicitudes {
  private readonly solicitudesService = inject(SolicitudesService);

  readonly solicitudes = toSignal(
    this.solicitudesService.listar().pipe(
      map((pagina) => pagina.contenido),
      catchError(() => of([]))
    ),
    { initialValue: [] }
  );

  tagSeveridad(estado: EstadoSolicitud): TagSeverity {
    const severidades: Record<EstadoSolicitud, TagSeverity> = {
      REGISTRADA: 'warn',
      CLASIFICADA: 'warn',
      EN_ATENCION: 'warn',
      ATENDIDA: 'success',
      CERRADA: 'success',
      CANCELADA: 'danger'
    };

    return severidades[estado];
  }

  tagPrioridad(prioridad: Prioridad | null): TagSeverity {
    if (!prioridad) {
      return 'warn';
    }

    const severidades: Record<Prioridad, TagSeverity> = {
      BAJA: 'success',
      MEDIA: 'warn',
      ALTA: 'danger',
      CRITICA: 'danger'
    };

    return severidades[prioridad];
  }
}
