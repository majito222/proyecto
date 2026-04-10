package co.edu.uniquindio.proyecto.infrastructure.mapper;

import co.edu.uniquindio.proyecto.application.dto.response.UsuarioDetalleResponse;
import co.edu.uniquindio.proyecto.application.dto.response.UsuarioResumenResponse;
import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper para convertir entre entidades de dominio y DTOs de usuario.
 */
@Component
public class UsuarioMapper {

    public UsuarioDetalleResponse toDetalleResponse(Usuario usuario) {
        return new UsuarioDetalleResponse(
            usuario.getId().valor(),
            usuario.getNombre(),
            usuario.getEmail().valor(),
            usuario.getTipo().name(),
            usuario.estaActivo() ? "ACTIVO" : "INACTIVO"
        );
    }

    public UsuarioResumenResponse toResumenResponse(Usuario usuario) {
        return new UsuarioResumenResponse(
            usuario.getId().valor(),
            usuario.getNombre(),
            usuario.getTipo().name(),
            usuario.estaActivo() ? "ACTIVO" : "INACTIVO"
        );
    }

    public List<UsuarioResumenResponse> toResumenResponseList(List<Usuario> usuarios) {
        return usuarios.stream()
            .map(this::toResumenResponse)
            .toList();
    }
}
