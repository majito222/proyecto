# Listado de Endpoints

## Acceso y proteccion

Publicos:

- `POST /api/auth/login`
- `/swagger-ui.html`
- `/swagger-ui/**`
- `/v3/api-docs/**`
- `/h2-console/**`

Protegidos por JWT:

- Todas las rutas bajo `/api/v1/**`

## Matriz de roles

| Rol | Operaciones |
|---|---|
| `ESTUDIANTE` | Crear solicitudes |
| `FUNCIONARIO` | Clasificar, priorizar, iniciar atencion, finalizar atencion |
| `ADMINISTRADOR` | Crear usuarios, listar usuarios, consultar usuarios, asignar responsable, cerrar solicitudes |
| `FUNCIONARIO` o `ADMINISTRADOR` | Cancelar solicitudes |

## Autenticacion

| Metodo | URL | Descripcion |
|---|---|---|
| POST | `/api/auth/login` | Autentica por email y password y devuelve un JWT Bearer. |

### Request

```json
{
  "email": "security.admin@uq.edu.co",
  "password": "Admin123*"
}
```

### Response

```json
{
  "token": "<jwt>",
  "tipo": "Bearer",
  "expiresIn": 900000
}
```

## Salud

| Metodo | URL | Descripcion |
|---|---|---|
| GET | `/api/v1/health` | Verifica disponibilidad de la API. Requiere autenticacion. |

## Usuarios

| Metodo | URL | Rol | Descripcion |
|---|---|---|---|
| POST | `/api/v1/usuarios` | `ADMINISTRADOR` | Crea un usuario con password cifrada en persistencia. |
| GET | `/api/v1/usuarios` | `ADMINISTRADOR` | Lista usuarios con paginacion y ordenamiento. |
| GET | `/api/v1/usuarios/{id}` | `ADMINISTRADOR` | Consulta detalle de un usuario. |

### Crear usuario

```json
{
  "nombre": "Ana Perez",
  "email": "ana@uq.edu.co",
  "password": "AnaSegura123*",
  "tipo": "ESTUDIANTE"
}
```

## Solicitudes

| Metodo | URL | Rol | Descripcion |
|---|---|---|---|
| POST | `/api/v1/solicitudes` | `ESTUDIANTE` | Registra una nueva solicitud para el estudiante autenticado. |
| GET | `/api/v1/solicitudes` | Autenticado | Lista solicitudes con filtros por estado y canal. |
| GET | `/api/v1/solicitudes/{codigo}` | Autenticado | Consulta el detalle de una solicitud. |
| GET | `/api/v1/solicitudes/{codigo}/historial` | Autenticado | Consulta historial paginado de una solicitud. |
| PUT | `/api/v1/solicitudes/{codigo}/responsable` | `ADMINISTRADOR` | Asigna un funcionario responsable. |
| POST | `/api/v1/solicitudes/{codigo}/clasificacion` | `FUNCIONARIO` | Clasifica la solicitud. |
| POST | `/api/v1/solicitudes/{codigo}/prioridad` | `FUNCIONARIO` | Asigna prioridad. |
| POST | `/api/v1/solicitudes/{codigo}/atencion/inicio` | `FUNCIONARIO` | Inicia la atencion. |
| POST | `/api/v1/solicitudes/{codigo}/atencion/finalizacion` | `FUNCIONARIO` | Marca la solicitud como atendida. |
| POST | `/api/v1/solicitudes/{codigo}/cierre` | `ADMINISTRADOR` | Cierra la solicitud. |
| POST | `/api/v1/solicitudes/{codigo}/cancelacion` | `FUNCIONARIO` o `ADMINISTRADOR` | Cancela la solicitud. |

### Crear solicitud

El `estudianteId` ya no se envia en el body. Se toma desde el usuario autenticado.

```json
{
  "canal": "CORREO",
  "tipo": "CONSULTA_ACADEMICA",
  "descripcion": "Solicito revision del estado de mi homologacion para el periodo actual."
}
```

### Asignar responsable

```json
{
  "funcionarioId": "700001"
}
```

### Clasificar solicitud

```json
{
  "tipo": "HOMOLOGACION"
}
```

### Priorizar solicitud

```json
{
  "nivel": "ALTA",
  "justificacion": "La solicitud impacta el registro del semestre actual."
}
```

### Iniciar o finalizar atencion

Los endpoints aceptan body vacio:

- `POST /api/v1/solicitudes/{codigo}/atencion/inicio`
- `POST /api/v1/solicitudes/{codigo}/atencion/finalizacion`

### Cerrar o cancelar

Request de cierre:

```json
{
  "observacion": "La solicitud fue resuelta y validada por administracion."
}
```

Request de cancelacion:

```json
{
  "observacion": "La solicitud fue cancelada por falta de soportes."
}
```

## Consultas avanzadas

| Metodo | URL | Descripcion |
|---|---|---|
| GET | `/api/v1/solicitudes/gui11/estado-prioridad/{estado}` | Solicitudes por estado ordenadas por prioridad descendente. |
| GET | `/api/v1/solicitudes/gui11/codigo` | Busqueda por codigo o descripcion con paginacion. |
| GET | `/api/v1/solicitudes/gui11/activas` | Solicitudes activas paginadas. |
| GET | `/api/v1/solicitudes/gui11/pendientes-alta` | Solicitudes de alta prioridad sin asignar. |

## Respuestas de error

Formato comun:

```json
{
  "codigo": "VALIDATION_ERROR",
  "mensaje": "descripcion: La descripcion debe tener entre 20 y 1000 caracteres",
  "status": 400,
  "error": "Bad Request",
  "ruta": "/api/v1/solicitudes",
  "timestamp": "2026-04-20T08:00:00",
  "detalles": []
}
```

Errores relevantes:

- `400`: validacion o argumento invalido
- `401`: autenticacion requerida o token invalido
- `403`: rol no permitido
- `404`: recurso no encontrado
- `409`: transicion de estado invalida

## Observaciones REST

- No hay endpoints `PATCH`.
- Varias rutas modelan acciones de negocio explicitas.
- Para esta entrega siguen siendo validas, aunque una version mas REST estricta podria modelar varias transiciones como actualizaciones parciales del recurso.
- Las rutas `gui11/*` conservan el nombre de la guia academica y no de una API publica ideal.
