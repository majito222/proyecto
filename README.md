# Sistema de Triage y Gestion de Solicitudes Academicas

API REST construida con Spring Boot para gestionar solicitudes academicas, usuarios y su ciclo de atencion. El sistema centraliza el registro, clasificacion, priorizacion, asignacion, seguimiento y cierre de solicitudes, manteniendo historial y reglas de negocio por rol.

## Arquitectura

El proyecto sigue una separacion por capas inspirada en arquitectura hexagonal:

```text
co.edu.uniquindio.proyecto
|- domain
|  |- entity
|  |- valueobject
|  |- exception
|  |- repository
|  `- service
|- application
|  |- dto
|  `- use cases
`- infrastructure
   |- rest
   |- mapper
   |- jpa
   |- security
   `- exception
```

Flujo principal:

```text
Controller -> UseCase -> Domain -> Repository (puerto) -> Adapter JPA -> Base de datos
```

Resumen por capa:

- `domain`: entidades, value objects, invariantes y contratos de repositorio.
- `application`: casos de uso que orquestan operaciones del sistema.
- `infrastructure`: API REST, persistencia JPA, seguridad JWT, mappers y manejo de errores.

Nota tecnica: la intencion hexagonal esta bien marcada, aunque `application` y algunos puertos de `domain` todavia usan tipos de Spring como `@Service`, `@Transactional`, `Page` y `Pageable`.

## Tecnologias

- Java 25
- Spring Boot 3.3
- Spring Web MVC
- Spring Data JPA
- Spring Security
- JWT con `jjwt` y firma HS256
- Bean Validation
- H2 Database
- MySQL Connector/J
- Springdoc OpenAPI
- MapStruct
- Lombok
- Gradle

## Ejecucion

Requisitos:

- JDK 25
- Gradle Wrapper

Levantar la aplicacion:

```bash
./gradlew bootRun
```

En Windows:

```powershell
.\gradlew.bat bootRun
```

Ejecutar pruebas:

```bash
./gradlew test
```

Utilidades:

- API: `http://localhost:8081`
- Swagger UI: `http://localhost:8081/swagger-ui.html`
- H2 Console: `http://localhost:8081/h2-console`

## Seguridad

La API implementa autenticacion stateless con Spring Security + JWT.

Flujo:

1. `POST /api/auth/login` recibe `email` y `password`.
2. `AuthenticationManager` valida credenciales contra usuarios guardados en BD.
3. `JwtService` genera un token firmado con HS256.
4. El cliente envia `Authorization: Bearer <token>`.
5. `JwtAuthenticationFilter` valida el token y carga el usuario autenticado.
6. Los endpoints sensibles toman el actor desde el usuario autenticado y no desde IDs enviados por el cliente.

Rutas publicas:

- `POST /api/auth/login`
- `/swagger-ui/**`
- `/v3/api-docs/**`
- `/h2-console/**`

Rutas protegidas:

- `/api/v1/**`

Roles principales:

- `ESTUDIANTE`: crear solicitudes.
- `FUNCIONARIO`: clasificar, priorizar e iniciar/finalizar atencion.
- `ADMINISTRADOR`: crear usuarios, consultar usuarios, asignar responsable y cerrar solicitudes.
- `FUNCIONARIO` o `ADMINISTRADOR`: cancelar solicitudes.

Credenciales de desarrollo configuradas:

- `security.admin@uq.edu.co` / `Admin123*`
- `security.funcionario@uq.edu.co` / `Func123*`

Recomendacion: para un entorno mas cercano a produccion, mover `jwt.secret` y credenciales de arranque a variables de entorno.

## Endpoints principales

Autenticacion:

- `POST /api/auth/login`

Usuarios:

- `POST /api/v1/usuarios`
- `GET /api/v1/usuarios`
- `GET /api/v1/usuarios/{id}`

Solicitudes:

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

Consultas adicionales:

- `GET /api/v1/solicitudes/gui11/estado-prioridad/{estado}`
- `GET /api/v1/solicitudes/gui11/codigo`
- `GET /api/v1/solicitudes/gui11/activas`
- `GET /api/v1/solicitudes/gui11/pendientes-alta`
- `GET /api/v1/health`

El detalle completo esta en [docs/endpoints.md](/C:/Users/santi/Documents/GitHub/proyecto/docs/endpoints.md).

## Ejemplos

Login:

```http
POST /api/auth/login
Content-Type: application/json
```

```json
{
  "email": "security.admin@uq.edu.co",
  "password": "Admin123*"
}
```

Respuesta:

```json
{
  "token": "<jwt>",
  "tipo": "Bearer",
  "expiresIn": 900000
}
```

Crear solicitud:

```http
POST /api/v1/solicitudes
Authorization: Bearer <jwt>
Content-Type: application/json
```

```json
{
  "canal": "CORREO",
  "tipo": "CONSULTA_ACADEMICA",
  "descripcion": "Solicito revision del estado de mi homologacion para el periodo actual."
}

Crear usuario:

```json
{
  "nombre": "Ana Perez",
  "email": "ana@uq.edu.co",
  "password": "AnaSegura123*",
  "tipo": "ESTUDIANTE"
}
```
```

## Documentacion adicional

- [Guia Entrega 02](/C:/Users/santi/Documents/GitHub/proyecto/docs/guia-entrega-02.md)
- [Resumen de arquitectura](/C:/Users/santi/Documents/GitHub/proyecto/docs/architecture-summary.md)
- [Listado de endpoints](/C:/Users/santi/Documents/GitHub/proyecto/docs/endpoints.md)
- [Trazabilidad de requisitos](/C:/Users/santi/Documents/GitHub/proyecto/docs/requirements-traceability.md)
- [Resumen de seguridad](/C:/Users/santi/Documents/GitHub/proyecto/docs/security-summary.md)
- [OpenAPI](/C:/Users/santi/Documents/GitHub/proyecto/docs/api/openapi.yaml)
