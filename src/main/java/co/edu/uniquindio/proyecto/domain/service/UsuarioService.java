package co.edu.uniquindio.proyecto.domain.service;

import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.valueobject.*;

public class UsuarioService {

    public Usuario crearUsuario(IdUsuario id,
                                String nombre,
                                Email email,
                                TipoUsuario tipo) {

        return new Usuario(id, nombre, email, tipo);
    }

    public void activarUsuario(Usuario usuario) {
        usuario.activarUsuario();
    }

    public void desactivarUsuario(Usuario usuario) {
        usuario.desactivarUsuario();
    }

}
