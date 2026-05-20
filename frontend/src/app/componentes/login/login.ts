import { Component, DestroyRef, computed, inject, signal } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
import { Router, RouterLink } from '@angular/router';
import { finalize } from 'rxjs';
import { Button } from 'primeng/button';
import { Card } from 'primeng/card';
import { Fluid } from 'primeng/fluid';
import { IftaLabel } from 'primeng/iftalabel';
import { InputText } from 'primeng/inputtext';
import { Message } from 'primeng/message';
import { Password } from 'primeng/password';
import { AuthService } from '../../servicios/auth';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule, RouterLink, Card, Fluid, IftaLabel, InputText, Password, Button, Message],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {
  private readonly fb = inject(FormBuilder);
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);
  private readonly destroyRef = inject(DestroyRef);

  readonly isLoading = signal(false);
  readonly result = signal<string | null>(null);

  readonly loginForm = this.fb.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]]
  });

  private readonly formStatus = toSignal(this.loginForm.statusChanges, {
    initialValue: this.loginForm.status
  });

  readonly canSubmit = computed(() => this.formStatus() === 'VALID' && !this.isLoading());

  onSubmit(): void {
    if (!this.canSubmit()) {
      this.loginForm.markAllAsTouched();
      return;
    }

    this.isLoading.set(true);
    this.result.set(null);

    this.auth.login(this.loginForm.getRawValue()).pipe(
      finalize(() => this.isLoading.set(false)),
      takeUntilDestroyed(this.destroyRef)
    ).subscribe({
      next: () => {
        this.result.set('Inicio de sesion correcto.');
        void this.router.navigateByUrl('/lista-solicitudes');
      },
      error: () => {
        this.result.set('No fue posible iniciar sesion. Revisa tus credenciales.');
      }
    });
  }

  hasError(controlName: 'email' | 'password', error: 'required' | 'email' | 'minlength'): boolean {
    const control = this.loginForm.controls[controlName];
    return control.hasError(error) && (control.dirty || control.touched);
  }
}
