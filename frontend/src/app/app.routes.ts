import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { publicGuard } from './core/guards/public.guard';
import { rolesGuard } from './core/guards/roles.guard';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./features/home/inicio/inicio').then((m) => m.Inicio)
  },
  {
    path: 'login',
    canActivate: [publicGuard],
    loadComponent: () => import('./features/auth/login/login').then((m) => m.Login)
  },
  {
    path: 'registro',
    canActivate: [publicGuard],
    loadComponent: () => import('./features/auth/registro/registro').then((m) => m.Registro)
  },
  {
    path: 'dashboard',
    canActivate: [authGuard],
    children: [
      { path: '', redirectTo: 'estudiante', pathMatch: 'full' },
      {
        path: 'estudiante',
        canActivate: [rolesGuard],
        data: { expectedRoles: ['ESTUDIANTE'] },
        loadComponent: () => import('./features/dashboard/estudiante/estudiante-dashboard').then((m) => m.EstudianteDashboard)
      },
      {
        path: 'funcionario',
        canActivate: [rolesGuard],
        data: { expectedRoles: ['FUNCIONARIO'] },
        loadComponent: () => import('./features/dashboard/funcionario/funcionario-dashboard').then((m) => m.FuncionarioDashboard)
      },
      {
        path: 'admin',
        canActivate: [rolesGuard],
        data: { expectedRoles: ['ADMINISTRADOR'] },
        loadComponent: () => import('./features/dashboard/admin/admin-dashboard').then((m) => m.AdminDashboard)
      }
    ]
  },
  {
    path: 'nueva-solicitud',
    canActivate: [authGuard],
    loadComponent: () => import('./features/solicitudes/nueva-solicitud/nueva-solicitud').then((m) => m.NuevaSolicitud)
  },
  {
    path: 'lista-solicitudes',
    canActivate: [authGuard],
    loadComponent: () => import('./features/solicitudes/lista-solicitudes/lista-solicitudes').then((m) => m.ListaSolicitudes)
  },
  {
    path: 'admin',
    canActivate: [rolesGuard],
    data: { expectedRoles: ['ADMINISTRADOR'] },
    loadComponent: () => import('./features/dashboard/admin/admin-dashboard').then((m) => m.AdminDashboard)
  },
  {
    path: 'unauthorized',
    loadComponent: () => import('./shared/components/unauthorized/unauthorized').then((m) => m.Unauthorized)
  },
  { path: '**', redirectTo: '' }
];
