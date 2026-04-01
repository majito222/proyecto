package co.edu.uniquindio.proyecto.infrastructure.mapper;

import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.valueobject.*;
import co.edu.uniquindio.proyecto.infrastructure.dto.UsuarioResponse;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper para convertir entre entidades de dominio y DTOs de usuario.
 */
@Component
public class UsuarioMapper {

    /**
     * Convierte una entidad Usuario a UsuarioResponse.
     */
    public UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(
            usuario.getId().valor(),
            usuario.getNombre(),
            usuario.getEmail().valor(),
            usuario.getTipo().name(),
            usuario.estaActivo() ? "ACTIVO" : "INACTIVO"
        );
    }

    /**
     * Convierte una lista de usuarios a responses.
     */
    public List<UsuarioResponse> toResponseList(List<Usuario> usuarios) {
        return usuarios.stream()
            .map(this::toResponse)
            .toList();
    }
}
