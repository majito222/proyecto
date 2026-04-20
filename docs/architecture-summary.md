# Resumen de Arquitectura

## Flujo principal

El recorrido principal del sistema es el siguiente:

```text
Controller -> UseCase -> Domain -> Repository -> Adapter JPA -> Base de datos
```

Lectura del flujo:

1. El cliente consume la API REST.
2. El controller recibe el request y valida DTOs con `@Valid`.
3. El caso de uso coordina la operacion.
4. El dominio aplica reglas e invariantes.
5. El repositorio del dominio define el contrato.
6. El adapter JPA implementa ese contrato y persiste en H2.
7. La respuesta vuelve al cliente en DTOs.

## Separacion por capas

### `domain`

Contiene el nucleo del negocio:

- `Solicitud` y `Usuario`
- value objects como `CodigoSolicitud`, `IdUsuario`, `Email` y `PrioridadSolicitud`
- excepciones de dominio
- interfaces `SolicitudRepository` y `UsuarioRepository`

Aqui viven reglas como transiciones validas de estado, historial auditable y permisos funcionales por tipo de usuario.

### `application`

Contiene los casos de uso del sistema:

- crear, consultar, clasificar, priorizar y cerrar solicitudes
- listar usuarios
- consultar historiales

Esta capa usa los puertos del dominio para no depender directamente de JPA.

### `infrastructure`

Resuelve los detalles tecnicos:

- `rest`: controllers HTTP
- `mapper`: conversion DTO <-> dominio
- `jpa`: entidades persistentes, repositorios Spring Data y adapters
- `security`: Spring Security, login y JWT
- `exception`: manejo global de errores HTTP

## Seguridad dentro del flujo

Con la version actual, el flujo protegido queda asi:

```text
Request -> JwtAuthenticationFilter -> SecurityContext -> Controller -> UseCase -> Domain
```

Eso significa que antes de entrar al controller:

- se revisa el header `Authorization`
- se valida el JWT
- se carga el usuario por email
- se registra la autenticacion en el contexto de Spring Security

Luego, los casos de uso vuelven a aplicar validaciones funcionales por rol del dominio.

## Fortalezas actuales

- Dominio separado de JPA y HTTP en entidades.
- Entidades persistentes separadas del modelo de negocio.
- Controllers relativamente delgados.
- Mappers dedicados para REST y persistencia.
- Seguridad JWT integrada sin sesiones en servidor.

## Puntos a mejorar

- `application` sigue usando `@Service` y `@Transactional`.
- Los puertos de dominio exponen `Page` y `Pageable` de Spring.
- Parte del mapeo de path/query params sigue en controllers.
- La autorizacion HTTP todavia es general por autenticacion; las reglas finas de rol siguen principalmente en casos de uso.
