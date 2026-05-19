import { Component, inject } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { Button } from 'primeng/button';
import { AuthService } from './servicios/auth';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, RouterLinkActive, Button],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  readonly auth = inject(AuthService);
}
