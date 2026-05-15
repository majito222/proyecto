package co.edu.uniquindio.proyecto.application;

import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ListarUsuariosUseCase {

    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public Page<Usuario> ejecutar(Pageable pageable) {
        return usuarioRepository.listarTodos(pageable);
    }
}
