import { Component, DestroyRef, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { SolicitudesService } from '../../servicios/solicitudes';
import { CrearSolicitudRequest, TipoSolicitud } from '../../modelos/solicitudes';

@Component({
  selector: 'app-nueva-solicitud',
  imports: [ReactiveFormsModule],
  templateUrl: './nueva-solicitud.html',
  styleUrl: './nueva-solicitud.css'
})
export class NuevaSolicitud {
  private solicitudesService = inject(SolicitudesService);
  private router = inject(Router);
  private destroyRef = inject(DestroyRef);

  result = signal('');
  isLoading = signal(false);

  tipos: TipoSolicitud[] = [
    'REGISTRO_ASIGNATURA',
    'CANCELACION_ASIGNATURA',
    'HOMOLOGACION',
    'OTROS'
  ];

  solicitudForm = inject(FormBuilder).group({
    tipo: ['', Validators.required],
    descripcion: ['', [Validators.required, Validators.minLength(10)]],
    solicitanteId: ['', Validators.required]
  });

  onSubmit(): void {
    if (this.solicitudForm.invalid) return;
    this.isLoading.set(true);

    const solicitud = this.solicitudForm.value as CrearSolicitudRequest;
    this.solicitudesService.crear(solicitud)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (response) => {
          this.result.set(`Solicitud creada: ${response.codigo}`);
          this.solicitudForm.reset();
          this.isLoading.set(false);
          setTimeout(() => this.router.navigate(['/lista-solicitudes']), 2000);
        },
        error: () => {
          this.result.set('Error al crear la solicitud. Verifica los datos e intenta de nuevo.');
          this.isLoading.set(false);
        }
      });
  }
}