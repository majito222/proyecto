import { Component, computed, inject } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { RouterLink } from '@angular/router';
import { Card } from 'primeng/card';
import { ChartModule } from 'primeng/chart';
import { ProgressBar } from 'primeng/progressbar';
import { Skeleton } from 'primeng/skeleton';
import { TableModule } from 'primeng/table';
import { Tag } from 'primeng/tag';
import { TimelineModule } from 'primeng/timeline';
import { DashboardService } from '../../../core/services/dashboard.service';

@Component({
  selector: 'app-estudiante-dashboard',
  imports: [RouterLink, Card, ChartModule, ProgressBar, Skeleton, TableModule, Tag, TimelineModule],
  templateUrl: './estudiante-dashboard.html',
  styleUrl: './estudiante-dashboard.css'
})
export class EstudianteDashboard {
  private readonly dashboard = inject(DashboardService);
  private readonly ready = toSignal(this.dashboard.estudianteData$, { initialValue: false });

  readonly loading = computed(() => !this.ready());
  readonly solicitudes = computed(() => this.dashboard.solicitudes().slice(0, 5));
  readonly materias = this.dashboard.materias;
  readonly tareas = this.dashboard.tareas;
  readonly anuncios = this.dashboard.anuncios;
  readonly actividades = this.dashboard.actividad;
  readonly promedio = computed(() => 4.32);
  readonly creditos = computed(() => this.materias().reduce((total, materia) => total + materia.creditos, 0));
  readonly progresoSemestre = computed(() => Math.round(this.materias().reduce((total, materia) => total + materia.progreso, 0) / this.materias().length));
  readonly solicitudesPendientes = computed(() => this.solicitudes().filter((solicitud) => solicitud.estado === 'Pendiente' || solicitud.estado === 'En revision').length);

  readonly estadoChart = computed(() => {
    const estados = this.dashboard.solicitudesPorEstado();
    return {
      labels: ['Pendiente', 'En revision', 'Aprobada', 'Rechazada'],
      datasets: [
        {
          data: [estados['Pendiente'] ?? 0, estados['En revision'] ?? 0, estados['Aprobada'] ?? 0, estados['Rechazada'] ?? 0],
          backgroundColor: ['#f59e0b', '#38bdf8', '#22c55e', '#ef4444'],
          borderWidth: 0
        }
      ]
    };
  });

  readonly cargaChart = computed(() => ({
    labels: this.materias().map((materia) => materia.codigo),
    datasets: [
      {
        label: 'Avance',
        data: this.materias().map((materia) => materia.progreso),
        backgroundColor: '#38bdf8',
        borderRadius: 8
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
