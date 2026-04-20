package co.edu.uniquindio.proyecto.infrastructure.mapper;

import co.edu.uniquindio.proyecto.application.dto.request.CrearUsuarioRequest;
import co.edu.uniquindio.proyecto.domain.valueobject.*;
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
            request.password(),
            convertirTipo(request.tipo())
        );
    }

    /**
     * Convierte tipo de API a dominio.
     */
    private TipoUsuario convertirTipo(CrearUsuarioRequest.TipoUsuario tipo) {
        if (tipo == null) {
            return null; // O lanza una excepción según tu lógica
        }
        return TipoUsuario.valueOf(tipo.name());
    }

    /**
     * Record interno para agrupar datos de usuario.
     */
    public record UsuarioData(
        String nombre,
        Email email,
        String password,
        TipoUsuario tipo
    ) {}
}
