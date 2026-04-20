package co.edu.uniquindio.proyecto.application;

import co.edu.uniquindio.proyecto.application.security.PasswordHasher;
import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.Email;
import co.edu.uniquindio.proyecto.domain.valueobject.IdUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.TipoUsuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * Caso de uso para crear usuarios.
 */
@Service
@RequiredArgsConstructor
public class CrearUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;
    private final PasswordHasher passwordHasher;

    /**
     * Crea un nuevo usuario.
     *
     * @param id identificador del usuario
     * @param nombre nombre del usuario
     * @param email email del usuario
     * @param tipo rol del usuario
     * @return usuario creado
     */
    @Transactional
    public Usuario ejecutar(IdUsuario id, String nombre, Email email, String password, TipoUsuario tipo) {

        Usuario usuario = new Usuario(id, nombre, email, passwordHasher.hash(password), tipo);

        return usuarioRepository.guardar(usuario);
    }

    @Transactional
    public Usuario ejecutar(String nombre, Email email, String password, TipoUsuario tipo) {
        return ejecutar(IdUsuario.generar(), nombre, email, password, tipo);
    }
}
