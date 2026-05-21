import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { toSignal } from '@angular/core/rxjs-interop';
import { MessageService } from 'primeng/api';
import { Button } from 'primeng/button';
import { Card } from 'primeng/card';
import { ChartModule } from 'primeng/chart';
import { DialogModule } from 'primeng/dialog';
import { DrawerModule } from 'primeng/drawer';
import { InputText } from 'primeng/inputtext';
import { Skeleton } from 'primeng/skeleton';
import { TableModule } from 'primeng/table';
import { Tag } from 'primeng/tag';
import { ToastModule } from 'primeng/toast';
import { ToolbarModule } from 'primeng/toolbar';
import { DashboardService } from '../../../core/services/dashboard.service';

@Component({
  selector: 'app-admin-dashboard',
  imports: [FormsModule, Button, Card, ChartModule, DialogModule, DrawerModule, InputText, Skeleton, TableModule, Tag, ToastModule, ToolbarModule],
  templateUrl: './admin-dashboard.html',
  styleUrl: './admin-dashboard.css'
})
export class AdminDashboard {
  private readonly dashboard = inject(DashboardService);
  private readonly messages = inject(MessageService);
  private readonly ready = toSignal(this.dashboard.adminData$, { initialValue: false });

  readonly loading = computed(() => !this.ready());
  readonly solicitudes = this.dashboard.solicitudes;
  readonly usuarios = this.dashboard.usuarios;
  readonly programas = this.dashboard.programas;
  readonly materias = this.dashboard.materias;
  readonly actividad = this.dashboard.actividad;
  readonly logs = this.dashboard.logs;
  readonly globalQuery = signal('');
  readonly auditoriaVisible = signal(false);
  readonly filtrosVisible = signal(false);

  readonly estudiantesActivos = this.dashboard.estudiantesActivos;
  readonly profesoresActivos = this.dashboard.profesoresActivos;
  readonly solicitudesPendientes = this.dashboard.solicitudesPendientes;
  readonly materiasActivas = this.dashboard.materiasActivas;
  readonly programasActivos = computed(() => this.programas().length);

  readonly solicitudesFiltradas = computed(() => {
    const query = this.globalQuery().trim().toLowerCase();
    if (!query) return this.solicitudes();
    return this.solicitudes().filter((solicitud) =>
      [solicitud.id, solicitud.estudiante, solicitud.programa, solicitud.tipo, solicitud.estado]
        .some((value) => value.toLowerCase().includes(query))
    );
  });

  readonly estadoChart = computed(() => {
    const estados = this.dashboard.solicitudesPorEstado();
    return {
      labels: ['Pendiente', 'En revision', 'Aprobada', 'Rechazada'],
      datasets: [{ data: [estados['Pendiente'] ?? 0, estados['En revision'] ?? 0, estados['Aprobada'] ?? 0, estados['Rechazada'] ?? 0], backgroundColor: ['#f59e0b', '#38bdf8', '#22c55e', '#ef4444'] }]
    };
  });

  readonly estudiantesProgramaChart = computed(() => ({
    labels: this.programas().map((programa) => programa.codigo),
    datasets: [{ label: 'Estudiantes', data: this.programas().map((programa) => programa.estudiantes), backgroundColor: '#22c55e', borderRadius: 8 }]
  }));

  readonly materiasChart = computed(() => ({
    labels: this.materias().map((materia) => materia.codigo),
    datasets: [{ label: 'Inscritos', data: this.materias().map((materia) => materia.inscritos), backgroundColor: '#a78bfa', borderRadius: 8 }]
  }));

  readonly actividadChart = {
    labels: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun'],
    datasets: [
      { label: 'Solicitudes', data: [42, 57, 61, 72, 89, 76], borderColor: '#38bdf8', backgroundColor: 'rgba(56,189,248,.18)', fill: true, tension: 0.35 },
      { label: 'Usuarios', data: [18, 22, 28, 31, 40, 37], borderColor: '#22c55e', backgroundColor: 'rgba(34,197,94,.15)', fill: true, tension: 0.35 }
    ]
  };

  readonly chartOptions = {
    maintainAspectRatio: false,
    plugins: { legend: { labels: { color: '#dbeafe' } } },
    scales: {
      x: { ticks: { color: '#9aa8bd' }, grid: { color: '#1f2937' } },
      y: { ticks: { color: '#9aa8bd' }, grid: { color: '#1f2937' } }
    }
  };

  abrirAuditoria(): void {
    this.auditoriaVisible.set(true);
    this.messages.add({ severity: 'info', summary: 'Auditoria', detail: 'Logs cargados correctamente' });
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
