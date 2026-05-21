import { Component, DestroyRef, computed, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
import { finalize } from 'rxjs';
import { Button } from 'primeng/button';
import { Card } from 'primeng/card';
import { Fluid } from 'primeng/fluid';
import { IftaLabel } from 'primeng/iftalabel';
import { InputText } from 'primeng/inputtext';
import { Message } from 'primeng/message';
import { Password } from 'primeng/password';
import { UsuariosService } from '../../../core/services/usuarios.service';

@Component({
  selector: 'app-registro',
  imports: [ReactiveFormsModule, RouterLink, Card, Fluid, IftaLabel, InputText, Password, Button, Message],
  templateUrl: './registro.html',
  styleUrl: './registro.css',
})
export class Registro {
  private readonly fb = inject(FormBuilder);
  private readonly usuariosService = inject(UsuariosService);
  private readonly router = inject(Router);
  private readonly destroyRef = inject(DestroyRef);

  readonly isLoading = signal(false);
  readonly result = signal<string | null>(null);

  readonly registroForm = this.fb.nonNullable.group({
    nombre: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(100)]],
    confirmarPassword: ['', [Validators.required]]
  });

  private readonly formStatus = toSignal(this.registroForm.statusChanges, {
    initialValue: this.registroForm.status
  });

  private readonly formValue = toSignal(this.registroForm.valueChanges, {
    initialValue: this.registroForm.getRawValue()
  });

  readonly passwordsMatch = computed(() => {
    const value = this.formValue();
    return value.password === value.confirmarPassword;
  });

  readonly canSubmit = computed(() => this.formStatus() === 'VALID' && this.passwordsMatch() && !this.isLoading());

  registrar(): void {
    if (!this.canSubmit()) {
      this.registroForm.markAllAsTouched();
      return;
    }

    const { nombre, email, password } = this.registroForm.getRawValue();
    this.isLoading.set(true);
    this.result.set(null);

    this.usuariosService.crear({
      nombre,
      email,
      password,
      tipo: 'ESTUDIANTE'
    }).pipe(
      finalize(() => this.isLoading.set(false)),
      takeUntilDestroyed(this.destroyRef)
    ).subscribe({
      next: () => {
        this.result.set('Cuenta creada correctamente. Ahora puedes iniciar sesion.');
        void this.router.navigateByUrl('/login');
      },
      error: () => {
        this.result.set('No fue posible crear la cuenta. Revisa los datos e intenta de nuevo.');
      }
    });
  }

  hasError(controlName: 'nombre' | 'email' | 'password' | 'confirmarPassword', error: string): boolean {
    const control = this.registroForm.controls[controlName];
    return control.hasError(error) && (control.dirty || control.touched);
  }
}
