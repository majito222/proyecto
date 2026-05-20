import { Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';
import { publicGuard } from './guards/public.guard';
import { rolesGuard } from './guards/roles.guard';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./componentes/inicio/inicio').then((m) => m.Inicio)
  },
  {
    path: 'login',
    canActivate: [publicGuard],
    loadComponent: () => import('./componentes/login/login').then((m) => m.Login)
  },
  {
    path: 'registro',
    canActivate: [publicGuard],
    loadComponent: () => import('./componentes/registro/registro').then((m) => m.Registro)
  },
  {
    path: 'nueva-solicitud',
    canActivate: [authGuard],
    loadComponent: () => import('./componentes/nueva-solicitud/nueva-solicitud').then((m) => m.NuevaSolicitud)
  },
  {
    path: 'lista-solicitudes',
    canActivate: [authGuard],
    loadComponent: () => import('./componentes/lista-solicitudes/lista-solicitudes').then((m) => m.ListaSolicitudes)
  },
  {
    path: 'admin',
    canActivate: [rolesGuard],
    data: { expectedRoles: ['ADMINISTRADOR'] },
    loadComponent: () => import('./componentes/admin/admin').then((m) => m.Admin)
  },
  {
    path: 'unauthorized',
    loadComponent: () => import('./componentes/unauthorized/unauthorized').then((m) => m.Unauthorized)
  },
  { path: '**', redirectTo: '' }
];
