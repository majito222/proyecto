# Guia Explicativa del Proyecto - Entrega 02

## 1. Introduccion

### 1.1 Que hace el sistema

El proyecto implementa el backend de un sistema de triage y gestion de solicitudes academicas. Permite registrar solicitudes, clasificarlas, priorizarlas, asignarlas a responsables, controlar su ciclo de vida y conservar un historial auditable de eventos.

### 1.2 Problema que resuelve

Cuando las solicitudes academicas llegan por varios canales y se administran manualmente, aparecen problemas como:

- falta de trazabilidad
- tiempos de respuesta altos
- ausencia de responsables definidos
- priorizacion inconsistente
- poca visibilidad del estado de cada caso

La API busca centralizar ese flujo y volverlo controlado, auditable y consumible por clientes HTTP.

### 1.3 Objetivo de la entrega

La entrega demuestra:

- modelado de dominio
- organizacion por capas inspirada en arquitectura hexagonal
- casos de uso desacoplados
- API REST documentada
- persistencia JPA con H2
- seguridad JWT con Spring Security

## 2. Arquitectura General

### 2.1 Estructura del proyecto

```text
co.edu.uniquindio.proyecto
|- domain
|- application
`- infrastructure
```

### 2.2 Responsabilidad de cada capa

#### `domain`

Contiene el nucleo del negocio:

- entidades `Solicitud` y `Usuario`
- value objects
- excepciones de dominio
- contratos de repositorio

#### `application`

Contiene los casos de uso:

- crear solicitud
- clasificar
- priorizar
- asignar responsable
- iniciar o finalizar atencion
- cerrar o cancelar
- crear o consultar usuarios

#### `infrastructure`

Contiene detalles tecnicos:

- controllers REST
- DTOs y mappers
- persistencia JPA
- seguridad JWT
- manejo de errores HTTP

### 2.3 Flujo principal

```text
Request HTTP
 -> JwtAuthenticationFilter
 -> SecurityContext
 -> Controller
 -> UseCase
 -> Domain
 -> Repository
 -> Adapter JPA
 -> Base de datos
```

## 3. Estado Actual de la Implementacion

### 3.1 Casos de uso implementados

La entrega ya contiene casos de uso concretos, por ejemplo:

- `CrearSolicitudUseCase`
- `ClasificarSolicitudUseCase`
- `PriorizarSolicitudUseCase`
- `AsignarResponsableUseCase`
- `IniciarAtencionUseCase`
- `MarcarAtendidaUseCase`
- `CerrarSolicitudUseCase`
- `CancelarSolicitudUseCase`
- `ConsultarSolicitudesUseCase`
- `ConsultarSolicitudPorCodigoUseCase`
- `CrearUsuarioUseCase`

### 3.2 API REST

Los controllers expuestos son:

- `SolicitudController`
- `UsuarioController`
- `AuthController`
- `HealthController`

### 3.3 Persistencia

La persistencia usa entidades JPA, `JpaRepository` y adapters que implementan puertos del dominio.

### 3.4 Seguridad

La seguridad integrada incluye:

- login en `POST /api/auth/login`
- JWT HS256
- filtro `JwtAuthenticationFilter`
- `SecurityFilterChain` stateless
- autorizacion por rol con `@PreAuthorize`
- passwords cifradas con BCrypt
- respuestas JSON uniformes para `401` y `403`

## 4. Capa Domain

### 4.1 Entidades

#### `Solicitud`

Controla:

- estado actual
- tipo
- prioridad
- responsable
- historial

#### `Usuario`

Controla:

- identificacion
- email
- tipo de usuario
- estado
- password hash para autenticacion

### 4.2 Value objects

Entre los principales:

- `CodigoSolicitud`
- `IdUsuario`
- `Email`
- `DescripcionSolicitud`
- `EstadoSolicitud`
- `TipoSolicitud`
- `TipoCanal`
- `PrioridadSolicitud`
- `TipoUsuario`
- `EstadoUsuario`
- `Historial`

### 4.3 Reglas de negocio

El dominio aplica reglas como:

- una solicitud inicia en `REGISTRADA`
- no toda transicion de estado es valida
- solo solicitudes `ATENDIDA` pueden cerrarse
- una solicitud `CERRADA` o `CANCELADA` no puede cancelarse de nuevo
- toda accion relevante genera historial
- el tipo y estado del usuario influyen en las operaciones permitidas

## 5. Capa Application

### 5.1 Rol de los casos de uso

Los casos de uso:

- consultan entidades a traves de puertos
- verifican condiciones funcionales
- delegan la logica central al dominio
- persisten cambios

### 5.2 Ejemplos

#### `CrearSolicitudUseCase`

- recibe canal, tipo y descripcion
- usa al estudiante autenticado
- valida que el usuario pueda registrar solicitudes
- crea la entidad y la guarda

#### `CrearUsuarioUseCase`

- recibe nombre, email, password y tipo
- aplica hash de password mediante `PasswordHasher`
- construye el usuario y lo persiste

#### `CerrarSolicitudUseCase`

- carga la solicitud
- valida que el actor sea administrador activo
- delega el cierre al dominio
- guarda el resultado

## 6. Infrastructure REST

### 6.1 Controllers

Los controllers:

- validan entrada
- toman el actor autenticado
- delegan al caso de uso
- mapean dominio a DTO

### 6.2 DTOs y validaciones

Se usan `record` para request y response.

Validaciones presentes:

- `@NotBlank`
- `@NotNull`
- `@Size`
- `@Email`

Cambios importantes del estado actual:

- `CrearSolicitudRequest` ya no pide `estudianteId`
- `ClasificarSolicitudRequest` y `PriorizarSolicitudRequest` ya no piden el ID del actor
- `CrearUsuarioRequest` ahora exige `password`

## 7. Mappers

Se usan mappers explicitos para separar modelos:

- REST -> dominio: `SolicitudRequestMapper`, `UsuarioRequestMapper`
- dominio -> REST: `SolicitudMapper`, `UsuarioMapper`
- persistencia -> dominio: `SolicitudPersistenceMapper`, `UsuarioPersistenceMapper`

## 8. Persistencia JPA

### 8.1 Componentes

- `SolicitudEntity`
- `UsuarioEntity`
- `SolicitudJpaDataRepository`
- `UsuarioJpaDataRepository`
- `SolicitudJpaRepositoryImpl`
- `UsuarioJpaRepositoryImpl`

### 8.2 Capacidades

- CRUD
- filtros
- paginacion
- busqueda por texto
- consultas avanzadas de solicitudes

## 9. Seguridad JWT

### 9.1 Flujo de autenticacion

1. El cliente hace login con email y password.
2. `AuthenticationManager` valida credenciales.
3. Se genera JWT firmado con HS256.
4. El cliente envia `Authorization: Bearer <token>`.
5. `JwtAuthenticationFilter` valida token y autentica el request.

### 9.2 Roles y autorizacion

La autorizacion actual queda asi:

- `ESTUDIANTE`: crear solicitudes
- `FUNCIONARIO`: clasificar, priorizar, iniciar/finalizar atencion
- `ADMINISTRADOR`: gestionar usuarios, asignar responsable, cerrar solicitudes
- `FUNCIONARIO` o `ADMINISTRADOR`: cancelar solicitudes

### 9.3 Mejora importante

El actor de las operaciones sensibles ya no viaja en el body. Se toma desde el usuario autenticado, evitando suplantacion por cambio de IDs en el request.

### 9.4 Passwords

Las passwords:

- se almacenan con BCrypt
- se hashean al crear usuarios por API
- se normalizan si existen registros legacy sin hash

## 10. Excepciones y errores HTTP

La API tiene manejo centralizado con `GlobalExceptionHandler`.

Se controlan errores como:

- `400` por validacion o argumentos invalidos
- `404` por recurso no encontrado
- `409` por transiciones de estado invalidas
- `500` por errores inesperados

En seguridad:

- `401` sale por `RestAuthenticationEntryPoint`
- `403` sale por `RestAccessDeniedHandler`

## 11. Endpoints Principales

### 11.1 Autenticacion

- `POST /api/auth/login`

### 11.2 Usuarios

- `POST /api/v1/usuarios`
- `GET /api/v1/usuarios`
- `GET /api/v1/usuarios/{id}`

### 11.3 Solicitudes

- `POST /api/v1/solicitudes`
- `GET /api/v1/solicitudes`
- `GET /api/v1/solicitudes/{codigo}`
- `GET /api/v1/solicitudes/{codigo}/historial`
- `PUT /api/v1/solicitudes/{codigo}/responsable`
- `POST /api/v1/solicitudes/{codigo}/clasificacion`
- `POST /api/v1/solicitudes/{codigo}/prioridad`
- `POST /api/v1/solicitudes/{codigo}/atencion/inicio`
- `POST /api/v1/solicitudes/{codigo}/atencion/finalizacion`
- `POST /api/v1/solicitudes/{codigo}/cierre`
- `POST /api/v1/solicitudes/{codigo}/cancelacion`

### 11.4 Consultas avanzadas

- `GET /api/v1/solicitudes/gui11/estado-prioridad/{estado}`
- `GET /api/v1/solicitudes/gui11/codigo`
- `GET /api/v1/solicitudes/gui11/activas`
- `GET /api/v1/solicitudes/gui11/pendientes-alta`

## 12. Cumplimiento resumido por requerimiento

### RF-01 Registro de solicitudes

- `SolicitudController`
- `CrearSolicitudUseCase`
- `Solicitud`

### RF-02 Clasificacion

- `SolicitudController`
- `ClasificarSolicitudUseCase`
- `Solicitud`

### RF-03 Priorizacion

- `SolicitudController`
- `PriorizarSolicitudUseCase`
- `PrioridadSolicitud`
- `Solicitud`

### RF-04 Ciclo de vida

- `IniciarAtencionUseCase`
- `MarcarAtendidaUseCase`
- `CerrarSolicitudUseCase`
- `CancelarSolicitudUseCase`
- `Solicitud`

### RF-05 Responsable

- `SolicitudController`
- `AsignarResponsableUseCase`
- `Solicitud`

### RF-06 Historial

- `Solicitud`
- `ConsultarSolicitudPorCodigoUseCase`
- `SolicitudController`

### RF-07 Consultas y paginacion

- `ConsultarSolicitudesUseCase`
- `ConsultarSolicitudesAvanzadasUseCase`
- `SolicitudJpaDataRepository`
- `PaginaResponse`

### RF-08 Usuarios

- `UsuarioController`
- `CrearUsuarioUseCase`
- `UsuarioJpaRepositoryImpl`

### RF-13 Seguridad JWT

- `AuthController`
- `AuthService`
- `SecurityConfig`
- `JwtService`
- `JwtAuthenticationFilter`
- `CustomUserDetails`
- `RestAuthenticationEntryPoint`
- `RestAccessDeniedHandler`

## 13. Decisiones de diseno

### 13.1 Por que arquitectura hexagonal

Porque ayuda a:

- separar negocio de tecnologia
- mantener entidades y reglas mas limpias
- facilitar pruebas y evolucion

### 13.2 Por que DTOs con `record`

Porque:

- reducen codigo repetitivo
- son inmutables
- expresan mejor que son contratos de transporte

### 13.3 Por que JWT

Porque es apropiado para APIs REST:

- stateless
- simple de consumir desde frontend
- facil de integrar con Spring Security

## 14. Mejoras pendientes

- mover `jwt.secret` y credenciales de arranque a variables de entorno
- sacar `Page` y `Pageable` de los puertos del dominio
- reducir dependencias de Spring dentro de `application`
- rediseñar algunas rutas para acercarlas mas a REST puro
- agregar pruebas de integracion de seguridad mas profundas

## 15. Conclusion

La Entrega 02 ya muestra un backend funcional y coherente con los objetivos academicos principales:

- separacion por capas
- casos de uso identificables
- API REST documentada
- persistencia con JPA y H2
- DTOs, mappers y validaciones
- seguridad JWT con control de acceso por rol

El cambio mas importante frente al estado previo es que la seguridad ya no solo autentica: ahora tambien usa el usuario autenticado como actor real de las operaciones y endurece la autorizacion del sistema.
