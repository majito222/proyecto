package co.edu.uniquindio.proyecto.application.usecase;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.*;

/**
 * Caso de uso para priorizar una solicitud.
 */
public class PriorizarSolicitudUseCase {

    private final SolicitudRepository solicitudRepository;
    private final UsuarioRepository usuarioRepository;

    public PriorizarSolicitudUseCase(SolicitudRepository solicitudRepository,
                                   UsuarioRepository usuarioRepository) {
        this.solicitudRepository = solicitudRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Asigna prioridad a una solicitud con justificación.
     * @param solicitudId identificador de la solicitud
     * @param funcionarioId identificador del funcionario
     * @param prioridad prioridad asignada
     * @return solicitud priorizada
     */
    public Solicitud ejecutar(CodigoSolicitud solicitudId, 
                            IdUsuario funcionarioId, 
                            PrioridadSolicitud prioridad) {
        
        Solicitud solicitud = solicitudRepository.buscarPorCodigo(solicitudId);
        Usuario funcionario = usuarioRepository.buscarPorId(funcionarioId);

        solicitud.asignarPrioridad(prioridad, funcionario.getId());
        
        return solicitudRepository.guardar(solicitud);
    }
}
