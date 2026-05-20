package co.edu.uniquindio.proyecto.infrastructure.jpa;

import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.valueobject.Email;
import co.edu.uniquindio.proyecto.domain.valueobject.EstadoUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.IdUsuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioPersistenceMapper {

    public UsuarioEntity toEntity(Usuario usuario) {
        UsuarioEntity entity = new UsuarioEntity();
        entity.setId(usuario.getId().valor());
        entity.setNombre(usuario.getNombre());
        entity.setEmail(usuario.getEmail().valor());
        entity.setPassword(usuario.getPasswordHash());
        entity.setTipo(usuario.getTipo());
        entity.setEstado(usuario.getEstado());
        return entity;
    }

    public Usuario toDomain(UsuarioEntity entity) {
        Usuario usuario = new Usuario(
                new IdUsuario(entity.getId()),
                entity.getNombre(),
                new Email(entity.getEmail()),
                entity.getPassword(),
                entity.getTipo()
        );

        if (entity.getEstado() == EstadoUsuario.INACTIVO) {
            usuario.desactivarUsuario();
        }

        return usuario;
    }
}
