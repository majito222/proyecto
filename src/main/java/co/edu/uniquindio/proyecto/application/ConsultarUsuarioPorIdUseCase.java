package co.edu.uniquindio.proyecto.application;

import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.IdUsuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConsultarUsuarioPorIdUseCase {

    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public Usuario ejecutar(IdUsuario id) {
        return usuarioRepository.buscarPorId(id);
    }
}