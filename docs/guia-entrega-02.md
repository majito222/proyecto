# Guia Explicativa del Proyecto - Entrega 02

## 1. Introduccion del Proyecto

### 1.1 Que hace el sistema

El proyecto implementa el backend de un **Sistema de Triage y Gestion de Solicitudes Academicas**. Su funcion es recibir solicitudes hechas por estudiantes, organizarlas, asignarles tipo y prioridad, entregarlas a un responsable, seguir su progreso y cerrarlas o cancelarlas segun corresponda.

### 1.2 Problema que resuelve

En muchos procesos academicos las solicitudes llegan por varios canales y se administran de forma dispersa. Eso genera:

- poca trazabilidad
- dificultad para saber en que estado va cada caso
- retrasos en la atencion
- falta de responsables claros
- poca consistencia en el manejo de prioridades

El sistema busca ordenar ese flujo para que cada solicitud tenga un ciclo de vida controlado.

### 1.3 Objetivo del backend

El backend tiene como objetivo:

- centralizar la gestion de solicitudes en una API REST
- aplicar reglas de negocio claras
- conservar historial de acciones
- persistir usuarios y solicitudes
- proteger el acceso con autenticacion JWT

En resumen, convierte un proceso disperso en un flujo estructurado, auditable y consumible por frontend o clientes API.

## 2. Arquitectura General

### 2.1 Que es la arquitectura hexagonal

La arquitectura hexagonal, tambien llamada **Ports and Adapters**, busca separar el negocio del sistema de la tecnologia que lo rodea.

La idea es que el nucleo del negocio no dependa directamente de:

- HTTP
- Spring
- JPA
- la base de datos

En cambio, el dominio define reglas e interfaces, y la infraestructura se conecta a ese dominio a traves de adaptadores.

### 2.2 Como esta organizada en el proyecto

#### `domain/`

Es el nucleo del negocio. Contiene:

- entidades
- value objects
- excepciones
- interfaces de repositorio
- servicios de dominio

#### `application/`

Contiene los **UseCases** o casos de uso. Esta capa coordina acciones funcionales como:

- crear solicitud
- asignar responsable
- clasificar
- priorizar
- iniciar atencion
- cerrar

#### `infrastructure/`

Contiene la parte tecnica del sistema:

- controllers REST
- mappers
- persistencia JPA
- seguridad JWT
- manejo global de excepciones

### 2.3 Flujo del sistema

El flujo principal puede explicarse asi:

```text
Controller -> UseCase -> Domain -> Repository -> Adapter JPA -> DB
```

Paso a paso:

1. El cliente envia una peticion HTTP.
2. El controller recibe el request y valida el DTO.
3. El caso de uso coordina la operacion.
4. El dominio aplica reglas del negocio.
5. El repositorio abstrae el acceso a datos.
6. El adapter JPA conecta con la base de datos.
7. El resultado vuelve al cliente como DTO de respuesta.

Con seguridad activada, antes del controller se valida tambien el token JWT.

## 3. Cambios Realizados en Esta Entrega

Respecto a una entrega mas centrada en dominio, esta version agrega una capa completa de aplicacion, exposicion HTTP, persistencia y seguridad.

### 3.1 Casos de uso

Se agregaron clases como:

- `CrearSolicitudUseCase`
- `ClasificarSolicitudUseCase`
- `AsignarResponsableUseCase`
- `PriorizarSolicitudUseCase`
- `IniciarAtencionUseCase`
- `MarcarAtendidaUseCase`
- `CerrarSolicitudUseCase`
- `CancelarSolicitudUseCase`
- `ConsultarSolicitudesUseCase`
- `ConsultarSolicitudPorCodigoUseCase`

Esto permite representar acciones funcionales reales del sistema.

### 3.2 API REST completa

Se crearon controladores REST como:

- `SolicitudController`
- `UsuarioController`
- `AuthController`
- `HealthController`

Con esto el sistema ya puede ser consumido por frontend, Postman o cualquier cliente HTTP.

### 3.3 DTOs con records

Se agregaron DTOs de entrada y salida usando `record`, por ejemplo:

- `CrearSolicitudRequest`
- `CrearUsuarioRequest`
- `LoginRequest`
- `SolicitudDetalleResponse`
- `UsuarioDetalleResponse`
- `LoginResponse`

Esto separa el contrato de la API del modelo de dominio.

### 3.4 Persistencia con JPA

Se incorporaron:

- `SolicitudEntity`
- `UsuarioEntity`
- `SolicitudJpaDataRepository`
- `UsuarioJpaDataRepository`
- adapters de persistencia

Asi el sistema ya guarda datos de forma permanente en H2 durante el desarrollo.

### 3.5 Seguridad con JWT

En esta version ya existe autenticacion con JWT:

- `SecurityFilterChain`
- endpoint `POST /api/auth/login`
- validacion de usuario con `AuthenticationManager`
- passwords cifradas con BCrypt
- generacion y validacion de token con `JwtService`
- filtro `JwtAuthenticationFilter`

### 3.6 Justificacion de los cambios

Estos cambios se hicieron porque un sistema empresarial no puede quedarse solo en el modelo de negocio. Necesita:

- operaciones de aplicacion concretas
- API REST para integracion
- persistencia de datos
- validaciones de entrada y salida
- seguridad para controlar acceso

## 4. Capa Domain (Negocio)

### 4.1 Que contiene

La capa `domain` contiene principalmente:

#### Entidades

- `Solicitud`
- `Usuario`

#### Value objects

Entre los mas importantes estan:

- `CodigoSolicitud`
- `IdUsuario`
- `Email`
- `DescripcionSolicitud`
- `PrioridadSolicitud`
- `EstadoSolicitud`
- `TipoSolicitud`
- `TipoCanal`
- `TipoUsuario`
- `EstadoUsuario`
- `Historial`

### 4.2 Que reglas de negocio maneja

El dominio controla reglas como:

- una solicitud inicia en estado `REGISTRADA`
- no todas las transiciones de estado son validas
- no se puede cerrar una solicitud si no esta `ATENDIDA`
- no se puede cancelar una solicitud ya cerrada
- las acciones importantes generan historial
- los usuarios deben estar activos para ejecutar ciertas acciones

Ejemplos claros estan en:

- `Solicitud.clasificarSolicitud(...)`
- `Solicitud.iniciarAtencion(...)`
- `Solicitud.marcarAtendida(...)`
- `Solicitud.cerrarSolicitud(...)`
- `Solicitud.cancelarSolicitud(...)`

### 4.3 Que no contiene

La capa domain no debe contener detalles tecnicos como:

- anotaciones `@Entity`
- anotaciones `@RestController`
- clases HTTP
- dependencias JPA dentro de entidades

Eso ayuda a mantener el negocio limpio y reusable.

## 5. Capa Application (Casos de Uso)

### 5.1 Que son los UseCases

Un caso de uso representa una accion concreta del sistema. No es solo una clase tecnica: es una operacion funcional.

Ejemplos:

- crear solicitud
- consultar solicitud
- asignar responsable
- cerrar solicitud

### 5.2 Que hace cada UseCase principal

#### `CrearSolicitudUseCase`

- busca al estudiante
- valida que sea estudiante activo
- crea la solicitud
- la guarda

#### `ClasificarSolicitudUseCase`

- busca la solicitud
- busca el funcionario
- valida permisos
- delega la clasificacion al dominio

#### `AsignarResponsableUseCase`

- consulta la solicitud
- consulta el funcionario
- valida que pueda atender solicitudes
- asigna el responsable

#### `PriorizarSolicitudUseCase`

- recibe el nivel de prioridad y la justificacion
- valida al responsable
- actualiza la solicitud

#### `IniciarAtencionUseCase` y `MarcarAtendidaUseCase`

- cambian el estado de la solicitud
- respetan el flujo definido por el dominio

#### `CerrarSolicitudUseCase`

- valida que quien cierra sea administrador activo
- registra la observacion
- cierra la solicitud

### 5.3 Como se comunican con el dominio

Los casos de uso orquestan, pero no reemplazan al dominio. Su trabajo es:

- cargar entidades desde repositorios
- llamar metodos del dominio
- persistir cambios

### 5.4 Como usan interfaces o puertos

La capa application trabaja con interfaces como:

- `SolicitudRepository`
- `UsuarioRepository`

Eso permite que los casos de uso no conozcan directamente JPA ni SQL.

## 6. Capa Infrastructure - API REST

### 6.1 Que hacen los Controllers

Los controllers reciben peticiones HTTP y las convierten en llamadas a casos de uso.

Ejemplos:

- `SolicitudController`
- `UsuarioController`
- `AuthController`

### 6.2 Como funcionan los endpoints

Un endpoint sigue este esquema:

1. recibe request HTTP
2. valida entrada
3. delega al caso de uso o servicio correspondiente
4. usa mappers para preparar respuesta
5. devuelve `ResponseEntity`

### 6.3 Uso de anotaciones

#### `@RestController`

Marca que la clase expone endpoints REST y responde JSON.

#### `@RequestMapping`

Define la ruta base del recurso. Por ejemplo:

- `/api/v1/solicitudes`
- `/api/v1/usuarios`
- `/api/auth`

#### `@Valid`

Activa validaciones automaticas sobre los DTOs de entrada.

### 6.4 Por que son controllers delgados

Se consideran delgados porque no contienen la logica de negocio principal. Su funcion es:

- recibir
- validar
- delegar
- responder

La logica real vive en `application`, `domain` y en el flujo de seguridad para autenticacion.

## 7. DTOs y Validaciones

### 7.1 Que son los DTOs

Los DTOs son objetos que transportan datos entre la API y el cliente. No representan el dominio interno.

### 7.2 Diferencia entre Request y Response

#### Request

Representa lo que entra al sistema.

Ejemplos:

- `CrearSolicitudRequest`
- `CerrarSolicitudRequest`
- `LoginRequest`

#### Response

Representa lo que devuelve la API.

Ejemplos:

- `SolicitudDetalleResponse`
- `UsuarioDetalleResponse`
- `LoginResponse`
- `ErrorResponse`

### 7.3 Por que se usan records

Se usan `record` porque:

- reducen codigo repetido
- son inmutables por defecto
- son ideales para transferencia de datos
- hacen mas claro que el DTO no contiene comportamiento complejo

### 7.4 Validaciones implementadas

Se usan anotaciones como:

- `@NotBlank`
- `@NotNull`
- `@Size`
- `@Email`

Ejemplo de seguridad:

- `LoginRequest` exige `email` valido y `password` obligatoria

Ademas, el dominio tambien valida por dentro. Por eso hay doble proteccion:

- validacion de API
- validacion de negocio

## 8. Mappers

### 8.1 Que hacen los mappers

Los mappers convierten objetos entre capas distintas.

### 8.2 DTO -> dominio

Ejemplos:

- `SolicitudRequestMapper`
- `UsuarioRequestMapper`

Transforman datos del request en value objects del dominio.

### 8.3 Dominio -> DTO

Ejemplos:

- `SolicitudMapper`
- `UsuarioMapper`

Convierten entidades del dominio en respuestas listas para la API.

### 8.4 Persistencia -> dominio

En la capa JPA tambien existen mappers como:

- `SolicitudPersistenceMapper`
- `UsuarioPersistenceMapper`

Su trabajo es separar el modelo relacional del modelo de negocio.

### 8.5 Por que son importantes

Porque ayudan a:

- no mezclar capas
- evitar logica de conversion repetida
- no exponer entidades internas
- mantener controllers y adapters mas limpios

## 9. Persistencia (JPA)

### 9.1 Que es `@Entity`

`@Entity` indica que una clase representa una tabla de base de datos.

En este proyecto existen:

- `SolicitudEntity`
- `UsuarioEntity`

Estas clases son distintas de las entidades de dominio. Esa separacion es intencional.

### 9.2 Que es `JpaRepository`

`JpaRepository` permite hacer CRUD y consultas sin escribir toda la capa de acceso a datos manualmente.

Ejemplos:

- `SolicitudJpaDataRepository`
- `UsuarioJpaDataRepository`

### 9.3 Que es el adapter de persistencia

El adapter conecta el dominio con JPA.

Ejemplos:

- `SolicitudJpaRepositoryImpl`
- `UsuarioJpaRepositoryImpl`

Estos adapters:

- implementan interfaces del dominio
- usan `JpaRepository`
- convierten entre dominio y entidades persistentes

### 9.4 Como se conecta con el dominio

La conexion se ve asi:

1. `domain` define `SolicitudRepository`
2. `infrastructure` implementa ese contrato
3. el adapter usa `SolicitudJpaDataRepository`
4. los mappers convierten `Solicitud <-> SolicitudEntity`

### 9.5 Base de datos usada

La base usada es **H2**.

Ventajas para esta entrega:

- arranca rapido
- no requiere instalacion externa
- es ideal para desarrollo y pruebas

## 10. Consultas Avanzadas (Guia 11)

### 10.1 Tipos de consultas implementadas

Se usan dos enfoques principales.

#### Query methods

Son metodos derivados del nombre, por ejemplo:

- `findByEmail(...)`
- `findByTipo(...)`

#### `@Query`

Tambien hay consultas personalizadas para:

- busquedas por texto
- solicitudes activas
- solicitudes por estado y prioridad
- solicitudes de alta prioridad sin asignar

### 10.2 Filtros

El sistema permite filtrar solicitudes por:

- estado
- canal
- texto

### 10.3 Paginacion con `Pageable`

La paginacion se usa para:

- listar solicitudes
- listar usuarios
- consultar historial
- ejecutar consultas avanzadas

La API devuelve `PaginaResponse<T>` con:

- contenido
- pagina
- tamano
- total de elementos
- total de paginas

### 10.4 Ejemplos del proyecto

- `/api/v1/solicitudes?estado=REGISTRADA&pagina=0&tamano=10`
- `/api/v1/solicitudes/gui11/activas`
- `/api/v1/solicitudes/gui11/estado-prioridad/{estado}`
- `/api/v1/solicitudes/gui11/codigo`

## 11. Seguridad (JWT)

### 11.1 Como funciona la autenticacion

La autenticacion se hace con Spring Security + JWT.

El usuario inicia sesion enviando:

- `email`
- `password`

Si las credenciales son validas, el backend devuelve un token JWT.

### 11.2 Componentes principales

#### `SecurityFilterChain`

Define que:

- la aplicacion no usa sesiones
- `csrf` esta deshabilitado
- `/api/auth/login` es publico
- las rutas `/api/v1/**` quedan protegidas

#### `AuthService`

Usa `AuthenticationManager` para validar credenciales y luego genera el token.

#### `SecurityServiceImpl`

Implementa `UserDetailsService` y busca el usuario por email en la base de datos.

#### `CustomUserDetails`

Convierte `UsuarioEntity` en el formato que Spring Security necesita:

- email como username
- password cifrada
- authorities a partir del tipo de usuario

#### `JwtService`

Genera y valida el JWT usando HS256. El token contiene:

- subject: email
- roles
- fecha de emision
- fecha de expiracion

#### `JwtAuthenticationFilter`

Intercepta requests protegidos, valida el Bearer token y deja autenticado al usuario en el contexto de seguridad.

### 11.3 Flujo completo

1. El cliente hace `POST /api/auth/login`.
2. Spring Security valida email y password.
3. Si son correctos, se genera un JWT.
4. El cliente envia el token en `Authorization: Bearer <token>`.
5. El filtro JWT valida el token en requests posteriores.
6. Si el token es valido, el request puede entrar al controller.

### 11.4 Que endpoints estan protegidos

Publicos:

- `POST /api/auth/login`
- Swagger
- H2 Console

Protegidos:

- `/api/v1/usuarios/**`
- `/api/v1/solicitudes/**`
- `/api/v1/health`

### 11.5 Complemento con reglas de negocio

Ademas de la autenticacion HTTP, varios casos de uso vuelven a validar tipo y estado del usuario. Eso significa que la seguridad se aplica en dos niveles:

- seguridad tecnica en HTTP con JWT
- seguridad funcional en negocio con validaciones de rol

## 12. Endpoints Principales

### 12.1 Autenticacion

#### `POST /api/auth/login`

Autentica al usuario y devuelve un JWT.

### 12.2 Solicitudes

#### `POST /api/v1/solicitudes`

Crea una nueva solicitud academica.

#### `GET /api/v1/solicitudes`

Consulta solicitudes con filtros y paginacion.

#### `GET /api/v1/solicitudes/{codigo}`

Consulta el detalle de una solicitud.

#### `GET /api/v1/solicitudes/{codigo}/historial`

Consulta el historial de una solicitud.

#### `PUT /api/v1/solicitudes/{codigo}/responsable`

Asigna un funcionario responsable.

#### `POST /api/v1/solicitudes/{codigo}/clasificacion`

Clasifica una solicitud.

#### `POST /api/v1/solicitudes/{codigo}/prioridad`

Asigna prioridad.

#### `POST /api/v1/solicitudes/{codigo}/atencion/inicio`

Inicia la atencion.

#### `POST /api/v1/solicitudes/{codigo}/atencion/finalizacion`

Marca la solicitud como atendida.

#### `POST /api/v1/solicitudes/{codigo}/cierre`

Cierra la solicitud.

### 12.3 Usuarios

#### `POST /api/v1/usuarios`

Crea un usuario.

#### `GET /api/v1/usuarios`

Lista usuarios paginados.

#### `GET /api/v1/usuarios/{id}`

Consulta un usuario por id.

## 13. Cumplimiento de Requerimientos (RF)

### RF-01 Registro de solicitudes

- Capa: `infrastructure`, `application`, `domain`
- Clases: `SolicitudController`, `CrearSolicitudUseCase`, `Solicitud`

### RF-02 Clasificacion de solicitudes

- Capa: `infrastructure`, `application`, `domain`
- Clases: `SolicitudController`, `ClasificarSolicitudUseCase`, `Solicitud`

### RF-03 Priorizacion de solicitudes

- Capa: `infrastructure`, `application`, `domain`
- Clases: `SolicitudController`, `PriorizarSolicitudUseCase`, `PrioridadSolicitud`, `Solicitud`

### RF-04 Gestion del ciclo de vida

- Capa: `application`, `domain`
- Clases: `IniciarAtencionUseCase`, `MarcarAtendidaUseCase`, `Solicitud`

### RF-05 Asignacion de responsable

- Capa: `infrastructure`, `application`, `domain`
- Clases: `SolicitudController`, `AsignarResponsableUseCase`, `Solicitud`

### RF-06 Historial

- Capa: `domain`, `application`, `infrastructure`
- Clases: `Solicitud`, `ConsultarSolicitudPorCodigoUseCase`, `SolicitudPersistenceMapper`, `SolicitudController`

### RF-07 Consultas y paginacion

- Capa: `application`, `infrastructure`
- Clases: `ConsultarSolicitudesUseCase`, `ConsultarSolicitudesAvanzadasUseCase`, `SolicitudJpaDataRepository`, `PaginaResponse`

### RF-08 Cierre

- Capa: `application`, `domain`, `infrastructure`
- Clases: `CerrarSolicitudUseCase`, `CancelarSolicitudUseCase`, `Solicitud`, `SolicitudController`

### RF-13 Seguridad JWT

- Capa: `infrastructure` y apoyo en `application`/`domain`
- Clases:
  - `AuthController`
  - `AuthService`
  - `SecurityConfig`
  - `SecurityServiceImpl`
  - `JwtService`
  - `JwtAuthenticationFilter`
  - `CustomUserDetails`

Explicacion: el proyecto ya autentica usuarios con email y password, emite JWT, protege rutas HTTP y complementa esto con validaciones funcionales por rol dentro de casos de uso.

## 14. Flujo Completo de una Solicitud

Tomemos como ejemplo la creacion de una solicitud.

### Paso 1. Login

El cliente primero hace login en `POST /api/auth/login` con email y password.

### Paso 2. Obtencion del token

Si las credenciales son validas, el sistema responde con un JWT.

### Paso 3. Request protegido

El cliente envia:

```http
Authorization: Bearer <token>
```

junto con el `POST /api/v1/solicitudes`.

### Paso 4. El filtro JWT valida

`JwtAuthenticationFilter` valida el token y deja autenticado al usuario.

### Paso 5. El controller recibe y valida

`SolicitudController` recibe el request usando:

- `@RequestBody`
- `@Valid`

Si el body no cumple el contrato, responde `400`.

### Paso 6. El mapper convierte a dominio

`SolicitudRequestMapper` transforma el DTO en value objects del dominio.

### Paso 7. El UseCase orquesta

`CrearSolicitudUseCase`:

- busca al estudiante
- valida rol y estado
- crea la solicitud
- la guarda

### Paso 8. El dominio aplica reglas

La entidad `Solicitud`:

- nace con estado `REGISTRADA`
- registra historial
- protege invariantes

### Paso 9. Persistencia

`SolicitudJpaRepositoryImpl` convierte la entidad de dominio a `SolicitudEntity` y la guarda con JPA.

### Paso 10. Respuesta

El controller devuelve:

- `201 Created`
- header `Location`
- body `SolicitudDetalleResponse`

## 15. Decisiones de Diseno

### 15.1 Por que arquitectura hexagonal

Porque ayuda a:

- separar negocio de tecnologia
- mantener el dominio mas limpio
- reducir acoplamiento
- facilitar pruebas y evolucion del sistema

### 15.2 Por que DTOs

Porque permiten:

- no exponer entidades internas
- definir contratos claros de API
- validar entrada y salida
- desacoplar REST del dominio

### 15.3 Por que MapStruct y mappers

Porque el sistema necesita transformar datos entre REST, dominio y persistencia. Centralizar ese trabajo evita duplicacion y mejora legibilidad.

### 15.4 Por que JPA

Porque simplifica la persistencia:

- reduce codigo repetitivo
- ofrece consultas derivadas y personalizadas
- se integra bien con Spring Boot

### 15.5 Por que JWT

Porque es un enfoque adecuado para APIs REST:

- no requiere sesion en servidor
- facilita autenticacion stateless
- es facil de consumir desde frontend

## 16. Posibles Mejoras

- mover `jwt.secret` y usuarios por defecto a variables de entorno
- aplicar autorizacion HTTP mas detallada por rol
- eliminar dependencias de Spring en `application`
- sacar `Page` y `Pageable` de los puertos del dominio
- refactorizar algunos endpoints para hacerlos mas REST
- agregar pruebas de integracion de seguridad mas profundas
- preparar una configuracion separada para produccion
- incorporar refresh tokens si el alcance crece

## 17. Conclusion

El proyecto logra una base bastante completa para la Entrega 02.

Lo alcanzado incluye:

- dominio modelado con entidades y value objects
- casos de uso claros
- API REST funcional
- persistencia con JPA
- historial y paginacion
- autenticacion con JWT

Desde el punto de vista academico, el proyecto demuestra aprendizaje en:

- arquitectura hexagonal
- DDD
- casos de uso
- APIs REST
- persistencia con JPA
- seguridad con Spring Security y JWT

La principal mejora pendiente no es implementar seguridad desde cero, porque ya existe, sino refinarla y endurecerla para un escenario mas cercano a produccion.
