import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { DashboardService } from './dashboard.service';

@Injectable({ providedIn: 'root' })
export class MateriasService {
  private readonly http = inject(HttpClient);
  private readonly dashboard = inject(DashboardService);
  private readonly url = 'http://localhost:8080/api/v1/materias';

  readonly materias = this.dashboard.materias;

  listar() {
    return this.http.get(this.url);
  }
}
