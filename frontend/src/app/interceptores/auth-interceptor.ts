import { inject } from '@angular/core';
import { HttpInterceptorFn } from '@angular/common/http';
import { AuthService } from '../servicios/auth';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = inject(AuthService).getToken();

  if (token) {
    const peticionAutenticada = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` }
    });
    return next(peticionAutenticada);
  }

  return next(req);
};