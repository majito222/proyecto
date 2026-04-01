package co.edu.uniquindio.proyecto.application.usecase;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.*;

/**
 * Caso de uso para cerrar una solicitud.
 */
public class CerrarSolicitudUseCase {

    private final SolicitudRepository solicitudRepository;
    private final UsuarioRepository usuarioRepository;

    public CerrarSolicitudUseCase(SolicitudRepository solicitudRepository,
                                 UsuarioRepository usuarioRepository) {
        this.solicitudRepository = solicitudRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Cierra una solicitud con observación.
     * @param solicitudId identificador de la solicitud
     * @param administradorId identificador del administrador
     * @param observacion observación de cierre
     * @return solicitud cerrada
     */
    public Solicitud ejecutar(CodigoSolicitud solicitudId, 
                            IdUsuario administradorId, 
                            String observacion) {

        Solicitud solicitud = solicitudRepository.buscarPorCodigo(solicitudId);
        Usuario administrador = usuarioRepository.buscarPorId(administradorId);

        solicitud.cerrarSolicitud(administrador.getId(), observacion);

        return solicitudRepository.guardar(solicitud);
    }
}
