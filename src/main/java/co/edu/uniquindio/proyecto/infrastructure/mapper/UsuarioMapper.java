package co.edu.uniquindio.proyecto.infrastructure.mapper;

import co.edu.uniquindio.proyecto.application.dto.response.UsuarioDetalleResponse;
import co.edu.uniquindio.proyecto.application.dto.response.UsuarioResumenResponse;
import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target = "id", source = "id.valor")
    @Mapping(target = "email", source = "email.valor")
    @Mapping(target = "tipo", source = "tipo")
    @Mapping(target = "estado", source = ".", qualifiedByName = "mapEstado")
    UsuarioDetalleResponse toDetalleResponse(Usuario usuario);

    @Mapping(target = "id", source = "id.valor")
    @Mapping(target = "tipo", source = "tipo")
    @Mapping(target = "estado", source = ".", qualifiedByName = "mapEstado")
    UsuarioResumenResponse toResumenResponse(Usuario usuario);

    List<UsuarioResumenResponse> toResumenResponseList(List<Usuario> usuarios);

    @Named("mapEstado")
    default String mapEstado(Usuario usuario) {
        return usuario.estaActivo() ? "ACTIVO" : "INACTIVO";
    }
}
