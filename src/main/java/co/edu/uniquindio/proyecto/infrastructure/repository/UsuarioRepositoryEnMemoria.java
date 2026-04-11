package co.edu.uniquindio.proyecto.infrastructure.repository;

import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.Email;
import co.edu.uniquindio.proyecto.domain.valueobject.IdUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.TipoUsuario;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementación en memoria del repositorio de usuarios.
 */
@Repository
public class UsuarioRepositoryEnMemoria implements UsuarioRepository {

    private final Map<String, Usuario> usuarios = new ConcurrentHashMap<>();

    @Override
    public Usuario guardar(Usuario usuario) {
        String clave = usuario.getId().valor();
        usuarios.put(clave, usuario);
        return usuario;
    }

    @Override
    public Usuario buscarPorId(IdUsuario id) {
        Usuario usuario = usuarios.get(id.valor());
        if (usuario == null) {
            throw new NoSuchElementException("Usuario no encontrado: " + id.valor());
        }
        return usuario;
    }

    @Override
    public Optional<Usuario> buscarPorEmail(Email email) {
        return usuarios.values().stream()
                .filter(usuario -> usuario.getEmail().valor().equals(email.valor()))
                .findFirst();
    }

    @Override
    public List<Usuario> buscarPorTipo(TipoUsuario tipo) {
        return usuarios.values().stream()
                .filter(usuario -> usuario.getTipo() == tipo)
                .toList();
    }

    @Override
    public List<Usuario> listarTodos() {
        return new ArrayList<>(usuarios.values());
    }

    @Override
    public void eliminarPorId(IdUsuario id) {
        usuarios.remove(id.valor());
    }
    //  MÉTODOS SPRING DATA
    @Override
    public Usuario save(Usuario usuario) {
        return guardar(usuario);  // Reutiliza tu método
    }

    @Override
    public Optional<Usuario> findById(IdUsuario id) {
        try {
            return Optional.of(buscarPorId(id));
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }
}

