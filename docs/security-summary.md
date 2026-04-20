# Resumen de Seguridad

## Estado actual

La aplicacion no tiene seguridad HTTP implementada.

No se encontro:

- dependencia `spring-boot-starter-security`
- clase `SecurityFilterChain`
- filtro JWT
- endpoint `/auth/login`
- generacion o validacion de JWT
- `PasswordEncoder` o `BCryptPasswordEncoder`

## Consecuencia

Todas las rutas REST quedan efectivamente publicas mientras la aplicacion este levantada.

## Diferencia con OpenAPI

El archivo `docs/api/openapi.yaml` declara un esquema `bearerAuth` global, pero el codigo real no lo implementa. Hoy esa documentacion no coincide con el comportamiento de la API.

## Recomendacion minima

1. Agregar `spring-boot-starter-security`.
2. Definir `SecurityFilterChain`.
3. Crear `/auth/login` y servicio de autenticacion.
4. Cifrar contrasenas con BCrypt.
5. Firmar JWT HS256 y validar expiracion.
6. Proteger rutas por rol (`ESTUDIANTE`, `FUNCIONARIO`, `ADMINISTRADOR`).
