import { Component, DestroyRef, computed, inject, signal } from '@angular/core';
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
import { SolicitudesService } from '../../servicios/solicitudes';
import { CanalSolicitud, Prioridad, TipoSolicitud } from '../../modelos/solicitudes';
import { PrioridadIaService } from '../../servicios/prioridad-ia';

@Component({
  selector: 'app-nueva-solicitud',
  imports: [ReactiveFormsModule, Card, Fluid, IftaLabel, Select, Textarea, Button, Message, Tag],
  templateUrl: './nueva-solicitud.html',
  styleUrl: './nueva-solicitud.css'
})
export class NuevaSolicitud {
  private readonly fb = inject(FormBuilder);
  private readonly solicitudesService = inject(SolicitudesService);
  private readonly prioridadIa = inject(PrioridadIaService);
  private readonly router = inject(Router);
  private readonly destroyRef = inject(DestroyRef);

  readonly isLoading = signal(false);
  readonly result = signal<string | null>(null);
  readonly tipos: TipoSolicitud[] = ['REGISTRO_ASIGNATURA', 'HOMOLOGACION', 'CANCELACION', 'SOLICITUD_CUPO', 'CONSULTA_ACADEMICA'];
  readonly canales: CanalSolicitud[] = ['SAC', 'CSU', 'CORREO', 'TELEFONICO'];

  readonly solicitudForm = this.fb.nonNullable.group({
    canal: this.fb.nonNullable.control<CanalSolicitud>('SAC', Validators.required),
    tipo: this.fb.nonNullable.control<TipoSolicitud>('CONSULTA_ACADEMICA', Validators.required),
    descripcion: ['', [Validators.required, Validators.minLength(20), Validators.maxLength(1000)]]
  });

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

  readonly canSubmit = computed(() => this.formStatus() === 'VALID' && !this.isLoading());

  crear(): void {
    if (!this.canSubmit()) {
      this.solicitudForm.markAllAsTouched();
      return;
    }

    this.isLoading.set(true);
    this.result.set(null);

    this.solicitudesService.crear(this.solicitudForm.getRawValue()).pipe(
      finalize(() => this.isLoading.set(false)),
      takeUntilDestroyed(this.destroyRef)
    ).subscribe({
      next: () => void this.router.navigateByUrl('/lista-solicitudes'),
      error: () => this.result.set('No fue posible crear la solicitud.')
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
}
