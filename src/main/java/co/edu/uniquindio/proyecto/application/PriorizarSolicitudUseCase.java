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
 * Caso de uso para priorizar una solicitud.
 */
@Service
@RequiredArgsConstructor
public class PriorizarSolicitudUseCase {

    private final SolicitudRepository solicitudRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * Asigna prioridad a una solicitud con justificación.
     * @param solicitudId identificador de la solicitud
     * @param funcionarioId identificador del funcionario
     * @param prioridad prioridad asignada
     * @return solicitud priorizada
     */
    @Transactional
    public Solicitud ejecutar(CodigoSolicitud solicitudId,
                              IdUsuario funcionarioId,
                              PrioridadSolicitud prioridad) {

        Solicitud solicitud = solicitudRepository.buscarPorCodigo(solicitudId);
        Usuario funcionario = usuarioRepository.buscarPorId(funcionarioId);

        solicitud.asignarPrioridad(prioridad, funcionario.getId());

        return solicitudRepository.guardar(solicitud);
    }
}
