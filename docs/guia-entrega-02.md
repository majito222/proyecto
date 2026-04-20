# Guia Explicativa del Proyecto - Entrega 02

## 1. Introduccion del Proyecto

### 1.1 Que hace el sistema

El proyecto implementa el backend de un **Sistema de Triage y Gestion de Solicitudes Academicas**.  
Su funcion principal es recibir, organizar, clasificar, priorizar y dar seguimiento a solicitudes realizadas por estudiantes a traves de distintos canales, como correo, CSU, SAC o canal telefonico.

### 1.2 Problema que resuelve

Antes de un sistema como este, las solicitudes academicas suelen manejarse de forma dispersa:

- llegan por diferentes medios
- no tienen una trazabilidad clara
- no existe un flujo uniforme de atencion
- se dificulta saber en que estado va cada caso
- no siempre hay responsables asignados

Eso genera retrasos, falta de control y poca visibilidad sobre el proceso.

### 1.3 Objetivo del backend

El objetivo del backend es:

- centralizar las solicitudes en una unica API
- aplicar reglas de negocio claras
- permitir la gestion del ciclo de vida de cada solicitud
- conservar historial auditable
- exponer endpoints REST para que otras capas, como frontend o clientes API, consuman el sistema
- conectar el dominio con una base de datos para almacenar la informacion

En otras palabras, este backend convierte un proceso academico desordenado en un flujo estructurado y controlado.

## 2. Arquitectura General

### 2.1 Que es la arquitectura hexagonal

La arquitectura hexagonal, tambien llamada **Ports and Adapters**, busca separar el nucleo del negocio de los detalles tecnicos.

La idea principal es que el dominio del sistema no dependa directamente de:

- Spring
- HTTP
- JPA
- la base de datos

En lugar de eso, el dominio define las reglas y las interfaces, mientras que la infraestructura se encarga de conectarlo con el mundo exterior.

### 2.2 Como esta organizada en este proyecto

El proyecto esta dividido en tres capas principales:

#### `domain/`

Es el nucleo del sistema. Aqui estan:

- entidades
- value objects
- excepciones de negocio
- interfaces de repositorio
- servicios de dominio

#### `application/`

Aqui viven los **casos de uso**.  
Esta capa coordina lo que debe pasar cuando llega una accion del usuario, por ejemplo:

- crear una solicitud
- clasificarla
- asignar responsable
- cerrarla

#### `infrastructure/`

Esta capa contiene todo lo tecnico:

- controladores REST
- mappers
- persistencia JPA
- manejo global de excepciones

### 2.3 Flujo general del sistema

El flujo principal del proyecto se puede explicar asi:

```text
Controller -> UseCase -> Domain -> Repository -> DB
```

Explicado paso a paso:

1. El cliente hace una peticion HTTP.
2. El `Controller` recibe el request.
3. El `UseCase` orquesta la operacion.
4. El `Domain` aplica reglas de negocio.
5. El `Repository` abstrae el acceso a datos.
6. El adapter JPA guarda o consulta en la base de datos.
7. La respuesta vuelve al cliente en forma de DTO.

## 3. Cambios Realizados en Esta Entrega

Respecto a una entrega centrada solo en dominio, en esta etapa el proyecto evoluciono hacia un backend funcional.

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

Esto permite representar acciones reales del sistema como operaciones de aplicacion.

### 3.2 API REST completa

Se crearon controladores REST para exponer el sistema por HTTP:

- `SolicitudController`
- `UsuarioController`
- `HealthController`

Con esto el proyecto deja de ser solo modelo interno y se vuelve consumible desde clientes externos.

### 3.3 DTOs con records

Se agregaron DTOs de entrada y salida usando `record`, por ejemplo:

- `CrearSolicitudRequest`
- `CrearUsuarioRequest`
- `SolicitudDetalleResponse`
- `UsuarioDetalleResponse`
- `PaginaResponse`

Esto ayuda a separar el contrato de la API del modelo de dominio.

### 3.4 Persistencia con JPA

Se agrego almacenamiento con:

- `SolicitudEntity`
- `UsuarioEntity`
- `SolicitudJpaDataRepository`
- `UsuarioJpaDataRepository`
- adapters e infraestructura de persistencia

Asi el sistema ahora puede guardar y recuperar informacion desde base de datos H2.

### 3.5 Seguridad con JWT

Aqui hay una aclaracion importante para la sustentacion:

**La guia de la entrega menciona seguridad con JWT, pero el codigo actual no la implementa todavia.**

Actualmente:

- no existe `SecurityFilterChain`
- no existe `/auth/login`
- no hay generacion ni validacion de JWT
- no hay `spring-security` en dependencias

Lo que si existe es validacion de roles a nivel de negocio en varios casos de uso, por ejemplo para restringir quien puede crear, clasificar, cerrar o cancelar solicitudes.

### 3.6 Justificacion de estos cambios

Estos cambios se hicieron porque un sistema empresarial no puede quedarse solo en el modelo de dominio.  
Para que sea util en un entorno real, necesita:

- operaciones concretas de aplicacion
- API REST para comunicarse con clientes
- persistencia para no perder datos
- validacion de entrada y salida
- seguridad para controlar acceso

## 4. Capa Domain (Negocio)

### 4.1 Que contiene

La capa `domain` contiene principalmente:

#### Entidades

- `Solicitud`
- `Usuario`

La entidad mas importante es `Solicitud`, porque representa el agregado principal del sistema.

#### Value Objects

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

### 4.2 Reglas de negocio que maneja

El dominio controla reglas como:

- toda solicitud inicia en estado `REGISTRADA`
- solo ciertas transiciones de estado son validas
- no se puede cerrar una solicitud si no esta `ATENDIDA`
- no se puede cancelar una solicitud ya cerrada o cancelada
- toda accion importante registra un evento en el historial
- los value objects validan sus propios datos

Ejemplos claros:

- `Solicitud.clasificarSolicitud(...)`
- `Solicitud.iniciarAtencion(...)`
- `Solicitud.marcarAtendida(...)`
- `Solicitud.cerrarSolicitud(...)`
- `Solicitud.cancelarSolicitud(...)`

### 4.3 Que no contiene

La capa domain no tiene:

- anotaciones `@Entity`
- anotaciones `@RestController`
- clases HTTP
- dependencias JPA en entidades

Eso es importante porque mantiene el negocio separado de la tecnologia.

## 5. Capa Application (Casos de Uso)

### 5.1 Que son los UseCases

Un caso de uso representa una accion del sistema desde el punto de vista funcional.  
No describe solo una clase tecnica, sino una operacion de negocio concreta.

Por ejemplo:

- crear solicitud
- consultar solicitud
- asignar responsable
- cerrar solicitud

### 5.2 Que hace cada UseCase principal

#### `CrearSolicitudUseCase`

- busca al estudiante en el repositorio
- valida que tenga rol valido
- crea la solicitud
- la guarda

#### `ClasificarSolicitudUseCase`

- consulta la solicitud
- consulta el usuario que la clasifica
- valida rol
- delega la clasificacion al dominio

#### `AsignarResponsableUseCase`

- carga la solicitud
- carga el funcionario
- valida que pueda atender solicitudes
- asigna el responsable

#### `PriorizarSolicitudUseCase`

- recibe la prioridad
- valida al responsable
- actualiza la solicitud

#### `IniciarAtencionUseCase` y `MarcarAtendidaUseCase`

- cambian el estado de la solicitud
- respetando el flujo del dominio

#### `CerrarSolicitudUseCase`

- valida que quien cierra sea administrador activo
- cierra la solicitud con observacion

#### `CancelarSolicitudUseCase`

- permite cancelar con reglas de rol y estado

### 5.3 Como se comunican con el dominio

Los casos de uso no implementan las reglas del negocio por si solos.  
Lo que hacen es:

- obtener entidades
- llamar metodos del dominio
- guardar resultados

Por eso se dice que **orquestan**, pero no reemplazan al dominio.

### 5.4 Como usan interfaces o puertos

La capa application trabaja con interfaces como:

- `SolicitudRepository`
- `UsuarioRepository`

Eso significa que los casos de uso no conocen directamente JPA ni SQL.  
Solo conocen contratos abstractos.

## 6. Capa Infrastructure - API REST

### 6.1 Que hacen los Controllers

Los controllers reciben peticiones HTTP y las convierten en llamadas a los casos de uso.

Ejemplos:

- `SolicitudController`
- `UsuarioController`

### 6.2 Como funcionan los endpoints

Un endpoint sigue este esquema:

1. recibe request HTTP
2. valida datos de entrada
3. llama al caso de uso correspondiente
4. convierte la respuesta a DTO
5. devuelve `ResponseEntity`

### 6.3 Uso de anotaciones

#### `@RestController`

Indica que la clase expone endpoints REST y que sus respuestas salen en formato JSON.

#### `@RequestMapping`

Define la ruta base del recurso.  
Por ejemplo:

- `/api/v1/solicitudes`
- `/api/v1/usuarios`

#### `@Valid`

Activa validaciones automaticas sobre los DTOs de entrada.

### 6.4 Por que se consideran controllers delgados

Se consideran delgados porque no contienen la logica principal del negocio.  
Su papel es:

- recibir datos
- delegar
- responder

La mayor parte del comportamiento se ejecuta en `application` y `domain`.

## 7. DTOs y Validaciones

### 7.1 Que son los DTOs

Los DTOs son objetos usados para transportar datos entre la API y el cliente.  
No representan directamente el dominio interno.

### 7.2 Diferencia entre Request y Response

#### Request

Representa lo que entra desde el cliente.

Ejemplos:

- `CrearSolicitudRequest`
- `ClasificarSolicitudRequest`
- `CerrarSolicitudRequest`

#### Response

Representa lo que devuelve la API.

Ejemplos:

- `SolicitudDetalleResponse`
- `SolicitudResumenResponse`
- `UsuarioDetalleResponse`
- `ErrorResponse`

### 7.3 Por que se usan records

Se usan `record` porque:

- reducen codigo repetido
- son inmutables por defecto
- son ideales para estructuras de datos simples
- expresan mejor que el DTO solo transporta informacion

### 7.4 Validaciones implementadas

Se usan anotaciones como:

- `@NotBlank`
- `@NotNull`
- `@Size`
- `@Email`

Ademas, el dominio tambien valida por dentro, por ejemplo:

- `Email` valida formato
- `DescripcionSolicitud` valida longitud minima
- `PrioridadSolicitud` obliga a tener justificacion

Esto da una doble proteccion:

- validacion externa en la API
- validacion interna en el dominio

## 8. Mappers

### 8.1 Que hacen los mappers

Los mappers convierten objetos entre distintas capas del sistema.

### 8.2 Conversion DTO -> dominio

Ejemplo:

- `SolicitudRequestMapper`
- `UsuarioRequestMapper`

Estos transforman datos de request en value objects del dominio.

Por ejemplo, `SolicitudRequestMapper` convierte:

- `estudianteId` -> `IdUsuario`
- `canal` -> `TipoCanal`
- `tipo` -> `TipoSolicitud`
- `descripcion` -> `DescripcionSolicitud`

### 8.3 Conversion dominio -> DTO

Ejemplo:

- `SolicitudMapper`
- `UsuarioMapper`

Estos convierten entidades de dominio en respuestas listas para la API.

### 8.4 Por que son importantes

Los mappers son importantes porque:

- evitan mezclar capas
- mantienen limpio el controller
- separan el contrato REST del modelo interno
- hacen mas facil cambiar la API sin romper el dominio

## 9. Persistencia (JPA)

### 9.1 Que es `@Entity`

`@Entity` indica que una clase representa una tabla de base de datos.

En este proyecto existen:

- `SolicitudEntity`
- `UsuarioEntity`

Estas clases no son iguales a las entidades de dominio.  
Esa separacion es intencional.

### 9.2 Que es `JpaRepository`

`JpaRepository` permite hacer operaciones CRUD y consultas sobre las entidades persistentes sin escribir toda la logica de acceso a datos manualmente.

Ejemplos:

- `SolicitudJpaDataRepository`
- `UsuarioJpaDataRepository`

### 9.3 Que es el adapter de persistencia

El adapter conecta el mundo del dominio con el mundo JPA.

Ejemplos:

- `SolicitudJpaRepositoryImpl`
- `UsuarioJpaRepositoryImpl`

Estos adapters:

- implementan las interfaces del dominio
- llaman al repositorio JPA real
- usan mappers de persistencia

### 9.4 Como se conecta con el dominio

La conexion ocurre asi:

1. el dominio define `SolicitudRepository`
2. infrastructure implementa ese contrato en `SolicitudJpaRepositoryImpl`
3. el adapter usa `SolicitudJpaDataRepository`
4. el mapper convierte `Solicitud <-> SolicitudEntity`

### 9.5 Base de datos usada

Actualmente se usa **H2 en memoria**, configurada en `application.properties`.

Esto es util para desarrollo y pruebas porque:

- arranca rapido
- no requiere instalacion externa
- facilita pruebas locales

## 10. Consultas Avanzadas (Guia 11)

### 10.1 Tipos de consultas implementadas

Se implementan dos estilos principales:

#### Query methods

Son metodos derivados del nombre, como:

- `findByEstado(...)`
- `findByCodigo(...)`
- `findBySolicitanteId(...)`

#### `@Query`

Tambien se usan consultas personalizadas JPQL o nativas, por ejemplo:

- buscar activas
- buscar por filtros dinamicos
- buscar con historial
- reporte agrupado por estado

### 10.2 Filtros

El proyecto permite filtrar solicitudes por:

- estado
- canal
- texto

Esto se ve especialmente en `ConsultarSolicitudesUseCase` y en `SolicitudJpaDataRepository`.

### 10.3 Paginacion con `Pageable`

La paginacion se usa para evitar respuestas demasiado grandes.

Ejemplos:

- listar solicitudes
- listar usuarios
- historial paginado
- consultas avanzadas de la guia 11

La API devuelve una estructura `PaginaResponse<T>` con:

- contenido
- pagina
- tamano
- total de elementos
- total de paginas

### 10.4 Ejemplos del proyecto

Ejemplos concretos:

- `/api/v1/solicitudes?estado=REGISTRADA&pagina=0&tamano=10`
- `/api/v1/solicitudes/gui11/activas`
- `/api/v1/solicitudes/gui11/estado-prioridad/{estado}`
- `/api/v1/solicitudes/gui11/codigo`

## 11. Seguridad (JWT)

### 11.1 Estado real del proyecto

Para esta seccion es muy importante ser honesto en la sustentacion:

**El proyecto actualmente no implementa autenticacion JWT real.**

### 11.2 Que deberia existir si estuviera implementada

En un proyecto con JWT deberian existir componentes como:

- `SecurityFilterChain`
- endpoint `/auth/login`
- servicio para autenticar usuario
- generacion de token
- validacion del token en cada request
- rutas protegidas por rol

### 11.3 Flujo esperado de JWT

El flujo correcto seria:

1. el usuario hace login
2. el sistema valida credenciales
3. se genera un token JWT
4. el cliente envia el token en `Authorization: Bearer ...`
5. las rutas protegidas validan ese token

### 11.4 Que hay realmente hoy

En este codigo:

- no hay dependencia de Spring Security
- no hay filtro JWT
- no hay endpoints protegidos por autenticacion HTTP

Lo que si hay es una forma de **seguridad de negocio**, porque algunos casos de uso validan el rol del usuario antes de ejecutar acciones.

Por ejemplo:

- solo un estudiante activo puede registrar solicitudes
- solo un funcionario activo puede atender o priorizar
- solo un administrador activo puede cerrar

### 11.5 Como explicarlo en la sustentacion

La forma correcta de presentarlo es:

> La arquitectura ya esta preparada para manejar reglas de autorizacion en el dominio y los casos de uso, pero la autenticacion HTTP con JWT aun no esta materializada en la version actual.

## 12. Endpoints Principales

### 12.1 Solicitudes

#### `POST /api/v1/solicitudes`

Crea una nueva solicitud academica.

#### `GET /api/v1/solicitudes`

Consulta solicitudes con filtros y paginacion.

#### `GET /api/v1/solicitudes/{codigo}`

Consulta el detalle de una solicitud.

#### `GET /api/v1/solicitudes/{codigo}/historial`

Consulta el historial paginado de la solicitud.

#### `PUT /api/v1/solicitudes/{codigo}/responsable`

Asigna un funcionario responsable.

#### `POST /api/v1/solicitudes/{codigo}/clasificacion`

Clasifica una solicitud y define su tipo.

#### `POST /api/v1/solicitudes/{codigo}/prioridad`

Asigna prioridad.

#### `POST /api/v1/solicitudes/{codigo}/atencion/inicio`

Inicia la atencion.

#### `POST /api/v1/solicitudes/{codigo}/atencion/finalizacion`

Marca la solicitud como atendida.

#### `POST /api/v1/solicitudes/{codigo}/cierre`

Cierra la solicitud.

#### `POST /api/v1/solicitudes/{codigo}/cancelacion`

Cancela la solicitud.

### 12.2 Usuarios

#### `POST /api/v1/usuarios`

Crea un usuario.

#### `GET /api/v1/usuarios`

Lista usuarios paginados.

#### `GET /api/v1/usuarios/{id}`

Consulta un usuario por id.

### 12.3 Health

#### `GET /api/v1/health`

Verifica que la API este disponible.

## 13. Cumplimiento de Requerimientos (RF)

### RF-01 Registro de solicitudes

- Capa: `infrastructure`, `application`, `domain`
- Clases:
  - `SolicitudController`
  - `CrearSolicitudUseCase`
  - `Solicitud`

Explicacion: el request llega al controller, el caso de uso valida el estudiante y el dominio crea la solicitud con estado inicial `REGISTRADA`.

### RF-02 Clasificacion de solicitudes

- Capa: `infrastructure`, `application`, `domain`
- Clases:
  - `SolicitudController`
  - `ClasificarSolicitudUseCase`
  - `Solicitud`

Explicacion: se actualiza el tipo y el estado de la solicitud siguiendo la regla de clasificacion.

### RF-03 Priorizacion de solicitudes

- Capa: `infrastructure`, `application`, `domain`
- Clases:
  - `SolicitudController`
  - `PriorizarSolicitudUseCase`
  - `PrioridadSolicitud`
  - `Solicitud`

Explicacion: el sistema permite asignar un nivel de prioridad con justificacion obligatoria.

### RF-04 Gestion del ciclo de vida

- Capa: `application`, `domain`
- Clases:
  - `IniciarAtencionUseCase`
  - `MarcarAtendidaUseCase`
  - `Solicitud`

Explicacion: el dominio controla las transiciones validas de estado.

### RF-05 Asignacion de responsable

- Capa: `infrastructure`, `application`, `domain`
- Clases:
  - `SolicitudController`
  - `AsignarResponsableUseCase`
  - `Solicitud`

Explicacion: una solicitud clasificada puede recibir un responsable para su atencion.

### RF-06 Historial

- Capa: `domain`, `application`, `infrastructure`
- Clases:
  - `Solicitud`
  - `ConsultarSolicitudPorCodigoUseCase`
  - `SolicitudPersistenceMapper`
  - `SolicitudController`

Explicacion: cada accion importante genera un evento y el historial puede consultarse desde la API.

### RF-07 Consultas y paginacion

- Capa: `application`, `infrastructure`
- Clases:
  - `ConsultarSolicitudesUseCase`
  - `ConsultarSolicitudesAvanzadasUseCase`
  - `SolicitudJpaDataRepository`
  - `PaginaResponse`

Explicacion: el sistema soporta filtros, busquedas avanzadas y paginacion para consultas mas eficientes.

### RF-08 Cierre de solicitudes

- Capa: `application`, `domain`, `infrastructure`
- Clases:
  - `CerrarSolicitudUseCase`
  - `CancelarSolicitudUseCase`
  - `Solicitud`
  - `SolicitudController`

Explicacion: solo ciertas solicitudes pueden cerrarse o cancelarse, segun estado y rol.

### RF-13 Seguridad

- Capa real actual: `domain` y `application`
- Clases:
  - `Usuario`
  - varios `UseCase` con validacion de rol

Explicacion: el sistema valida roles de negocio, pero **no implementa seguridad JWT real**.  
Si el requisito exige JWT completo, este punto esta parcial o pendiente en la version actual.

## 14. Flujo Completo de una Solicitud

Tomemos como ejemplo la creacion de una solicitud.

### Paso 1. Llega el request

El cliente envia un `POST /api/v1/solicitudes` con un JSON como:

```json
{
  "estudianteId": "123456",
  "canal": "CORREO",
  "tipo": "CONSULTA_ACADEMICA",
  "descripcion": "Solicito revision del estado de mi homologacion para el periodo actual."
}
```

### Paso 2. El controller recibe y valida

`SolicitudController` recibe el request usando:

- `@RequestBody`
- `@Valid`

Si faltan datos o no cumplen el formato esperado, la API responde con error `400`.

### Paso 3. El mapper convierte a objetos del dominio

`SolicitudRequestMapper` transforma el DTO en:

- `IdUsuario`
- `TipoCanal`
- `TipoSolicitud`
- `DescripcionSolicitud`

### Paso 4. El UseCase orquesta la operacion

`CrearSolicitudUseCase`:

- busca al estudiante
- valida que sea estudiante activo
- verifica si ya existe el codigo
- crea la solicitud
- la guarda

### Paso 5. El dominio aplica reglas

La entidad `Solicitud`:

- se crea con estado inicial `REGISTRADA`
- registra evento en historial
- protege sus invariantes

### Paso 6. Persistencia

El repositorio del dominio es implementado por `SolicitudJpaRepositoryImpl`, que:

- convierte la solicitud de dominio a `SolicitudEntity`
- la guarda con JPA
- la reconstruye si es necesario

### Paso 7. Respuesta al cliente

El controller devuelve:

- `201 Created`
- header `Location`
- body con `SolicitudDetalleResponse`

Asi el cliente recibe una respuesta limpia y lista para consumir.

## 15. Decisiones de Diseño

### 15.1 Por que se uso arquitectura hexagonal

Porque permite:

- separar negocio de tecnologia
- mantener el dominio limpio
- cambiar infraestructura con menos impacto
- hacer pruebas mas faciles

### 15.2 Por que DTOs

Porque ayudan a:

- no exponer entidades internas
- definir contratos claros de API
- validar entradas y salidas
- desacoplar la capa REST del dominio

### 15.3 Por que MapStruct y mappers

Porque el mapeo entre capas es inevitable y conviene centralizarlo para no duplicarlo en controllers o repositorios.

### 15.4 Por que JPA

Porque simplifica la persistencia:

- reduce codigo boilerplate
- permite consultas derivadas y personalizadas
- se integra facilmente con Spring Boot

### 15.5 Por que JWT

Conceptualmente, JWT es adecuado porque:

- es comun en APIs REST
- permite autenticacion sin sesion
- facilita proteccion por token

Pero hay que aclarar otra vez que en este proyecto aun no esta implementado.

## 16. Posibles Mejoras

Estas son mejoras recomendables para una siguiente iteracion:

- implementar autenticacion real con JWT
- agregar `Spring Security`
- proteger endpoints por rol
- hacer mas limpia la capa `application` eliminando dependencias directas de Spring
- sacar `Page` y `Pageable` de los puertos del dominio
- agregar pruebas de integracion mas completas
- mejorar algunos endpoints para hacerlos mas REST puros
- preparar configuracion para base de datos productiva
- agregar logs de auditoria mas robustos
- documentar ejemplos completos de request y response por endpoint

## 17. Conclusion

Este proyecto logra construir una base solida para un sistema de gestion de solicitudes academicas.

Lo mas importante que se alcanzo fue:

- modelar el negocio con entidades y value objects
- organizar el proyecto en capas
- implementar casos de uso reales
- exponer una API REST funcional
- persistir datos con JPA
- manejar historial, filtros y paginacion

Desde el punto de vista academico, el proyecto demuestra aprendizaje en:

- arquitectura hexagonal
- DDD
- diseño por capas
- validacion de datos
- exposicion de APIs REST
- persistencia con Spring Data JPA

La principal brecha frente a una Entrega 02 completamente cerrada es la seguridad JWT, que todavia no esta materializada.  
Aun asi, la estructura actual deja una base clara para seguir evolucionando el sistema.
