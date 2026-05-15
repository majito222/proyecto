# Resumen de Seguridad

## Estado actual

La aplicacion implementa seguridad con:

- `Spring Security`
- JWT firmado con `HS256`
- autenticacion stateless
- autorizacion por rol con `@PreAuthorize`
- validaciones funcionales complementarias en casos de uso y dominio

## Componentes principales

- `SecurityConfig`: define `SecurityFilterChain`, modo stateless, method security y rutas publicas
- `AuthController`: expone `POST /api/auth/login`
- `AuthService`: autentica y emite JWT
- `SecurityServiceImpl`: implementa `UserDetailsService` contra JPA
- `CustomUserDetails`: adapta `UsuarioEntity` al modelo de Spring Security
- `JwtService`: genera y valida tokens
- `JwtAuthenticationFilter`: procesa Bearer tokens
- `RestAuthenticationEntryPoint`: responde `401` en formato JSON comun
- `RestAccessDeniedHandler`: responde `403` en formato JSON comun
- `DefaultUserInitializer`: inserta usuarios de desarrollo y normaliza passwords antiguas
- `PasswordHasherAdapter`: aplica `BCryptPasswordEncoder` para nuevas altas

## Flujo de autenticacion

1. El cliente envia `email` y `password` a `POST /api/auth/login`.
2. `AuthenticationManager` valida credenciales.
3. `SecurityServiceImpl` consulta el usuario por email.
4. `CustomUserDetails` expone username, password cifrada y roles.
5. `JwtService` genera un JWT con `subject`, `roles`, `issuedAt` y `expiration`.
6. El cliente envia `Authorization: Bearer <token>` en requests posteriores.
7. `JwtAuthenticationFilter` valida el token y deja el usuario autenticado en el contexto.

## Autorizacion por rol

La aplicacion ya no solo exige autenticacion general. Tambien aplica restricciones por rol:

- `ESTUDIANTE`: `POST /api/v1/solicitudes`
- `FUNCIONARIO`: clasificar, priorizar, iniciar y finalizar atencion
- `ADMINISTRADOR`: crear usuarios, listar usuarios, consultar usuario, asignar responsable, cerrar solicitud
- `FUNCIONARIO` o `ADMINISTRADOR`: cancelar solicitud

Adicionalmente, los casos de uso vuelven a validar si el usuario esta activo y si su tipo permite la operacion.

## Proteccion contra suplantacion

Una mejora clave del estado actual es que los endpoints sensibles ya no confian en IDs enviados por el cliente para identificar al actor de la operacion.

Ahora:

- el usuario autenticado se toma del contexto de seguridad
- los DTOs ya no incluyen `estudianteId`, `funcionarioId` o `administradorId` cuando ese dato corresponde al actor autenticado
- se evita que un usuario ejecute acciones en nombre de otro solo cambiando el body

## Passwords

Se usa `BCryptPasswordEncoder`.

Escenarios cubiertos:

- usuarios de arranque con password cifrada
- creacion de usuarios desde API con password real y hash BCrypt
- normalizacion de registros antiguos sin hash al iniciar la aplicacion

## Errores de seguridad

La API devuelve JSON consistente en errores de seguridad:

- `401 Unauthorized`: `RestAuthenticationEntryPoint`
- `403 Forbidden`: `RestAccessDeniedHandler`

Esto mantiene el mismo formato de `ErrorResponse` usado por el resto de la API.

## Configuracion actual

Propiedades principales:

- `jwt.secret`
- `jwt.expiration`
- `app.default-users.*`

Rutas publicas:

- `POST /api/auth/login`
- Swagger
- H2 Console

Rutas protegidas:

- `/api/v1/**`

## Riesgos y limites actuales

La seguridad quedo mas consistente, pero aun hay mejoras posibles:

- mover `jwt.secret` y credenciales iniciales a variables de entorno
- agregar pruebas de integracion especificas para `401` y `403`
- refinar aun mas la autorizacion por ownership o por alcance funcional si el dominio lo requiere
- evaluar rotacion de secretos y expiraciones mas robustas para un entorno real
