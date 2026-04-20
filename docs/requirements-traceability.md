# Trazabilidad de Requisitos

Este documento relaciona cada requisito funcional importante con su implementacion en controladores, casos de uso, dominio, persistencia, seguridad y documentacion.

## RF-01 Registro de solicitudes

- Endpoint: [SolicitudController.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/rest/SolicitudController.java:82)
- Caso de uso: [CrearSolicitudUseCase.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/CrearSolicitudUseCase.java:18)
- Dominio: [Solicitud.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/domain/entity/Solicitud.java:34)
- Reglas: [reglas-negocio.md](/C:/Users/santi/Documents/GitHub/proyecto/docs/reglas-negocio.md:27)
- OpenAPI: [openapi.yaml](/C:/Users/santi/Documents/GitHub/proyecto/docs/api/openapi.yaml:1)

## RF-02 Clasificacion de solicitudes

- Endpoint: [SolicitudController.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/rest/SolicitudController.java:117)
- Caso de uso: [ClasificarSolicitudUseCase.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/ClasificarSolicitudUseCase.java:18)
- Dominio: [Solicitud.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/domain/entity/Solicitud.java:127)

## RF-03 Priorizacion de solicitudes

- Endpoint: [SolicitudController.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/rest/SolicitudController.java:132)
- Caso de uso: [PriorizarSolicitudUseCase.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/PriorizarSolicitudUseCase.java:18)
- Dominio: [Solicitud.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/domain/entity/Solicitud.java:139)
- Regla de prioridad: [reglas-negocio.md](/C:/Users/santi/Documents/GitHub/proyecto/docs/reglas-negocio.md:151)

## RF-04 Gestion del ciclo de vida

- Inicio de atencion: [SolicitudController.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/rest/SolicitudController.java:149), [IniciarAtencionUseCase.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/IniciarAtencionUseCase.java:17), [Solicitud.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/domain/entity/Solicitud.java:161)
- Marcado como atendida: [SolicitudController.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/rest/SolicitudController.java:163), [MarcarAtendidaUseCase.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/MarcarAtendidaUseCase.java:18), [Solicitud.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/domain/entity/Solicitud.java:172)
- Reglas: [reglas-negocio.md](/C:/Users/santi/Documents/GitHub/proyecto/docs/reglas-negocio.md:28)

## RF-05 Asignacion de responsable

- Endpoint: [SolicitudController.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/rest/SolicitudController.java:103)
- Caso de uso: [AsignarResponsableUseCase.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/AsignarResponsableUseCase.java:17)
- Dominio: [Solicitud.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/domain/entity/Solicitud.java:150)
- Persistencia: [SolicitudJpaRepositoryImpl.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/jpa/SolicitudJpaRepositoryImpl.java:21)

## RF-06 Historial auditable

- Endpoint: [SolicitudController.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/rest/SolicitudController.java:223)
- Consulta con historial: [ConsultarSolicitudPorCodigoUseCase.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/ConsultarSolicitudPorCodigoUseCase.java:15)
- Dominio: [Solicitud.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/domain/entity/Solicitud.java:209)
- Persistencia: [SolicitudPersistenceMapper.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/jpa/SolicitudPersistenceMapper.java:14)

## RF-07 Consultas y paginacion

- Listado filtrado: [SolicitudController.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/rest/SolicitudController.java:193), [ConsultarSolicitudesUseCase.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/ConsultarSolicitudesUseCase.java:15)
- Consultas avanzadas: [ConsultarSolicitudesAvanzadasUseCase.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/ConsultarSolicitudesAvanzadasUseCase.java:15), [SolicitudJpaDataRepository.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/jpa/SolicitudJpaDataRepository.java:17)
- Usuarios paginados: [UsuarioController.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/rest/UsuarioController.java:67), [ListarUsuariosUseCase.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/ListarUsuariosUseCase.java:11)

## RF-08 Cierre y cancelacion de solicitudes

- Cierre: [SolicitudController.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/rest/SolicitudController.java:177), [CerrarSolicitudUseCase.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/CerrarSolicitudUseCase.java:16), [Solicitud.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/domain/entity/Solicitud.java:183)
- Cancelacion: [SolicitudController.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/rest/SolicitudController.java:190), [CancelarSolicitudUseCase.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/CancelarSolicitudUseCase.java:16), [Solicitud.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/domain/entity/Solicitud.java:194)

## RF-13 Seguridad JWT y control de acceso

- Endpoint de autenticacion: [AuthController.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/rest/AuthController.java:17)
- DTOs de autenticacion: [LoginRequest.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/dto/request/LoginRequest.java:1), [LoginResponse.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/dto/response/LoginResponse.java:1)
- Servicio de login: [AuthService.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/security/service/AuthService.java:1)
- Configuracion HTTP: [SecurityConfig.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/security/config/SecurityConfig.java:1)
- Carga de usuarios: [SecurityServiceImpl.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/security/service/SecurityServiceImpl.java:1), [UsuarioJpaDataRepository.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/jpa/UsuarioJpaDataRepository.java:1)
- Modelo de seguridad: [CustomUserDetails.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/security/CustomUserDetails.java:1)
- JWT: [JwtService.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/security/jwt/JwtService.java:1), [JwtAuthenticationFilter.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/security/jwt/JwtAuthenticationFilter.java:1), [JwtConfig.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/security/config/JwtConfig.java:1)
- Passwords de arranque: [DefaultUserInitializer.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/security/config/DefaultUserInitializer.java:1)
- Reglas funcionales complementarias por rol: [Usuario.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/domain/entity/Usuario.java:41), [CrearSolicitudUseCase.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/CrearSolicitudUseCase.java:18), [CerrarSolicitudUseCase.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/CerrarSolicitudUseCase.java:16)

## Soporte transversal

- Manejo de errores HTTP: [GlobalExceptionHandler.java](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/exception/GlobalExceptionHandler.java:20)
- Contrato REST: [openapi.yaml](/C:/Users/santi/Documents/GitHub/proyecto/docs/api/openapi.yaml:1)
- Resumen de arquitectura: [architecture-summary.md](/C:/Users/santi/Documents/GitHub/proyecto/docs/architecture-summary.md)
