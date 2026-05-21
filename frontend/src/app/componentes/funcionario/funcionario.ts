import { Component, computed, inject, signal } from '@angular/core';
import { NgClass } from '@angular/common';
import { map } from 'rxjs';
import { SolicitudesService } from '../../servicios/solicitudes';
import { AuthService } from '../../servicios/auth';
import { SolicitudResumen, TipoSolicitud, Prioridad } from '../../modelos/solicitudes';

@Component({
  selector: 'app-funcionario',
  imports: [NgClass],
  templateUrl: './funcionario.html',
  styleUrl: './funcionario.css'
})
export class Funcionario {
  private readonly solicitudesService = inject(SolicitudesService);
  private readonly auth = inject(AuthService);

  solicitudes = signal<SolicitudResumen[]>([]);
  solicitudSeleccionada = signal<SolicitudResumen | null>(null);
  isLoading = signal(false);
  result = signal<string | null>(null);
  nivelSeleccionado = signal<Prioridad>('MEDIA');
  justificacion = signal('');
  tipoSeleccionado = signal<TipoSolicitud>('CONSULTA_ACADEMICA');

  tipos: TipoSolicitud[] = [
    'REGISTRO_ASIGNATURA', 'HOMOLOGACION', 'CANCELACION', 'SOLICITUD_CUPO', 'CONSULTA_ACADEMICA'
  ];

  niveles: Prioridad[] = ['BAJA', 'MEDIA', 'ALTA', 'CRITICA'];

  totalSolicitudes = computed(() => this.solicitudes().length);
  registradas = computed(() => this.solicitudes().filter(s => s.estado === 'REGISTRADA').length);
  enAtencion = computed(() => this.solicitudes().filter(s => s.estado === 'EN_ATENCION').length);

  constructor() {
    this.cargarSolicitudes();
  }

  cargarSolicitudes(): void {
    this.solicitudesService.listar().pipe(
      map(res => res.contenido ?? [])
    ).subscribe({
      next: (data) => this.solicitudes.set(data),
      error: () => this.result.set('Error al cargar solicitudes.')
    });
  }

  seleccionar(solicitud: SolicitudResumen): void {
    this.solicitudSeleccionada.set(solicitud);
    this.result.set(null);
    this.tipoSeleccionado.set(solicitud.tipo);
  }

  get funcionarioId(): string {
    return this.auth.getUserId() ?? '';
  }

  clasificar(): void {
    const sol = this.solicitudSeleccionada();
    if (!sol) return;
    this.isLoading.set(true);
    this.solicitudesService.clasificar(sol.codigo, this.funcionarioId, this.tipoSeleccionado())
      .subscribe({
        next: () => {
          this.result.set('Solicitud clasificada correctamente ✅');
          this.isLoading.set(false);
          this.cargarSolicitudes();
        },
        error: () => {
          this.result.set('Error al clasificar.');
          this.isLoading.set(false);
        }
      });
  }

  priorizar(): void {
    const sol = this.solicitudSeleccionada();
    if (!sol || !this.justificacion().trim()) {
      this.result.set('Ingresa una justificación.');
      return;
    }
    this.isLoading.set(true);
    this.solicitudesService.priorizar(sol.codigo, this.funcionarioId, this.nivelSeleccionado(), this.justificacion())
      .subscribe({
        next: () => {
          this.result.set('Prioridad asignada correctamente ✅');
          this.isLoading.set(false);
          this.cargarSolicitudes();
        },
        error: () => {
          this.result.set('Error al priorizar.');
          this.isLoading.set(false);
        }
      });
  }

  iniciarAtencion(): void {
    const sol = this.solicitudSeleccionada();
    if (!sol) return;
    this.isLoading.set(true);
    this.solicitudesService.iniciarAtencion(sol.codigo, this.funcionarioId)
      .subscribe({
        next: () => {
          this.result.set('Atención iniciada correctamente ✅');
          this.isLoading.set(false);
          this.cargarSolicitudes();
        },
        error: () => {
          this.result.set('Error al iniciar atención.');
          this.isLoading.set(false);
        }
      });
  }

  finalizarAtencion(): void {
    const sol = this.solicitudSeleccionada();
    if (!sol) return;
    this.isLoading.set(true);
    this.solicitudesService.finalizarAtencion(sol.codigo, this.funcionarioId)
      .subscribe({
        next: () => {
          this.result.set('Atención finalizada correctamente ✅');
          this.isLoading.set(false);
          this.cargarSolicitudes();
        },
        error: () => {
          this.result.set('Error al finalizar atención.');
          this.isLoading.set(false);
        }
      });
  }

  badgeEstado(estado: string): string {
    const mapa: Record<string, string> = {
      REGISTRADA: 'badge-registrada',
      CLASIFICADA: 'badge-clasificada',
      EN_ATENCION: 'badge-en-atencion',
      ATENDIDA: 'badge-atendida',
      CERRADA: 'badge-cerrada',
      CANCELADA: 'badge-cancelada',
    };
    return mapa[estado] ?? 'badge-registrada';
  }
}