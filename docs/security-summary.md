# Resumen de Seguridad

## Estado actual

La aplicacion ya implementa seguridad HTTP con Spring Security y JWT.

Componentes principales:

- `SecurityConfig`: define `SecurityFilterChain`, modo stateless y rutas publicas.
- `AuthController`: expone `POST /api/auth/login`.
- `AuthService`: autentica con `AuthenticationManager` y genera token.
- `SecurityServiceImpl`: implementa `UserDetailsService` contra JPA.
- `CustomUserDetails`: adapta `UsuarioEntity` al modelo de Spring Security.
- `JwtService`: genera y valida JWT HS256.
- `JwtAuthenticationFilter`: procesa el Bearer token en cada request.
- `DefaultUserInitializer`: crea usuarios iniciales y normaliza passwords a BCrypt.

## Flujo de autenticacion

1. El cliente envia `email` y `password` a `POST /api/auth/login`.
2. `AuthenticationManager` valida credenciales usando `UserDetailsService`.
3. `SecurityServiceImpl` busca el usuario en `UsuarioJpaDataRepository.findByEmail(...)`.
4. `CustomUserDetails` expone email, password cifrada y authorities.
5. `JwtService` genera un token con:
   - `subject`: email
   - `roles`: authorities del usuario
   - `issuedAt`
   - `expiration`
6. El cliente usa `Authorization: Bearer <token>` en rutas protegidas.
7. `JwtAuthenticationFilter` valida firma, expiracion y usuario.

## Rutas publicas

- `POST /api/auth/login`
- `/swagger-ui.html`
- `/swagger-ui/**`
- `/v3/api-docs/**`
- `/h2-console/**`

## Rutas protegidas

- Todas las demas rutas, incluyendo `/api/v1/usuarios`, `/api/v1/solicitudes` y `/api/v1/health`

## Passwords

- Se usa `BCryptPasswordEncoder`.
- Los usuarios de arranque se insertan con password cifrada.
- Si existen registros antiguos con password sin hash, `DefaultUserInitializer` los normaliza al arrancar la aplicacion.

## Configuracion JWT

Propiedades actuales:

- `jwt.secret`
- `jwt.expiration`

La firma usa HMAC SHA-256 a traves de `jjwt`.

## Alcance y limites actuales

Lo que ya cumple:

- autenticacion stateless
- validacion de JWT por filtro
- passwords cifradas con BCrypt
- carga de usuarios desde JPA
- claims de roles en el token

Lo que aun conviene mejorar:

- mover `jwt.secret` fuera de `application.properties`
- agregar autorizacion HTTP mas fina por rol en `SecurityConfig` o `@PreAuthorize`
- migrar a `spring-boot-starter-oauth2-resource-server` si se quiere una integracion mas estandar con `JwtDecoder/JwtEncoder`
- agregar manejo de refresh tokens si el alcance del proyecto lo requiere
