package co.edu.uniquindio.proyecto.domain.repository;

import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.valueobject.Email;
import co.edu.uniquindio.proyecto.domain.valueobject.IdUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.TipoUsuario;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz de repositorio para la entidad Usuario.
 */
public interface UsuarioRepository {

    /**
     * Guarda un usuario (crea o actualiza).
     * @param usuario usuario a guardar
     * @return usuario guardado
     */
    Usuario guardar(Usuario usuario);

    /**
     * Busca un usuario por su identificador.
     * @param id identificador del usuario
     * @return usuario encontrado
     */
    Usuario buscarPorId(IdUsuario id);

    /**
     * Busca un usuario por su email.
     * @param email email del usuario
     * @return usuario encontrado
     */
    Optional<Usuario> buscarPorEmail(Email email);

    /**
     * Busca usuarios por tipo.
     * @param tipo tipo de usuario
     * @return lista de usuarios de ese tipo
     */
    List<Usuario> buscarPorTipo(TipoUsuario tipo);

    /**
     * Lista todos los usuarios.
     * @return lista de todos los usuarios
     */
    List<Usuario> listarTodos();

    /**
     * Elimina un usuario por su identificador.
     * @param id identificador del usuario a eliminar
     */
    void eliminarPorId(IdUsuario id);
}
