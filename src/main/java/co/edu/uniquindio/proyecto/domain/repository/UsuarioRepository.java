package co.edu.uniquindio.proyecto.domain.repository;

import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.valueobject.Email;
import co.edu.uniquindio.proyecto.domain.valueobject.IdUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.TipoUsuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {

    Usuario guardar(Usuario usuario);

    Usuario buscarPorId(IdUsuario id);

    Optional<Usuario> buscarPorEmail(Email email);

    List<Usuario> buscarPorTipo(TipoUsuario tipo);

    Page<Usuario> listarTodos(Pageable pageable);

    void eliminarPorId(IdUsuario id);
}
