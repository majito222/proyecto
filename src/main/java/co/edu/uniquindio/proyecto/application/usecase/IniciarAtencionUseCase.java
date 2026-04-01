package co.edu.uniquindio.proyecto.application.usecase;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.*;

/**
 * Caso de uso para iniciar atención de una solicitud.
 */
public class IniciarAtencionUseCase {

    private final SolicitudRepository solicitudRepository;
    private final UsuarioRepository usuarioRepository;

    public IniciarAtencionUseCase(SolicitudRepository solicitudRepository,
                                UsuarioRepository usuarioRepository) {
        this.solicitudRepository = solicitudRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Inicia la atención de una solicitud.
     * @param solicitudId identificador de la solicitud
     * @param funcionarioId identificador del funcionario
     * @return solicitud en atención
     */
    public Solicitud ejecutar(CodigoSolicitud solicitudId, IdUsuario funcionarioId) {
        
        Solicitud solicitud = solicitudRepository.buscarPorCodigo(solicitudId);
        Usuario funcionario = usuarioRepository.buscarPorId(funcionarioId);

        solicitud.iniciarAtencion(funcionario.getId());
        
        return solicitudRepository.guardar(solicitud);
    }
}
