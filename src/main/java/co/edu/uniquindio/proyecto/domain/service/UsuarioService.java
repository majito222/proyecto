package co.edu.uniquindio.proyecto.domain.service;

import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.valueobject.*;

/**
 * Servicio de dominio para operaciones basicas de usuarios.
 */
public class UsuarioService {

    /**
     * Crea un nuevo usuario.
     *
     * @param id identificador del usuario
     * @param nombre nombre del usuario
     * @param email email del usuario
     * @param tipo rol del usuario
     * @return usuario creado
     */
    public Usuario crearUsuario(IdUsuario id,
                                String nombre,
                                Email email,
                                TipoUsuario tipo) {

        return new Usuario(id, nombre, email, tipo);
    }

    /**
     * Activa un usuario.
     *
     * @param usuario usuario a activar
     */
    public void activarUsuario(Usuario usuario) {
        usuario.activarUsuario();
    }

    /**
     * Desactiva un usuario.
     *
     * @param usuario usuario a desactivar
     */
    public void desactivarUsuario(Usuario usuario) {
        usuario.desactivarUsuario();
    }

}
