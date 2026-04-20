# Trazabilidad de Requisitos

Este documento indica donde quedaron ubicados los requisitos funcionales y de apoyo dentro del proyecto: controladores, casos de uso, dominio, persistencia y documentacion.

## RF-01 Registro de solicitudes

- Endpoint: [SolicitudController.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/rest/SolicitudController.java:93)
- Caso de uso: [CrearSolicitudUseCase.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/CrearSolicitudUseCase.java:18)
- Dominio: [Solicitud.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/domain/entity/Solicitud.java:34)
- Reglas: [reglas-negocio.md](/C:/Users/santi/Documents/GitHub/proyecto/docs/reglas-negocio.md:27)
- OpenAPI: [openapi.yaml](/C:/Users/santi/Documents/GitHub/proyecto/docs/api/openapi.yaml:12)

## RF-02 Clasificacion de solicitudes

- Endpoint: [SolicitudController.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/rest/SolicitudController.java:130)
- Caso de uso: [ClasificarSolicitudUseCase.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/ClasificarSolicitudUseCase.java:18)
- Dominio: [Solicitud.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/domain/entity/Solicitud.java:127)
- OpenAPI: [openapi.yaml](/C:/Users/santi/Documents/GitHub/proyecto/docs/api/openapi.yaml:97)

## RF-03 Priorizacion de solicitudes

- Endpoint: [SolicitudController.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/rest/SolicitudController.java:145)
- Caso de uso: [PriorizarSolicitudUseCase.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/PriorizarSolicitudUseCase.java:18)
- Dominio: [Solicitud.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/domain/entity/Solicitud.java:139)
- Reglas: [reglas-negocio.md](/C:/Users/santi/Documents/GitHub/proyecto/docs/reglas-negocio.md:151)
- OpenAPI: [openapi.yaml](/C:/Users/santi/Documents/GitHub/proyecto/docs/api/openapi.yaml:121)

## RF-04 Gestion del ciclo de vida

- Inicio de atencion: [SolicitudController.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/rest/SolicitudController.java:163), [IniciarAtencionUseCase.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/IniciarAtencionUseCase.java:17), [Solicitud.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/domain/entity/Solicitud.java:161)
- Marcado como atendida: [SolicitudController.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/rest/SolicitudController.java:177), [MarcarAtendidaUseCase.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/MarcarAtendidaUseCase.java:18), [Solicitud.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/domain/entity/Solicitud.java:172)
- Reglas: [reglas-negocio.md](/C:/Users/santi/Documents/GitHub/proyecto/docs/reglas-negocio.md:28)

## RF-05 Asignacion de responsable

- Endpoint: [SolicitudController.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/rest/SolicitudController.java:116)
- Caso de uso: [AsignarResponsableUseCase.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/AsignarResponsableUseCase.java:17)
- Dominio: [Solicitud.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/domain/entity/Solicitud.java:150)
- Persistencia: [SolicitudJpaRepositoryImpl.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/jpa/SolicitudJpaRepositoryImpl.java:21)

## RF-06 Historial auditable

- Endpoint: [SolicitudController.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/rest/SolicitudController.java:249)
- Consulta con historial: [ConsultarSolicitudPorCodigoUseCase.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/ConsultarSolicitudPorCodigoUseCase.java:15)
- Dominio: [Solicitud.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/domain/entity/Solicitud.java:209)
- Persistencia: [SolicitudPersistenceMapper.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/jpa/SolicitudPersistenceMapper.java:14)
- Reglas: [reglas-negocio.md](/C:/Users/santi/Documents/GitHub/proyecto/docs/reglas-negocio.md:113)

## RF-07 Consultas y listado

- Listado filtrado: [SolicitudController.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/rest/SolicitudController.java:221), [ConsultarSolicitudesUseCase.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/ConsultarSolicitudesUseCase.java:15)
- Consultas avanzadas: [ConsultarSolicitudesAvanzadasUseCase.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/ConsultarSolicitudesAvanzadasUseCase.java:15), [SolicitudJpaDataRepository.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/jpa/SolicitudJpaDataRepository.java:17)
- Usuarios paginados: [UsuarioController.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/rest/UsuarioController.java:77), [ListarUsuariosUseCase.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/ListarUsuariosUseCase.java:11)

## RF-08 Cierre y cancelacion de solicitudes

- Cierre: [SolicitudController.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/rest/SolicitudController.java:191), [CerrarSolicitudUseCase.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/CerrarSolicitudUseCase.java:16), [Solicitud.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/domain/entity/Solicitud.java:183)
- Cancelacion: [SolicitudController.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/rest/SolicitudController.java:206), [CancelarSolicitudUseCase.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/CancelarSolicitudUseCase.java:16), [Solicitud.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/domain/entity/Solicitud.java:194)
- Reglas: [reglas-negocio.md](/C:/Users/santi/Documents/GitHub/proyecto/docs/reglas-negocio.md:75)

## RF-13 Roles y usuarios validos

- Reglas de rol en dominio: [Usuario.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/domain/entity/Usuario.java:41)
- Validacion de rol en casos de uso:
  [CrearSolicitudUseCase.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/CrearSolicitudUseCase.java:18),
  [ClasificarSolicitudUseCase.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/ClasificarSolicitudUseCase.java:18),
  [PriorizarSolicitudUseCase.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/PriorizarSolicitudUseCase.java:18),
  [IniciarAtencionUseCase.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/IniciarAtencionUseCase.java:17),
  [MarcarAtendidaUseCase.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/MarcarAtendidaUseCase.java:18),
  [CerrarSolicitudUseCase.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/CerrarSolicitudUseCase.java:16),
  [CancelarSolicitudUseCase.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/CancelarSolicitudUseCase.java:16)
- Reglas documentadas: [reglas-negocio.md](/C:/Users/santi/Documents/GitHub/proyecto/docs/reglas-negocio.md:170)

## Soporte transversal

- Manejo de errores HTTP: [GlobalExceptionHandler.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/exception/GlobalExceptionHandler.java:20)
- Contrato REST: [openapi.yaml](/C:/Users/santi/Documents/GitHub/proyecto/docs/api/openapi.yaml:1)
- Resumen de arquitectura: [architecture-summary.md](/C:/Users/santi/Documents/GitHub/proyecto/docs/architecture-summary.md)
