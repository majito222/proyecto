export type TipoSolicitud =
  'REGISTRO_ASIGNATURA' | 'CANCELACION_ASIGNATURA' | 'HOMOLOGACION' | 'OTROS';

export type EstadoSolicitud =
  'REGISTRADA' | 'CLASIFICADA' | 'EN_ATENCION' | 'ATENDIDA' | 'CERRADA' | 'CANCELADA';

export type Prioridad = 'BAJA' | 'MEDIA' | 'ALTA';

export interface SolicitudResumen {
  id: string;
  codigo: string;
  estado: EstadoSolicitud;
  tipo: TipoSolicitud;
  prioridad: Prioridad;
  fechaCreacion: string;
  nombrePropietario: string;
}

export interface CrearSolicitudRequest {
  tipo: TipoSolicitud;
  descripcion: string;
  solicitanteId: string;
}