# Frontend - Sistema de Triage Academico

Frontend Angular para el sistema de gestion de solicitudes academicas. La aplicacion esta orientada a autenticar usuarios, registrar solicitudes y consultar el estado de solicitudes consumiendo una API Spring Boot protegida con JWT.

## Tecnologias

- Angular 21
- TypeScript
- Angular standalone components
- Angular Router
- Angular HttpClient
- Reactive Forms
- Signals
- PrimeNG 21
- PrimeIcons
- Bootstrap, si se decide mantenerlo como utilitario global

## Instalacion

Desde esta carpeta:

```bash
npm install
```

## Ejecucion

```bash
npm start
```

La aplicacion se sirve normalmente en:

```text
http://localhost:4200
```

## Backend

El frontend debe consumir la API en:

```text
http://localhost:8080
```

Endpoints esperados:

- `POST /api/auth/login`
- `GET /api/v1/solicitudes`
- `POST /api/v1/solicitudes`

## Estructura esperada

```text
src/
|- app/
|  |- app.config.ts
|  |- app.routes.ts
|  |- app.ts
|  |- componentes/
|  |  |- inicio/
|  |  |- login/
|  |  |- nueva-solicitud/
|  |  `- lista-solicitudes/
|  |- interceptores/
|  |  `- auth-interceptor.ts
|  |- modelos/
|  `- servicios/
|     |- auth.ts
|     `- solicitudes.ts
|- index.html
`- main.ts
```

## Rutas

Rutas recomendadas:

- `/`
- `/login`
- `/nueva-solicitud`
- `/lista-solicitudes`
- `**`

## PrimeNG

La configuracion moderna de PrimeNG 21 debe declararse en `app.config.ts` con providers globales:

```ts
import Aura from '@primeuix/themes/aura';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { providePrimeNG } from 'primeng/config';

export const appConfig: ApplicationConfig = {
  providers: [
    provideAnimationsAsync(),
    providePrimeNG({
      theme: {
        preset: Aura,
        options: {
          darkModeSelector: '.app-dark'
        }
      }
    })
  ]
};
```

PrimeNG 21 usa styled mode: los estilos se generan desde design tokens del preset seleccionado. Aura es un preset oficial que define color, spacing, radios, superficies, estados y variantes visuales. En este modo no se importan temas CSS antiguos como en versiones legacy.

## Componentes PrimeNG recomendados

Login:

- `Card`
- `Fluid`
- `IftaLabel`
- `InputText`
- `Password`
- `Button`
- `Message`

Lista de solicitudes:

- `Card`
- `Tag`
- PrimeIcons como `pi pi-plus`, `pi pi-calendar` y `pi pi-lock`

En Angular standalone, estos componentes se importan directamente en el arreglo `imports` del componente. No se deben usar modulos legacy como `InputTextModule` o `ButtonModule` si el proyecto adopta el patron moderno por componentes standalone.

## Autenticacion JWT

Flujo esperado:

1. El usuario envia email y password desde el login.
2. `AuthService` llama a `POST /api/auth/login`.
3. El backend responde con token JWT y roles.
4. El frontend guarda `token` y `roles` en `localStorage`.
5. `authInterceptor` agrega `Authorization: Bearer <token>` a las peticiones protegidas.
6. `logout()` limpia token, roles y actualiza `isAuthenticated` a `false`.

## Guards

El frontend usa guards funcionales modernos (`CanActivateFn`) y no clases legacy.

- `authGuard`: protege rutas privadas. Si no existe sesion activa devuelve `router.createUrlTree(['/login'])`.
- `publicGuard`: bloquea `/login` y `/registro` cuando el usuario ya esta autenticado y redirige a `/lista-solicitudes`.
- `rolesGuard`: valida autenticacion y roles desde `route.data['expectedRoles']`. Si falta sesion redirige a `/login`; si faltan permisos redirige a `/unauthorized`.

Todos los guards usan `inject()`, `Router`, `createUrlTree()` y `AuthService`.

## Interceptor JWT

El interceptor esta implementado como `HttpInterceptorFn`.

Responsabilidades:

- Lee el token desde `AuthService.getToken()`.
- No agrega `Authorization` al endpoint `/api/auth/login`.
- Clona la peticion con `req.clone()`.
- Agrega `Authorization: Bearer <token>` a peticiones protegidas.
- Captura respuestas `401`.
- Ejecuta `logout()` automaticamente y redirige a `/login`.

## Flujo JWT completo

```text
Login -> AuthService.login() -> localStorage token/roles -> isAuthenticated signal
-> authInterceptor -> Authorization: Bearer <token> -> API protegida
-> 401 -> logout automatico -> redirect /login
```

## Rutas protegidas

- `/login`: publica, protegida con `publicGuard`.
- `/registro`: publica, protegida con `publicGuard`.
- `/nueva-solicitud`: privada, protegida con `authGuard`.
- `/lista-solicitudes`: privada, protegida con `authGuard`.
- `/admin`: privada por rol, protegida con `rolesGuard` y `expectedRoles: ['ADMINISTRADOR']`.
- `/unauthorized`: pagina de acceso denegado.
- `**`: wildcard hacia inicio.

## Resumen de seguridad

La seguridad del frontend evita navegacion accidental a pantallas privadas y automatiza el envio del JWT. La autorizacion real debe seguir estando en el backend Spring Boot, porque cualquier validacion frontend puede ser manipulada desde el navegador.

Los roles se guardan en `localStorage` para persistencia de sesion y tambien en signals para que la UI reaccione sin recargar. Al cerrar sesion o recibir `401`, se limpian token y roles.

## Signals y formularios

El login debe combinar Reactive Forms con signals:

- `FormBuilder` para crear el formulario.
- `Validators.required`, `Validators.email` y `Validators.minLength`.
- `toSignal()` para transformar `statusChanges` o `valueChanges`.
- `computed()` para derivar `canSubmit`.
- `signal()` para estado local como `isLoading` y `result`.

Ejemplo conceptual:

```ts
readonly isLoading = signal(false);
readonly result = signal<string | null>(null);
readonly formStatus = toSignal(this.loginForm.statusChanges, {
  initialValue: this.loginForm.status
});
readonly canSubmit = computed(() => this.formStatus() === 'VALID' && !this.isLoading());
```

## Nivel del proyecto

Nivel actual: intermedio en frontend.

El proyecto ya usa Angular standalone, rutas lazy, PrimeNG 21 en styled mode, login reactivo con signals, interceptor JWT y una lista de solicitudes tipada. El siguiente salto de calidad esta en guards, manejo global de errores, ambientes por entorno y componentes de tabla mas completos.

## Recomendaciones

- Agregar refresh token si el backend lo soporta.
- Usar `p-table` cuando la lista crezca y necesite paginacion, filtros u ordenamiento.
- Mover URLs de API a archivos de environment.
- Agregar toast global para errores de API y expiracion de sesion.
- Mantener Bootstrap solo para utilidades globales si no compite visualmente con PrimeNG.
