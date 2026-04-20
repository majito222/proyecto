# Sistema de Triage y Gestion de Solicitudes Academicas

API REST construida con Spring Boot para gestionar usuarios y solicitudes academicas. El proyecto adopta una separacion por capas inspirada en arquitectura hexagonal y DDD: dominio, casos de uso, adaptadores REST y persistencia JPA.

## Arquitectura

Estructura principal:

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
   `- exception
```

Flujo principal:

```text
Controller -> UseCase -> Repository (puerto) -> Adapter JPA -> Base de datos
                     -> Domain
```

Separacion actual:

- `domain`: entidades, value objects, reglas e interfaces de repositorio.
- `application`: orquestacion de casos de uso.
- `infrastructure`: controladores REST, mappers, manejo de excepciones y persistencia JPA.

Nota tecnica: el proyecto sigue la intencion hexagonal, pero hoy `application` y parte de `domain` todavia dependen de tipos de Spring (`@Service`, `@Transactional`, `Page`, `Pageable`).

## Tecnologias

- Java 25
- Spring Boot
- Spring Web MVC
- Spring Data JPA
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

Comandos:

```bash
./gradlew bootRun
```

En Windows:

```powershell
.\gradlew.bat bootRun
```

Pruebas:

```bash
./gradlew test
```

Puertos y utilidades:

- API: `http://localhost:8081`
- Swagger UI: `http://localhost:8081/swagger-ui.html`
- H2 Console: `http://localhost:8081/h2-console`

## Endpoints principales

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

## Seguridad

Estado actual:

- No hay `spring-security` en dependencias.
- No existe `SecurityFilterChain`.
- No existe `/auth/login`.
- No hay JWT ni cifrado BCrypt implementado.

Eso significa que la API actual funciona sin autenticacion ni autorizacion a nivel HTTP. El resumen tecnico esta en [docs/security-summary.md](/C:/Users/santi/Documents/GitHub/proyecto/docs/security-summary.md).

## Ejemplo de request/response

Crear usuario:

```http
POST /api/v1/usuarios
Content-Type: application/json
```

```json
{
  "nombre": "Ana Perez",
  "email": "ana.perez@uqvirtual.edu.co",
  "tipo": "ESTUDIANTE"
}
```

Respuesta:

```json
{
  "id": "123456",
  "nombre": "Ana Perez",
  "email": "ana.perez@uqvirtual.edu.co",
  "tipo": "ESTUDIANTE",
  "estado": "ACTIVO"
}
```

Crear solicitud:

```http
POST /api/v1/solicitudes
Content-Type: application/json
```

```json
{
  "estudianteId": "123456",
  "canal": "CORREO",
  "tipo": "CONSULTA_ACADEMICA",
  "descripcion": "Solicito revision del estado de mi homologacion para el periodo actual."
}
```

## Documentacion adicional

- [Resumen de arquitectura](/C:/Users/santi/Documents/GitHub/proyecto/docs/architecture-summary.md)
- [Listado de endpoints](/C:/Users/santi/Documents/GitHub/proyecto/docs/endpoints.md)
- [Trazabilidad de requisitos](/C:/Users/santi/Documents/GitHub/proyecto/docs/requirements-traceability.md)
- [Resumen de seguridad](/C:/Users/santi/Documents/GitHub/proyecto/docs/security-summary.md)
