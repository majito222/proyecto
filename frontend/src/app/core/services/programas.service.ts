import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { DashboardService } from './dashboard.service';

@Injectable({ providedIn: 'root' })
export class ProgramasService {
  private readonly http = inject(HttpClient);
  private readonly dashboard = inject(DashboardService);
  private readonly url = 'http://localhost:8080/api/v1/programas';

  readonly programas = this.dashboard.programas;

  listar() {
    return this.http.get(this.url);
  }
}
