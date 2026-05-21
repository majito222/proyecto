import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { toSignal } from '@angular/core/rxjs-interop';
import { MessageService } from 'primeng/api';
import { Badge } from 'primeng/badge';
import { Button } from 'primeng/button';
import { Card } from 'primeng/card';
import { ChartModule } from 'primeng/chart';
import { DialogModule } from 'primeng/dialog';
import { InputText } from 'primeng/inputtext';
import { Select } from 'primeng/select';
import { Skeleton } from 'primeng/skeleton';
import { TableModule } from 'primeng/table';
import { Tag } from 'primeng/tag';
import { Textarea } from 'primeng/textarea';
import { TimelineModule } from 'primeng/timeline';
import { ToastModule } from 'primeng/toast';
import { DashboardService } from '../../../core/services/dashboard.service';
import { DashboardSolicitud, DashboardStatus } from '../../../shared/models/dashboard';

@Component({
  selector: 'app-funcionario-dashboard',
  imports: [FormsModule, Badge, Button, Card, ChartModule, DialogModule, InputText, Select, Skeleton, TableModule, Tag, Textarea, TimelineModule, ToastModule],
  templateUrl: './funcionario-dashboard.html',
  styleUrl: './funcionario-dashboard.css'
})
export class FuncionarioDashboard {
  private readonly dashboard = inject(DashboardService);
  private readonly messages = inject(MessageService);
  private readonly ready = toSignal(this.dashboard.funcionarioData$, { initialValue: false });

  readonly loading = computed(() => !this.ready());
  readonly solicitudes = this.dashboard.solicitudes;
  readonly actividad = this.dashboard.actividad;
  readonly busqueda = signal('');
  readonly estadoFiltro = signal<DashboardStatus | 'Todas'>('Todas');
  readonly solicitudSeleccionada = signal<DashboardSolicitud | null>(null);
  readonly comentario = signal('');
  readonly dialogVisible = signal(false);
  readonly estados: Array<DashboardStatus | 'Todas'> = ['Todas', 'Pendiente', 'En revision', 'Aprobada', 'Rechazada'];

  readonly filtradas = computed(() => {
    const query = this.busqueda().trim().toLowerCase();
    return this.solicitudes().filter((solicitud) => {
      const estadoOk = this.estadoFiltro() === 'Todas' || solicitud.estado === this.estadoFiltro();
      const textoOk = !query || [solicitud.id, solicitud.estudiante, solicitud.programa, solicitud.tipo, solicitud.prioridad]
        .some((value) => value.toLowerCase().includes(query));
      return estadoOk && textoOk;
    });
  });

  readonly pendientes = computed(() => this.solicitudes().filter((s) => s.estado === 'Pendiente').length);
  readonly aprobadas = computed(() => this.solicitudes().filter((s) => s.estado === 'Aprobada').length);
  readonly rechazadas = computed(() => this.solicitudes().filter((s) => s.estado === 'Rechazada').length);
  readonly asignadas = computed(() => this.solicitudes().length);
  readonly cargaTrabajo = computed(() => Math.min(100, Math.round((this.pendientes() / Math.max(this.asignadas(), 1)) * 100 + 38)));

  readonly workloadChart = computed(() => ({
    labels: ['Pendientes', 'En revision', 'Aprobadas', 'Rechazadas'],
    datasets: [
      {
        label: 'Solicitudes',
        data: [
          this.pendientes(),
          this.solicitudes().filter((s) => s.estado === 'En revision').length,
          this.aprobadas(),
          this.rechazadas()
        ],
        borderColor: '#38bdf8',
        backgroundColor: 'rgba(56, 189, 248, 0.22)',
        fill: true,
        tension: 0.35
      }
    ]
  }));

  readonly chartOptions = {
    maintainAspectRatio: false,
    plugins: { legend: { labels: { color: '#dbeafe' } } },
    scales: {
      x: { ticks: { color: '#9aa8bd' }, grid: { color: '#1f2937' } },
      y: { ticks: { color: '#9aa8bd' }, grid: { color: '#1f2937' } }
    }
  };

  abrirComentario(solicitud: DashboardSolicitud): void {
    this.solicitudSeleccionada.set(solicitud);
    this.comentario.set(solicitud.comentario ?? '');
    this.dialogVisible.set(true);
  }

  guardarComentario(): void {
    const solicitud = this.solicitudSeleccionada();
    if (!solicitud) return;
    this.dashboard.agregarComentario(solicitud.id, this.comentario());
    this.dialogVisible.set(false);
    this.messages.add({ severity: 'success', summary: 'Comentario guardado', detail: solicitud.id });
  }

  cambiarEstado(solicitud: DashboardSolicitud, estado: DashboardStatus): void {
    this.dashboard.cambiarEstado(solicitud.id, estado);
    this.messages.add({ severity: 'info', summary: 'Estado actualizado', detail: `${solicitud.id} -> ${estado}` });
  }

  estadoSeverity(estado: string): 'success' | 'info' | 'warn' | 'danger' {
    const severities: Record<string, 'success' | 'info' | 'warn' | 'danger'> = {
      Pendiente: 'warn',
      'En revision': 'info',
      Aprobada: 'success',
      Rechazada: 'danger'
    };
    return severities[estado] ?? 'info';
  }
}
