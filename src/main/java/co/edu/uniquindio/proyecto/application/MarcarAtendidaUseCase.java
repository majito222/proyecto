package co.edu.uniquindio.proyecto.application;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso para marcar solicitud como atendida.
 */
@Service
@RequiredArgsConstructor
public class MarcarAtendidaUseCase {

    private final SolicitudRepository solicitudRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * Marca una solicitud como atendida
     * @param solicitudId identificador de la solicitud
     * @param funcionarioId identificador del funcionario
     * @return solicitud atendida
     */
    @Transactional
    public Solicitud ejecutar(CodigoSolicitud solicitudId, IdUsuario funcionarioId) {

        Solicitud solicitud = solicitudRepository.buscarPorCodigo(solicitudId);
        Usuario funcionario = usuarioRepository.buscarPorId(funcionarioId);

        solicitud.marcarAtendida(funcionario.getId());

        return solicitudRepository.guardar(solicitud);
    }
}
