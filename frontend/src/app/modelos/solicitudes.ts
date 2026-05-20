export type TipoSolicitud =
  'REGISTRO_ASIGNATURA' | 'HOMOLOGACION' | 'CANCELACION' | 'SOLICITUD_CUPO' | 'CONSULTA_ACADEMICA';

export type CanalSolicitud = 'CSU' | 'CORREO' | 'SAC' | 'TELEFONICO';

export type EstadoSolicitud =
  'REGISTRADA' | 'CLASIFICADA' | 'EN_ATENCION' | 'ATENDIDA' | 'CERRADA' | 'CANCELADA';

export type Prioridad = 'BAJA' | 'MEDIA' | 'ALTA' | 'CRITICA';

export interface SolicitudResumen {
  codigo: string;
  estudianteId: string;
  estudianteNombre: string;
  estado: EstadoSolicitud;
  tipo: TipoSolicitud;
  prioridad: Prioridad | null;
  descripcion?: string | null;
  fechaCreacion?: string | null;
}

export interface SolicitudDetalle extends SolicitudResumen {
  canal: CanalSolicitud;
  descripcion: string;
  historial: unknown[];
}

export interface CrearSolicitudRequest {
  canal: CanalSolicitud;
  tipo: TipoSolicitud;
  descripcion: string;
}

export interface PaginaResponse<T> {
  contenido: T[];
  pagina: number;
  tamano: number;
  totalElementos: number;
  totalPaginas: number;
  primera: boolean;
  ultima: boolean;
  vacia: boolean;
}
