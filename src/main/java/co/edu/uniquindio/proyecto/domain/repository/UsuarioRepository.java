package co.edu.uniquindio.proyecto.domain.repository;

import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.valueobject.Email;
import co.edu.uniquindio.proyecto.domain.valueobject.IdUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.TipoUsuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {

    Usuario guardar(Usuario usuario);

    Usuario buscarPorId(IdUsuario id);

    Optional<Usuario> buscarPorEmail(Email email);

    List<Usuario> buscarPorTipo(TipoUsuario tipo);

    List<Usuario> listarTodos();

    void eliminarPorId(IdUsuario id);
}
