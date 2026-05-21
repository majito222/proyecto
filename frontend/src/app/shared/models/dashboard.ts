export type DashboardStatus = 'Pendiente' | 'En revision' | 'Aprobada' | 'Rechazada';

export interface DashboardSolicitud {
  id: string;
  estudiante: string;
  programa: string;
  tipo: string;
  estado: DashboardStatus;
  prioridad: 'Baja' | 'Media' | 'Alta' | 'Critica';
  fecha: string;
  asignadoA: string;
  comentario?: string;
}

export interface Materia {
  codigo: string;
  nombre: string;
  programa: string;
  creditos: number;
  inscritos: number;
  progreso: number;
  profesor: string;
}

export interface Usuario {
  id: string;
  nombre: string;
  email: string;
  rol: 'ESTUDIANTE' | 'FUNCIONARIO' | 'ADMINISTRADOR' | 'PROFESOR';
  programa: string;
  estado: 'Activo' | 'Inactivo';
}

export interface Programa {
  codigo: string;
  nombre: string;
  estudiantes: number;
  materias: number;
}

export interface Tarea {
  titulo: string;
  materia: string;
  fecha: string;
  prioridad: 'Baja' | 'Media' | 'Alta';
}

export interface Anuncio {
  titulo: string;
  detalle: string;
  fecha: string;
  tipo: 'Academico' | 'Financiero' | 'Bienestar';
}

export interface Actividad {
  titulo: string;
  detalle: string;
  fecha: string;
  icono: string;
  color: string;
}

export interface LogAuditoria {
  fecha: string;
  usuario: string;
  accion: string;
  modulo: string;
  resultado: 'OK' | 'Revision' | 'Bloqueado';
}
