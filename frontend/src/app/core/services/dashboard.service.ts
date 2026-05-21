import { Injectable, computed, signal } from '@angular/core';
import { of, delay } from 'rxjs';
import {
  Actividad,
  Anuncio,
  DashboardSolicitud,
  DashboardStatus,
  LogAuditoria,
  Materia,
  Programa,
  Tarea,
  Usuario
} from '../../shared/models/dashboard';

@Injectable({ providedIn: 'root' })
export class DashboardService {
  readonly solicitudes = signal<DashboardSolicitud[]>([
    { id: 'SOL-2401', estudiante: 'Laura Perez', programa: 'Ingenieria de Sistemas', tipo: 'Homologacion', estado: 'Pendiente', prioridad: 'Alta', fecha: '2026-05-20', asignadoA: 'Camila Rojas' },
    { id: 'SOL-2402', estudiante: 'Miguel Torres', programa: 'Administracion', tipo: 'Solicitud de cupo', estado: 'En revision', prioridad: 'Critica', fecha: '2026-05-19', asignadoA: 'Andres Mejia', comentario: 'Validando disponibilidad con facultad.' },
    { id: 'SOL-2403', estudiante: 'Ana Gomez', programa: 'Psicologia', tipo: 'Cancelacion', estado: 'Aprobada', prioridad: 'Media', fecha: '2026-05-18', asignadoA: 'Camila Rojas' },
    { id: 'SOL-2404', estudiante: 'Carlos Diaz', programa: 'Derecho', tipo: 'Registro asignatura', estado: 'Rechazada', prioridad: 'Baja', fecha: '2026-05-16', asignadoA: 'Sofia Leon', comentario: 'No cumple prerrequisito.' },
    { id: 'SOL-2405', estudiante: 'Valentina Ruiz', programa: 'Ingenieria de Sistemas', tipo: 'Consulta academica', estado: 'Pendiente', prioridad: 'Media', fecha: '2026-05-15', asignadoA: 'Andres Mejia' },
    { id: 'SOL-2406', estudiante: 'Daniel Arias', programa: 'Contaduria', tipo: 'Homologacion', estado: 'Aprobada', prioridad: 'Alta', fecha: '2026-05-14', asignadoA: 'Sofia Leon' },
    { id: 'SOL-2407', estudiante: 'Juliana Mora', programa: 'Medicina', tipo: 'Solicitud de cupo', estado: 'En revision', prioridad: 'Alta', fecha: '2026-05-13', asignadoA: 'Camila Rojas' }
  ]);

  readonly materias = signal<Materia[]>([
    { codigo: 'SIS-401', nombre: 'Ingenieria de Software II', programa: 'Ingenieria de Sistemas', creditos: 3, inscritos: 42, progreso: 72, profesor: 'Diana Pardo' },
    { codigo: 'ADM-210', nombre: 'Gestion Financiera', programa: 'Administracion', creditos: 3, inscritos: 35, progreso: 64, profesor: 'Hector Salas' },
    { codigo: 'PSI-105', nombre: 'Psicologia Cognitiva', programa: 'Psicologia', creditos: 4, inscritos: 28, progreso: 81, profesor: 'Paula Bernal' },
    { codigo: 'DER-310', nombre: 'Derecho Constitucional', programa: 'Derecho', creditos: 3, inscritos: 31, progreso: 55, profesor: 'Rafael Castro' },
    { codigo: 'MED-220', nombre: 'Bioquimica Clinica', programa: 'Medicina', creditos: 4, inscritos: 46, progreso: 69, profesor: 'Natalia Vera' }
  ]);

  readonly usuarios = signal<Usuario[]>([
    { id: 'U-001', nombre: 'Laura Perez', email: 'laura@uni.edu', rol: 'ESTUDIANTE', programa: 'Ingenieria de Sistemas', estado: 'Activo' },
    { id: 'U-002', nombre: 'Miguel Torres', email: 'miguel@uni.edu', rol: 'ESTUDIANTE', programa: 'Administracion', estado: 'Activo' },
    { id: 'U-003', nombre: 'Camila Rojas', email: 'camila@uni.edu', rol: 'FUNCIONARIO', programa: 'Registro Academico', estado: 'Activo' },
    { id: 'U-004', nombre: 'Andres Mejia', email: 'andres@uni.edu', rol: 'FUNCIONARIO', programa: 'Admisiones', estado: 'Activo' },
    { id: 'U-005', nombre: 'Diana Pardo', email: 'diana@uni.edu', rol: 'PROFESOR', programa: 'Ingenieria de Sistemas', estado: 'Activo' },
    { id: 'U-006', nombre: 'Sofia Leon', email: 'sofia@uni.edu', rol: 'ADMINISTRADOR', programa: 'Planeacion', estado: 'Activo' }
  ]);

  readonly programas = signal<Programa[]>([
    { codigo: 'SIS', nombre: 'Ingenieria de Sistemas', estudiantes: 430, materias: 38 },
    { codigo: 'ADM', nombre: 'Administracion', estudiantes: 380, materias: 34 },
    { codigo: 'PSI', nombre: 'Psicologia', estudiantes: 290, materias: 31 },
    { codigo: 'DER', nombre: 'Derecho', estudiantes: 310, materias: 29 },
    { codigo: 'MED', nombre: 'Medicina', estudiantes: 260, materias: 42 }
  ]);

  readonly tareas = signal<Tarea[]>([
    { titulo: 'Subir soporte de homologacion', materia: 'Ingenieria de Software II', fecha: '2026-05-23', prioridad: 'Alta' },
    { titulo: 'Confirmar horario de laboratorio', materia: 'Bioquimica Clinica', fecha: '2026-05-24', prioridad: 'Media' },
    { titulo: 'Responder observacion de registro', materia: 'Gestion Financiera', fecha: '2026-05-27', prioridad: 'Baja' }
  ]);

  readonly anuncios = signal<Anuncio[]>([
    { titulo: 'Cierre de adicion de materias', detalle: 'El plazo vence el 27 de mayo de 2026.', fecha: '2026-05-21', tipo: 'Academico' },
    { titulo: 'Actualizacion de recibos', detalle: 'Tesoreria habilito pagos diferidos para matricula.', fecha: '2026-05-20', tipo: 'Financiero' },
    { titulo: 'Jornada de bienestar', detalle: 'Atencion psicologica y orientacion academica esta semana.', fecha: '2026-05-19', tipo: 'Bienestar' }
  ]);

  readonly actividad = signal<Actividad[]>([
    { titulo: 'Solicitud aprobada', detalle: 'SOL-2403 fue aprobada por Registro Academico.', fecha: 'Hace 15 min', icono: 'pi pi-check', color: '#22c55e' },
    { titulo: 'Comentario agregado', detalle: 'Funcionario actualizo SOL-2402.', fecha: 'Hace 45 min', icono: 'pi pi-comments', color: '#38bdf8' },
    { titulo: 'Auditoria ejecutada', detalle: 'Validacion de roles completada.', fecha: 'Hace 2 h', icono: 'pi pi-shield', color: '#f59e0b' },
    { titulo: 'Nueva solicitud', detalle: 'SOL-2407 entro a revision inicial.', fecha: 'Hace 3 h', icono: 'pi pi-inbox', color: '#a855f7' }
  ]);

  readonly logs = signal<LogAuditoria[]>([
    { fecha: '2026-05-21 07:25', usuario: 'admin@uni.edu', accion: 'Cambio de rol', modulo: 'Usuarios', resultado: 'OK' },
    { fecha: '2026-05-21 07:12', usuario: 'camila@uni.edu', accion: 'Aprobo solicitud', modulo: 'Solicitudes', resultado: 'OK' },
    { fecha: '2026-05-20 18:44', usuario: 'sistema', accion: 'Intento sin permiso', modulo: 'Dashboard', resultado: 'Bloqueado' },
    { fecha: '2026-05-20 17:30', usuario: 'andres@uni.edu', accion: 'Filtro exportado', modulo: 'Auditoria', resultado: 'Revision' }
  ]);

  readonly solicitudesPorEstado = computed(() => this.countBy(this.solicitudes(), (item) => item.estado));
  readonly estudiantesActivos = computed(() => this.usuarios().filter((u) => u.rol === 'ESTUDIANTE' && u.estado === 'Activo').length + 1670);
  readonly profesoresActivos = computed(() => this.usuarios().filter((u) => u.rol === 'PROFESOR' && u.estado === 'Activo').length + 214);
  readonly materiasActivas = computed(() => this.materias().length + 169);
  readonly solicitudesPendientes = computed(() => this.solicitudes().filter((s) => s.estado === 'Pendiente' || s.estado === 'En revision').length);

  estudianteData$ = of(true).pipe(delay(260));
  funcionarioData$ = of(true).pipe(delay(320));
  adminData$ = of(true).pipe(delay(380));

  cambiarEstado(id: string, estado: DashboardStatus): void {
    this.solicitudes.update((solicitudes) =>
      solicitudes.map((solicitud) => solicitud.id === id ? { ...solicitud, estado } : solicitud)
    );
  }

  agregarComentario(id: string, comentario: string): void {
    this.solicitudes.update((solicitudes) =>
      solicitudes.map((solicitud) => solicitud.id === id ? { ...solicitud, comentario } : solicitud)
    );
  }

  private countBy<T>(items: T[], selector: (item: T) => string): Record<string, number> {
    return items.reduce<Record<string, number>>((acc, item) => {
      const key = selector(item);
      acc[key] = (acc[key] ?? 0) + 1;
      return acc;
    }, {});
  }
}
