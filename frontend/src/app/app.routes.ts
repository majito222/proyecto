import { Routes } from '@angular/router';
import { Inicio } from './componentes/inicio/inicio';
import { Login } from './componentes/login/login';
import { Registro } from './componentes/registro/registro';
import { ListaSolicitudes } from './componentes/lista-solicitudes/lista-solicitudes';
import { NuevaSolicitud } from './componentes/nueva-solicitud/nueva-solicitud';

export const routes: Routes = [
  { path: '', component: Inicio },
  { path: 'login', component: Login },
  { path: 'registro', component: Registro },
  { path: 'lista-solicitudes', component: ListaSolicitudes },
  { path: 'nueva-solicitud', component: NuevaSolicitud },
  { path: '**', pathMatch: 'full', redirectTo: '/' },
];