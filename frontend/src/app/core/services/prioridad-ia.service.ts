import { Injectable } from '@angular/core';
import { CanalSolicitud, Prioridad, TipoSolicitud } from '../../shared/models/solicitudes';

export interface PrioridadSugerida {
  prioridad: Prioridad;
  justificacion: string;
}

@Injectable({ providedIn: 'root' })
export class PrioridadIaService {
  sugerir(tipo: TipoSolicitud, canal: CanalSolicitud, descripcion: string): PrioridadSugerida {
    const texto = descripcion.toLowerCase();

    if (texto.includes('urgente') || texto.includes('matricula') || tipo === 'SOLICITUD_CUPO') {
      return {
        prioridad: 'ALTA',
        justificacion: 'Sugerida por el tipo o descripcion de la solicitud.'
      };
    }

    if (canal === 'CSU' || tipo === 'HOMOLOGACION') {
      return {
        prioridad: 'MEDIA',
        justificacion: 'Sugerida por canal o tipo de solicitud.'
      };
    }

    return {
      prioridad: 'BAJA',
      justificacion: 'Sugerida automaticamente.'
    };
  }
}
