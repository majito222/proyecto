package co.edu.uniquindio.proyecto.application.usecase;

import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.IdUsuario;
import org.springframework.stereotype.Service;

@Service
public class ConsultarUsuarioPorIdUseCase {

    private final UsuarioRepository usuarioRepository;

    public ConsultarUsuarioPorIdUseCase(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario ejecutar(IdUsuario id) {
        return usuarioRepository.buscarPorId(id);
    }
}