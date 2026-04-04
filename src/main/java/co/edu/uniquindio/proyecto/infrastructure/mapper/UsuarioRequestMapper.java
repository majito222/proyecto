package co.edu.uniquindio.proyecto.infrastructure.mapper;

import co.edu.uniquindio.proyecto.domain.valueobject.*;
import co.edu.uniquindio.proyecto.infrastructure.dto.CrearUsuarioRequest;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir DTOs de usuario a value objects del dominio.
 */
@Component
public class UsuarioRequestMapper {

    /**
     * Convierte CrearUsuarioRequest a value objects del dominio.
     */
    public UsuarioData toDomainData(CrearUsuarioRequest request) {
        return new UsuarioData(
            request.nombre(),
            new Email(request.email()),
            convertirTipo(request.tipo())
        );
    }

    /**
     * Convierte tipo de API a dominio.
     */
    private TipoUsuario convertirTipo(CrearUsuarioRequest.@NotNull(message = "El tipo de usuario es requerido") @org.jetbrains.annotations.UnknownNullability TipoUsuario tipo) {
        return TipoUsuario.valueOf(tipo.name());
    }

    /**
     * Record interno para agrupar datos de usuario.
     */
    public record UsuarioData(
        String nombre,
        Email email,
        TipoUsuario tipo
    ) {}
}
