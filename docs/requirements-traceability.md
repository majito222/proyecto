# Trazabilidad de Requisitos

Este documento relaciona requisitos funcionales y tecnicos con su implementacion actual.

## RF-01 Registro de solicitudes

- Endpoint: [SolicitudController](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/rest/SolicitudController.java)
- Caso de uso: [CrearSolicitudUseCase](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/CrearSolicitudUseCase.java)
- Dominio: [Solicitud](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/domain/entity/Solicitud.java)
- Mapper request: [SolicitudRequestMapper](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/mapper/SolicitudRequestMapper.java)
- Contrato API: [openapi.yaml](/C:/Users/santi/Documents/GitHub/proyecto/docs/api/openapi.yaml)

Nota: la solicitud se registra para el estudiante autenticado; el actor ya no viaja en el body.

## RF-02 Clasificacion de solicitudes

- Endpoint: [SolicitudController](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/rest/SolicitudController.java)
- Caso de uso: [ClasificarSolicitudUseCase](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/ClasificarSolicitudUseCase.java)
- Dominio: [Solicitud](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/domain/entity/Solicitud.java)

## RF-03 Priorizacion de solicitudes

- Endpoint: [SolicitudController](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/rest/SolicitudController.java)
- Caso de uso: [PriorizarSolicitudUseCase](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/PriorizarSolicitudUseCase.java)
- Dominio: [PrioridadSolicitud](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/domain/valueobject/PrioridadSolicitud.java), [Solicitud](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/domain/entity/Solicitud.java)

## RF-04 Gestion del ciclo de vida

- Inicio de atencion: [IniciarAtencionUseCase](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/IniciarAtencionUseCase.java)
- Finalizacion de atencion: [MarcarAtendidaUseCase](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/MarcarAtendidaUseCase.java)
- Cierre: [CerrarSolicitudUseCase](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/CerrarSolicitudUseCase.java)
- Cancelacion: [CancelarSolicitudUseCase](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/CancelarSolicitudUseCase.java)
- Reglas de estado: [Solicitud](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/domain/entity/Solicitud.java)

## RF-05 Asignacion de responsable

- Endpoint: [SolicitudController](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/rest/SolicitudController.java)
- Caso de uso: [AsignarResponsableUseCase](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/AsignarResponsableUseCase.java)
- Persistencia: [SolicitudJpaRepositoryImpl](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/jpa/SolicitudJpaRepositoryImpl.java)

## RF-06 Historial auditable

- Endpoint: [SolicitudController](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/rest/SolicitudController.java)
- Consulta enriquecida: [ConsultarSolicitudPorCodigoUseCase](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/ConsultarSolicitudPorCodigoUseCase.java)
- Modelo de historial: [Historial](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/domain/valueobject/Historial.java)
- Persistencia de historial: [SolicitudPersistenceMapper](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/jpa/SolicitudPersistenceMapper.java)

## RF-07 Consultas, filtros y paginacion

- Listado de solicitudes: [ConsultarSolicitudesUseCase](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/ConsultarSolicitudesUseCase.java)
- Consultas avanzadas: [ConsultarSolicitudesAvanzadasUseCase](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/ConsultarSolicitudesAvanzadasUseCase.java)
- Repositorio JPA: [SolicitudJpaDataRepository](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/jpa/SolicitudJpaDataRepository.java)
- Paginacion de usuarios: [ListarUsuariosUseCase](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/ListarUsuariosUseCase.java)
- DTO de pagina: [PaginaResponse](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/dto/response/PaginaResponse.java)

## RF-08 Usuarios

- Creacion de usuario: [UsuarioController](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/rest/UsuarioController.java), [CrearUsuarioUseCase](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/CrearUsuarioUseCase.java)
- Persistencia: [UsuarioJpaRepositoryImpl](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/jpa/UsuarioJpaRepositoryImpl.java)
- Password hashing: [PasswordHasher](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/application/security/PasswordHasher.java), [PasswordHasherAdapter](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/security/service/PasswordHasherAdapter.java)

## RF-13 Seguridad JWT y control de acceso

- Endpoint de autenticacion: [AuthController](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/rest/AuthController.java)
- Servicio de login: [AuthService](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/security/service/AuthService.java)
- Configuracion de seguridad: [SecurityConfig](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/security/config/SecurityConfig.java)
- Filtro JWT: [JwtAuthenticationFilter](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/security/jwt/JwtAuthenticationFilter.java)
- Servicio JWT: [JwtService](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/security/jwt/JwtService.java)
- User details: [CustomUserDetails](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/security/CustomUserDetails.java)
- Manejo `401`: [RestAuthenticationEntryPoint](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/security/config/RestAuthenticationEntryPoint.java)
- Manejo `403`: [RestAccessDeniedHandler](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/security/config/RestAccessDeniedHandler.java)
- Usuarios iniciales y normalizacion: [DefaultUserInitializer](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/security/config/DefaultUserInitializer.java)

## Soporte transversal

- Manejo global de errores: [GlobalExceptionHandler](/C:/Users/santi/Documents/GitHub/proyecto/src/main/java/co/edu/uniquindio/proyecto/infrastructure/exception/GlobalExceptionHandler.java)
- Contrato OpenAPI: [openapi.yaml](/C:/Users/santi/Documents/GitHub/proyecto/docs/api/openapi.yaml)
- Resumen de arquitectura: [architecture-summary.md](/C:/Users/santi/Documents/GitHub/proyecto/docs/architecture-summary.md)
- Resumen de seguridad: [security-summary.md](/C:/Users/santi/Documents/GitHub/proyecto/docs/security-summary.md)
