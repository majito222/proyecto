package co.edu.uniquindio.proyecto.application.usecase;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.*;

/**
 * Caso de uso para asignar responsable a una solicitud.
 */
public class AsignarResponsableUseCase {

    private final SolicitudRepository solicitudRepository;
    private final UsuarioRepository usuarioRepository;

    public AsignarResponsableUseCase(SolicitudRepository solicitudRepository,
                                   UsuarioRepository usuarioRepository) {
        this.solicitudRepository = solicitudRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Asigna un funcionario responsable a una solicitud.
     * @param solicitudId identificador de la solicitud
     * @param funcionarioId identificador del funcionario
     * @return solicitud actualizada
     */
    public Solicitud ejecutar(CodigoSolicitud solicitudId, IdUsuario funcionarioId) {
        
        Solicitud solicitud = solicitudRepository.buscarPorCodigo(solicitudId);
        Usuario funcionario = usuarioRepository.buscarPorId(funcionarioId);

        // La asignación de responsable se hace a través de la clasificación
        solicitud.clasificarSolicitud(solicitud.getTipo(), funcionario.getId());

        return solicitudRepository.guardar(solicitud);
    }
}
