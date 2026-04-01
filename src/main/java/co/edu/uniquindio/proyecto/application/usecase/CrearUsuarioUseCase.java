package co.edu.uniquindio.proyecto.application.usecase;

import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.valueobject.*;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;

/**
 * Caso de uso para crear usuarios.
 */
public class CrearUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    public CrearUsuarioUseCase(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Crea un nuevo usuario.
     * @param id identificador del usuario
     * @param nombre nombre del usuario
     * @param email email del usuario
     * @param tipo rol del usuario
     * @return usuario creado
     */
    public Usuario ejecutar(IdUsuario id, String nombre, Email email, TipoUsuario tipo) {
        Usuario usuario = new Usuario(id, nombre, email, tipo);
        return usuarioRepository.guardar(usuario);
    }

    /**
     * Activa un usuario.
     * @param usuario usuario a activar
     */
    public void activarUsuario(Usuario usuario) {
        usuario.activarUsuario();
        usuarioRepository.guardar(usuario);
    }

    /**
     * Desactiva un usuario.
     *
     * @param usuario usuario a desactivar
     */
    public void desactivarUsuario(Usuario usuario) {
        usuario.desactivarUsuario();
        usuarioRepository.guardar(usuario);
    }
}
