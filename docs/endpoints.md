# Listado de Endpoints

## Salud

| Metodo | URL | Descripcion |
|---|---|---|
| GET | `/api/v1/health` | Verifica que la API este disponible. |

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
| GET | `/api/v1/solicitudes` | Lista solicitudes con filtros por estado/canal y paginacion. |
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
| GET | `/api/v1/solicitudes/gui11/estado-prioridad/{estado}` | Busca solicitudes por estado ordenadas por prioridad. |
| GET | `/api/v1/solicitudes/gui11/codigo` | Busca por coincidencia en codigo o descripcion. |
| GET | `/api/v1/solicitudes/gui11/activas` | Lista solicitudes activas paginadas. |
| GET | `/api/v1/solicitudes/gui11/pendientes-alta` | Lista solicitudes de alta prioridad sin asignar. |

## Observaciones REST

- No existen endpoints `PATCH`.
- Varias URLs usan acciones nominales como `/clasificacion`, `/prioridad`, `/cierre` o `/cancelacion`.
- Si el criterio de la entrega exige REST estricto, seria mejor modelar estos cambios como transiciones sobre el recurso o subrecursos mas neutros.
