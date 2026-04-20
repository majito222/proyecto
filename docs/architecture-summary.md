# Resumen de Arquitectura

## Flujo principal

El flujo implementado en la API sigue este recorrido:

```text
Controller -> UseCase -> Domain -> Repository -> Adapter JPA -> Base de datos
```

Descripcion por capa:

- `infrastructure/rest`: recibe la peticion HTTP, valida el body con `@Valid`, delega a casos de uso y transforma la salida a DTOs.
- `application`: coordina la operacion de negocio. Los casos de uso consultan repositorios, invocan metodos del agregado y guardan cambios.
- `domain`: concentra entidades, value objects, excepciones y reglas de negocio. Aqui viven las invariantes del agregado `Solicitud` y las capacidades del `Usuario`.
- `infrastructure/jpa`: implementa los puertos de repositorio con `JpaRepository`, entidades persistentes separadas y mappers entre modelo de dominio y modelo relacional.

## Separacion de capas

Lo positivo:

- El dominio no tiene anotaciones de Spring ni JPA.
- Las entidades persistentes (`SolicitudEntity`, `UsuarioEntity`) estan separadas del dominio.
- Hay mappers para REST y para persistencia.
- Los controladores no acceden directo a `JpaRepository`.

Lo que rompe la pureza hexagonal:

- Los casos de uso en `application` usan `@Service` y `@Transactional`.
- Los repositorios de `domain` exponen `Page` y `Pageable` de Spring.
- `PaginaResponse` en `application` tambien depende de Spring Data.
- Parte del mapeo DTO -> dominio sigue dentro de los controladores para path/query params y algunos request bodies.

## Recomendacion estructural

Para una version mas alineada con hexagonal estricta:

1. Mover anotaciones Spring a adaptadores/configuracion.
2. Definir puertos de entrada y salida puros en `application`.
3. Reemplazar `Page` y `Pageable` por modelos propios de paginacion.
4. Centralizar conversiones REST en mappers o assemblers.
