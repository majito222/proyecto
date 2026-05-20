import { Component, computed, inject } from '@angular/core';
import { SlicePipe } from '@angular/common';
import { toSignal } from '@angular/core/rxjs-interop';
import { RouterLink } from '@angular/router';
import { catchError, map, of } from 'rxjs';
import { Card } from 'primeng/card';
import { Tag } from 'primeng/tag';
import { SolicitudesService } from '../../servicios/solicitudes';
import { EstadoSolicitud, Prioridad, SolicitudResumen, TipoSolicitud } from '../../modelos/solicitudes';
import { AuthService } from '../../servicios/auth';

type TagSeverity = 'success' | 'warn' | 'danger';

@Component({
  selector: 'app-lista-solicitudes',
  imports: [Card, Tag, RouterLink, SlicePipe],
  templateUrl: './lista-solicitudes.html',
  styleUrl: './lista-solicitudes.css'
})
export class ListaSolicitudes {
  private readonly solicitudesService = inject(SolicitudesService);
  readonly auth = inject(AuthService);
  private readonly tiposInfo: Record<TipoSolicitud, { label: string; icon: string; description: string }> = {
    REGISTRO_ASIGNATURA: {
      label: 'Registro de asignatura',
      icon: 'pi pi-book',
      description: 'Inscripcion, cruces o problemas con materias.'
    },
    HOMOLOGACION: {
      label: 'Homologacion',
      icon: 'pi pi-verified',
      description: 'Revision de materias cursadas para reconocimiento academico.'
    },
    CANCELACION: {
      label: 'Cancelacion',
      icon: 'pi pi-times-circle',
      description: 'Cancelacion de asignatura, semestre o proceso academico.'
    },
    SOLICITUD_CUPO: {
      label: 'Solicitud de cupo',
      icon: 'pi pi-users',
      description: 'Solicitud para obtener cupo en una asignatura.'
    },
    CONSULTA_ACADEMICA: {
      label: 'Consulta academica',
      icon: 'pi pi-question-circle',
      description: 'Dudas generales sobre procesos academicos.'
    }
  };

  readonly solicitudes = toSignal(
    this.solicitudesService.listar().pipe(
      map((pagina) => pagina.contenido),
      catchError(() => of([]))
    ),
    { initialValue: [] }
  );

  readonly gruposSolicitudes = computed(() => {
    const solicitudes = this.solicitudes();

    return this.gruposOrdenados
      .map((grupo) => ({
        ...grupo,
        solicitudes: solicitudes.filter((solicitud) => this.perteneceAlGrupo(solicitud, grupo.key))
      }))
      .filter((grupo) => grupo.solicitudes.length > 0);
  });

  readonly totalSolicitudes = computed(() => this.solicitudes().length);

  private readonly gruposOrdenados = [
    {
      key: 'HOMOLOGACION',
      tipo: 'HOMOLOGACION' as TipoSolicitud,
      label: 'Homologacion',
      icon: 'pi pi-verified',
      description: 'Revision de materias cursadas para reconocimiento academico.'
    },
    {
      key: 'SOLICITUD_CUPO',
      tipo: 'SOLICITUD_CUPO' as TipoSolicitud,
      label: 'Solicitud de cupo',
      icon: 'pi pi-users',
      description: 'Solicitud para obtener cupo en una asignatura.'
    },
    {
      key: 'REGISTRO_ASIGNATURA',
      tipo: 'REGISTRO_ASIGNATURA' as TipoSolicitud,
      label: 'Registro de asignatura',
      icon: 'pi pi-book',
      description: 'Inscripcion, cruces o problemas con materias.'
    },
    {
      key: 'CANCELACION_MATERIA',
      tipo: 'CANCELACION' as TipoSolicitud,
      label: 'Cancelacion de materia',
      icon: 'pi pi-times-circle',
      description: 'Solicitudes para cancelar una materia especifica.'
    },
    {
      key: 'CANCELACION_SEMESTRE',
      tipo: 'CANCELACION' as TipoSolicitud,
      label: 'Cancelacion de semestre',
      icon: 'pi pi-calendar-times',
      description: 'Solicitudes para cancelar el semestre completo.'
    },
    {
      key: 'CONSULTA_ACADEMICA',
      tipo: 'CONSULTA_ACADEMICA' as TipoSolicitud,
      label: 'Consulta academica',
      icon: 'pi pi-question-circle',
      description: 'Dudas generales sobre procesos academicos.'
    }
  ];

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

  tipoLabel(tipo: TipoSolicitud): string {
    return this.tiposInfo[tipo].label;
  }

  tipoIcon(tipo: TipoSolicitud): string {
    return this.tiposInfo[tipo].icon;
  }

  tipoDescripcion(tipo: TipoSolicitud): string {
    return this.tiposInfo[tipo].description;
  }

  subtipoCancelacion(solicitud: SolicitudResumen): string | null {
    if (solicitud.tipo !== 'CANCELACION') {
      return null;
    }

    const descripcion = solicitud.descripcion?.toUpperCase() ?? '';
    return descripcion.includes('CANCELACION DE: SEMESTRE') ? 'Semestre' : 'Materia';
  }

  private perteneceAlGrupo(solicitud: SolicitudResumen, groupKey: string): boolean {
    if (groupKey === 'CANCELACION_MATERIA') {
      return solicitud.tipo === 'CANCELACION' && this.subtipoCancelacion(solicitud) === 'Materia';
    }

    if (groupKey === 'CANCELACION_SEMESTRE') {
      return solicitud.tipo === 'CANCELACION' && this.subtipoCancelacion(solicitud) === 'Semestre';
    }

    return solicitud.tipo === groupKey;
  }
}
