import { Component, DestroyRef, computed, inject, signal } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
import { finalize, map } from 'rxjs';
import { Button } from 'primeng/button';
import { Card } from 'primeng/card';
import { Fluid } from 'primeng/fluid';
import { IftaLabel } from 'primeng/iftalabel';
import { InputText } from 'primeng/inputtext';
import { Message } from 'primeng/message';
import { Select } from 'primeng/select';
import { Tag } from 'primeng/tag';
import { Textarea } from 'primeng/textarea';
import { SolicitudesService } from '../../../core/services/solicitudes.service';
import { CanalSolicitud, Prioridad, TipoSolicitud } from '../../../shared/models/solicitudes';
import { PrioridadIaService } from '../../../core/services/prioridad-ia.service';
import { AuthService } from '../../../core/auth/auth.service';
import { AsistenteIaService } from '../../../core/services/asistente-ia.service';

@Component({
  selector: 'app-nueva-solicitud',
  imports: [ReactiveFormsModule, Card, Fluid, IftaLabel, Select, InputText, Textarea, Button, Message, Tag],
  templateUrl: './nueva-solicitud.html',
  styleUrl: './nueva-solicitud.css'
})
export class NuevaSolicitud {
  private readonly fb = inject(FormBuilder);
  private readonly solicitudesService = inject(SolicitudesService);
  private readonly prioridadIa = inject(PrioridadIaService);
  private readonly asistenteIa = inject(AsistenteIaService);
  readonly auth = inject(AuthService);
  private readonly router = inject(Router);
  private readonly destroyRef = inject(DestroyRef);

  readonly isLoading = signal(false);
  readonly result = signal<string | null>(null);
  readonly aiResponse = signal<string | null>(null);
  readonly tipos: TipoSolicitud[] = ['REGISTRO_ASIGNATURA', 'HOMOLOGACION', 'CANCELACION', 'SOLICITUD_CUPO', 'CONSULTA_ACADEMICA'];
  readonly canales: CanalSolicitud[] = ['SAC', 'CSU', 'CORREO', 'TELEFONICO'];
  readonly tiposCancelacion: Array<'MATERIA' | 'SEMESTRE'> = ['MATERIA', 'SEMESTRE'];
  private readonly ayudasPorTipo: Record<TipoSolicitud, { titulo: string; descripcion: string; ejemplo: string }> = {
    REGISTRO_ASIGNATURA: {
      titulo: 'Registro de asignatura',
      descripcion: 'Usa esta opcion cuando necesitas apoyo para inscribir una materia, solucionar cruces o revisar problemas con cupos en el registro academico.',
      ejemplo: 'Ejemplo: Necesito registrar Programacion Avanzada, pero el sistema no me permite inscribirla por un cruce de horario.'
    },
    HOMOLOGACION: {
      titulo: 'Homologacion',
      descripcion: 'Usa esta opcion para solicitar revision de materias cursadas en otro programa o institucion y validar si pueden reconocerse en tu plan de estudios.',
      ejemplo: 'Ejemplo: Solicito revisar la homologacion de Calculo I cursada en otra universidad durante el semestre anterior.'
    },
    CANCELACION: {
      titulo: 'Cancelacion',
      descripcion: 'Usa esta opcion para pedir la cancelacion de una asignatura, semestre o proceso academico, explicando claramente el motivo.',
      ejemplo: 'Ejemplo: Solicito cancelar la asignatura Bases de Datos por motivos medicos y adjunto soporte correspondiente.'
    },
    SOLICITUD_CUPO: {
      titulo: 'Solicitud de cupo',
      descripcion: 'Usa esta opcion cuando necesitas cupo en una asignatura o grupo que aparece lleno o no disponible en el sistema.',
      ejemplo: 'Ejemplo: Solicito cupo para Ingenieria de Software II en el grupo 02 porque es requisito para continuar mi plan.'
    },
    CONSULTA_ACADEMICA: {
      titulo: 'Consulta academica',
      descripcion: 'Usa esta opcion para dudas generales sobre procesos, estado academico, fechas, requisitos o informacion institucional.',
      ejemplo: 'Ejemplo: Quiero consultar las novedades academicas del semestre actual y los requisitos para matricula.'
    }
  };

  readonly solicitudForm = this.fb.nonNullable.group({
    canal: this.fb.nonNullable.control<CanalSolicitud>('SAC', Validators.required),
    tipo: this.fb.nonNullable.control<TipoSolicitud>('CONSULTA_ACADEMICA', Validators.required),
    cancelacion: this.fb.nonNullable.control<'MATERIA' | 'SEMESTRE'>('MATERIA'),
    materia: [''],
    grupo: [''],
    institucionOrigen: [''],
    periodo: [''],
    descripcion: ['', [Validators.required, Validators.minLength(20), Validators.maxLength(1000)]]
  });
  readonly aiQuestion = this.fb.nonNullable.control('');

  private readonly formStatus = toSignal(this.solicitudForm.statusChanges, {
    initialValue: this.solicitudForm.status
  });

  private readonly rawFormValue = toSignal(this.solicitudForm.valueChanges.pipe(
    map(() => this.solicitudForm.getRawValue())
  ), {
    initialValue: this.solicitudForm.getRawValue()
  });

  readonly prioridadSugerida = computed(() => {
    const value = this.rawFormValue();
    return this.prioridadIa.sugerir(value.tipo, value.canal, value.descripcion);
  });

  readonly ayudaTipo = computed(() => this.ayudasPorTipo[this.rawFormValue().tipo]);
  readonly caracteresDescripcion = computed(() => this.rawFormValue().descripcion.length);
  readonly puedeCrearSolicitud = computed(() => this.auth.hasAnyRole(['ESTUDIANTE']));
  readonly requiereMateria = computed(() => {
    const value = this.rawFormValue();
    return ['REGISTRO_ASIGNATURA', 'HOMOLOGACION', 'SOLICITUD_CUPO'].includes(value.tipo)
      || (value.tipo === 'CANCELACION' && value.cancelacion === 'MATERIA');
  });
  readonly canSubmit = computed(() => {
    const value = this.rawFormValue();
    const materiaValida = !this.requiereMateria() || value.materia.trim().length >= 3;
    return this.formStatus() === 'VALID' && materiaValida && !this.isLoading();
  });

  crear(): void {
    if (!this.canSubmit()) {
      this.solicitudForm.markAllAsTouched();
      return;
    }

    this.isLoading.set(true);
    this.result.set(null);

    const value = this.solicitudForm.getRawValue();
    this.solicitudesService.crear({
      canal: value.canal,
      tipo: value.tipo,
      descripcion: this.descripcionConDetalles()
    }).pipe(
      finalize(() => this.isLoading.set(false)),
      takeUntilDestroyed(this.destroyRef)
    ).subscribe({
      next: () => void this.router.navigateByUrl('/lista-solicitudes'),
      error: (error: unknown) => this.result.set(this.mensajeErrorCreacion(error))
  });
  }

  tagPrioridad(prioridad: Prioridad): 'success' | 'warn' | 'danger' {
    const severidades: Record<Prioridad, 'success' | 'warn' | 'danger'> = {
      BAJA: 'success',
      MEDIA: 'warn',
      ALTA: 'danger',
      CRITICA: 'danger'
    };

    return severidades[prioridad];
  }

  descripcionInvalida(): boolean {
    const control = this.solicitudForm.controls.descripcion;
    return control.invalid && (control.dirty || control.touched);
  }

  preguntarIA(): void {
    this.aiResponse.set(this.asistenteIa.responder(this.aiQuestion.value, this.contextoIa()));
  }

  generarDescripcionIA(): void {
    const descripcion = this.asistenteIa.generarDescripcion(this.contextoIa());
    this.solicitudForm.controls.descripcion.setValue(descripcion);
    this.solicitudForm.controls.descripcion.markAsDirty();
    this.aiResponse.set('Complete una descripcion base con los datos actuales. Puedes ajustarla antes de crear la solicitud.');
  }

  materiaInvalida(): boolean {
    const control = this.solicitudForm.controls.materia;
    return this.requiereMateria() && control.value.trim().length < 3 && (control.dirty || control.touched);
  }

  private descripcionConDetalles(): string {
    const value = this.solicitudForm.getRawValue();
    const detalles = [
      `Usuario: ${this.auth.userEmail() ?? 'No disponible'}`,
      `Rol: ${this.auth.sessionLabel()}`,
      `Tipo: ${value.tipo}`,
      value.tipo === 'CANCELACION' ? `Cancelacion de: ${value.cancelacion}` : null,
      value.materia.trim() ? `Materia: ${value.materia.trim()}` : null,
      value.grupo.trim() ? `Grupo: ${value.grupo.trim()}` : null,
      value.institucionOrigen.trim() ? `Institucion origen: ${value.institucionOrigen.trim()}` : null,
      value.periodo.trim() ? `Periodo: ${value.periodo.trim()}` : null,
      `Descripcion: ${value.descripcion.trim()}`
    ].filter((detalle): detalle is string => Boolean(detalle));

    return detalles.join('\n');
  }

  private contextoIa() {
    const value = this.solicitudForm.getRawValue();
    return {
      tipo: value.tipo,
      cancelacion: value.cancelacion,
      materia: value.materia,
      grupo: value.grupo,
      institucionOrigen: value.institucionOrigen,
      periodo: value.periodo
    };
  }

  private mensajeErrorCreacion(error: unknown): string {
    if (error instanceof HttpErrorResponse) {
      if (error.status === 403) {
        return 'No tienes permiso para crear solicitudes con este usuario. Inicia sesion con una cuenta de estudiante.';
      }

      const mensaje = error.error?.mensaje || error.error?.message;
      if (typeof mensaje === 'string' && mensaje.trim()) {
        return mensaje;
      }
    }

    return 'No fue posible crear la solicitud. Revisa los datos e intenta nuevamente.';
  }
}
