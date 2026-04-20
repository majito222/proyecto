# Listado de Endpoints

## Acceso y proteccion

Publicos:

- `POST /api/auth/login`
- `/swagger-ui/**`
- `/v3/api-docs/**`
- `/h2-console/**`

Protegidos por JWT:

- Todas las rutas bajo `/api/v1/**`

## Autenticacion

| Metodo | URL | Descripcion |
|---|---|---|
| POST | `/api/auth/login` | Autentica un usuario por email y password y devuelve un JWT. |

## Salud

| Metodo | URL | Descripcion |
|---|---|---|
| GET | `/api/v1/health` | Verifica que la API este disponible. Requiere autenticacion. |

## Usuarios

| Metodo | URL | Descripcion |
|---|---|---|
| POST | `/api/v1/usuarios` | Crea un usuario. |
| GET | `/api/v1/usuarios` | Lista usuarios con paginacion y ordenamiento. |
| GET | `/api/v1/usuarios/{id}` | Consulta el detalle de un usuario. |

## Solicitudes

| Metodo | URL | Descripcion |
|---|---|---|
| POST | `/api/v1/solicitudes` | Registra una nueva solicitud. |
| GET | `/api/v1/solicitudes` | Lista solicitudes con filtros por estado y canal, con paginacion. |
| GET | `/api/v1/solicitudes/{codigo}` | Consulta el detalle de una solicitud. |
| GET | `/api/v1/solicitudes/{codigo}/historial` | Consulta el historial paginado de una solicitud. |
| PUT | `/api/v1/solicitudes/{codigo}/responsable` | Asigna un responsable a la solicitud. |
| POST | `/api/v1/solicitudes/{codigo}/clasificacion` | Clasifica la solicitud. |
| POST | `/api/v1/solicitudes/{codigo}/prioridad` | Asigna prioridad a la solicitud. |
| POST | `/api/v1/solicitudes/{codigo}/atencion/inicio` | Inicia la atencion de la solicitud. |
| POST | `/api/v1/solicitudes/{codigo}/atencion/finalizacion` | Marca la solicitud como atendida. |
| POST | `/api/v1/solicitudes/{codigo}/cierre` | Cierra la solicitud. |
| POST | `/api/v1/solicitudes/{codigo}/cancelacion` | Cancela la solicitud. |

## Consultas avanzadas

| Metodo | URL | Descripcion |
|---|---|---|
| GET | `/api/v1/solicitudes/gui11/estado-prioridad/{estado}` | Busca solicitudes por estado y las ordena por prioridad. |
| GET | `/api/v1/solicitudes/gui11/codigo` | Busca solicitudes por coincidencia en codigo o descripcion. |
| GET | `/api/v1/solicitudes/gui11/activas` | Lista solicitudes activas paginadas. |
| GET | `/api/v1/solicitudes/gui11/pendientes-alta` | Lista solicitudes de prioridad alta sin asignar. |

## Observaciones REST

- No existen endpoints `PATCH`.
- Varias URLs modelan acciones explicitas como `/clasificacion`, `/prioridad`, `/cierre` y `/cancelacion`.
- La guia sigue siendo compatible con la entrega, pero una version mas REST estricta podria modelar estas transiciones como cambios de estado del recurso.
