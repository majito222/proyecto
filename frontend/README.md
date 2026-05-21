# Plataforma Universitaria - Frontend Angular 21

Aplicacion frontend para gestion academica universitaria con autenticacion JWT, dashboards por rol, solicitudes academicas, datos simulados para demo y UI moderna con PrimeNG 21.

## Stack

- Angular 21 con standalone components
- Signals, computed, inject y control flow moderno
- Angular Router con guards funcionales
- HttpClient con interceptor JWT
- PrimeNG 21, PrimeIcons y Chart.js
- TypeScript estricto

## Instalacion y ejecucion

```bash
npm install
npm start
```

Build de produccion:

```bash
npm run build
```

## Arquitectura

```text
src/app/
|- core/
|  |- auth/
|  |- guards/
|  |- interceptors/
|  |- layouts/
|  |- services/
|  `- utils/
|- shared/
|  |- components/
|  |- constants/
|  |- dto/
|  |- interfaces/
|  |- models/
|  `- pipes/
|- features/
|  |- auth/
|  |- dashboard/
|  |  |- admin/
|  |  |- estudiante/
|  |  `- funcionario/
|  |- home/
|  |- materias/
|  |- programas/
|  |- solicitudes/
|  `- usuarios/
`- app.routes.ts
```

`core` contiene servicios singleton, autenticacion, guards e interceptores. `shared` agrupa modelos y componentes reutilizables. `features` contiene pantallas por dominio para mantener bajo acoplamiento y carga perezosa por ruta.

## Dashboards Por Rol

### Estudiante

Ruta: `/dashboard/estudiante`

Incluye solicitudes recientes, estado de solicitudes, materias inscritas, tareas pendientes, promedio acumulado, anuncios recientes y calendario academico. Usa `p-card`, `p-tag`, `p-table`, `p-chart`, `p-timeline`, `p-progressBar`, skeleton loaders e iconos.

### Funcionario

Ruta: `/dashboard/funcionario`

Incluye solicitudes asignadas, pendientes, aprobadas, rechazadas, carga de trabajo, actividad reciente, busqueda avanzada, filtros, cambio de estado, comentarios, dialogos y toast notifications.

### Administrador

Ruta: `/dashboard/admin`

Incluye estudiantes activos, profesores activos, solicitudes totales, solicitudes pendientes, programas, materias activas, graficas institucionales, buscador global, filtros laterales, logs y auditoria.

## Roles

Roles soportados:

- `ESTUDIANTE`
- `FUNCIONARIO`
- `ADMINISTRADOR`

`AuthService` normaliza roles recibidos como `ROLE_ADMINISTRADOR` o `ADMINISTRADOR`, expone `roles`, `primaryRole`, `sessionLabel` y calcula la ruta de dashboard con `dashboardUrl()`.

## Signals

La aplicacion usa signals para estado de sesion, datos simulados y UI:

- `AuthService`: `isAuthenticated`, `roles`, `primaryRole`, `userEmail`, `sessionLabel`.
- `DashboardService`: solicitudes, usuarios, materias, programas, tareas, anuncios, actividad, logs.
- Dashboards: filtros, loading states, dialogos, comentarios y metricas derivadas con `computed`.

Esto evita consultar `localStorage` desde componentes y mantiene la UI reactiva.

## Guards

- `authGuard`: exige sesion activa y redirige a `/login`.
- `publicGuard`: evita abrir login/registro si ya hay sesion y redirige al dashboard del rol.
- `rolesGuard`: valida `data.expectedRoles` y redirige a `/unauthorized` si el rol no coincide.

## Interceptor JWT

`authInterceptor` lee el token desde `AuthService`, agrega `Authorization: Bearer <token>` a peticiones protegidas y cierra sesion ante errores `401`.

Flujo:

```text
Login -> API JWT -> AuthService guarda token/roles -> Router entra a dashboard -> Interceptor adjunta JWT -> API valida permisos
```

## Routing

Las rutas usan lazy loading con `loadComponent`:

- `/`
- `/login`
- `/registro`
- `/dashboard/estudiante`
- `/dashboard/funcionario`
- `/dashboard/admin`
- `/nueva-solicitud`
- `/lista-solicitudes`
- `/unauthorized`

## PrimeNG

La UI usa PrimeNG 21 con tema Aura configurado en `app.config.ts`. Componentes principales:

- `p-card`, `p-table`, `p-tag`, `p-chart`
- `p-timeline`, `p-progressBar`, `p-dialog`
- `p-toast`, `p-toolbar`, `p-drawer`
- `p-skeleton`, `p-select`, `p-button`

`chart.js` esta instalado para habilitar `p-chart`.

## Servicios

- `AuthService`: login, logout, token, roles y dashboard de destino.
- `DashboardService`: datos simulados, metricas reactivas y mutaciones de solicitudes.
- `SolicitudesService`: integracion HTTP para listar y crear solicitudes.
- `UsuariosService`: integracion HTTP para crear usuarios.
- `MateriasService`: base para integracion de materias y estado simulado.
- `ProgramasService`: base para integracion de programas y estado simulado.

## Datos Simulados

`DashboardService` genera solicitudes, usuarios, materias, programas, anuncios, tareas, actividad reciente y logs. Esto permite que los dashboards se vean funcionales desde el primer arranque, incluso sin backend completo para esas vistas.

## Alias

`tsconfig.json` define:

- `@core/*`
- `@shared/*`
- `@features/*`

## Validacion

El proyecto compila con:

```bash
npm run build
```
