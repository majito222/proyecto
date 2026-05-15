# Resumen de Arquitectura

## Vision general

El proyecto implementa una API REST para gestionar solicitudes academicas con una estructura inspirada en arquitectura hexagonal.

La organizacion principal es:

```text
domain
application
infrastructure
```

## Flujo principal

El recorrido real de una operacion protegida es el siguiente:

```text
HTTP Request
 -> JwtAuthenticationFilter
 -> SecurityContext
 -> Controller
 -> UseCase
 -> Domain
 -> Repository (puerto)
 -> Adapter JPA
 -> Base de datos
```

Lectura del flujo:

1. El cliente envia un request HTTP.
2. Si la ruta es protegida, `JwtAuthenticationFilter` valida el Bearer token.
3. Spring Security deja autenticado al usuario en el `SecurityContext`.
4. El controller valida DTOs, toma al actor autenticado y delega al caso de uso.
5. El caso de uso orquesta la operacion y consulta puertos del dominio.
6. El dominio aplica invariantes y reglas funcionales.
7. El adapter JPA implementa el puerto y persiste con Spring Data JPA.
8. La respuesta vuelve al cliente en DTOs.

## Capas

### `domain`

Contiene el nucleo del negocio:

- entidades: `Solicitud`, `Usuario`
- value objects: `CodigoSolicitud`, `IdUsuario`, `Email`, `PrioridadSolicitud`, `EstadoSolicitud`, `TipoSolicitud`, `TipoCanal`, `Historial`
- excepciones de dominio
- contratos de repositorio: `SolicitudRepository`, `UsuarioRepository`

Responsabilidades principales:

- ciclo de vida de la solicitud
- historial auditable
- validaciones funcionales por tipo y estado de usuario
- invariantes del negocio

### `application`

Contiene casos de uso que coordinan operaciones del sistema:

- crear solicitud
- clasificar
- priorizar
- asignar responsable
- iniciar o finalizar atencion
- cerrar o cancelar
- listar o consultar usuarios y solicitudes

La capa usa puertos del dominio, no repositorios JPA concretos.

### `infrastructure`

Resuelve detalles tecnicos:

- `rest`: controladores HTTP
- `mapper`: DTO <-> dominio
- `jpa`: entidades persistentes, `JpaRepository` y adapters
- `security`: Spring Security, JWT, login y roles
- `exception`: manejo global de errores HTTP

## Seguridad dentro de la arquitectura

La seguridad actual ya no depende de IDs enviados por el cliente para saber quien ejecuta una accion.

Decisiones clave:

- el actor se toma del usuario autenticado
- los controllers usan `@PreAuthorize` para restricciones por rol
- los casos de uso siguen validando reglas funcionales con el dominio
- `401` y `403` responden JSON consistente

Roles aplicados:

- `ESTUDIANTE`: crear solicitudes
- `FUNCIONARIO`: clasificar, priorizar, iniciar y finalizar atencion
- `ADMINISTRADOR`: crear usuarios, consultar usuarios, asignar responsable y cerrar solicitudes
- `FUNCIONARIO` o `ADMINISTRADOR`: cancelar solicitudes

## Mapeos entre capas

Se usan mappers explicitos para no mezclar modelos:

- REST -> dominio: `SolicitudRequestMapper`, `UsuarioRequestMapper`
- dominio -> REST: `SolicitudMapper`, `UsuarioMapper`
- persistencia -> dominio: `SolicitudPersistenceMapper`, `UsuarioPersistenceMapper`

Esto evita exponer entidades JPA o value objects directamente en la API.

## Persistencia

La persistencia usa:

- `Spring Data JPA`
- `H2` para desarrollo
- adapters concretos: `SolicitudJpaRepositoryImpl`, `UsuarioJpaRepositoryImpl`

Capacidades implementadas:

- CRUD basico
- filtros
- paginacion
- consultas derivadas
- consultas con `@Query`

## Fortalezas actuales

- separacion clara entre dominio y entidades JPA
- casos de uso bien identificables
- mapeo explicito entre capas
- autenticacion JWT stateless
- autorizacion por rol en HTTP y validaciones complementarias en negocio
- manejo global de errores

## Limites actuales

Todavia hay puntos que no son una hexagonal estricta:

- `application` usa `@Service` y `@Transactional`
- los puertos de dominio exponen `Page` y `Pageable` de Spring
- parte de la logica de paginacion sigue expresada con tipos Spring en capas internas
- varias rutas de transicion siguen un estilo RPC (`/clasificacion`, `/prioridad`, `/cierre`)

## Archivos clave

- [SolicitudController](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/rest/SolicitudController.java)
- [UsuarioController](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/rest/UsuarioController.java)
- [SecurityConfig](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/security/config/SecurityConfig.java)
- [CrearSolicitudUseCase](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/CrearSolicitudUseCase.java)
- [CrearUsuarioUseCase](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/CrearUsuarioUseCase.java)
- [Solicitud](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/domain/entity/Solicitud.java)
- [Usuario](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/domain/entity/Usuario.java)
