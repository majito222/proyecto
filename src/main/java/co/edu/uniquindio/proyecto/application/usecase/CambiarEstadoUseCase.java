package co.edu.uniquindio.proyecto.application.usecase;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.*;

/**
 * Caso de uso para cambiar el estado de una solicitud.
 */
public class CambiarEstadoUseCase {

    private final SolicitudRepository solicitudRepository;
    private final UsuarioRepository usuarioRepository;

    public CambiarEstadoUseCase(SolicitudRepository solicitudRepository,
                               UsuarioRepository usuarioRepository) {
        this.solicitudRepository = solicitudRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Cambia el estado de una solicitud según el tipo de transición.
     * @param solicitudId identificador de la solicitud
     * @param usuarioId identificador del usuario que realiza el cambio
     * @param tipoTransicion tipo de transición a realizar
     * @return solicitud actualizada
     */
    public Solicitud ejecutar(CodigoSolicitud solicitudId, 
                           IdUsuario usuarioId, 
                           String tipoTransicion) {

        Solicitud solicitud = solicitudRepository.buscarPorCodigo(solicitudId);
        Usuario usuario = usuarioRepository.buscarPorId(usuarioId);

        switch (tipoTransicion.toUpperCase()) {
            case "CLASIFICAR":
                solicitud.clasificarSolicitud(solicitud.getTipo(), usuario.getId());
                break;
            case "PRIORIZAR":
                solicitud.asignarPrioridad(new PrioridadSolicitud(PrioridadSolicitud.Nivel.ALTA, "Prioridad asignada por sistema"), usuario.getId());
                break;
            case "INICIAR_ATENCION":
                solicitud.iniciarAtencion(usuario.getId());
                break;
            case "MARCAR_ATENDIDA":
                solicitud.marcarAtendida(usuario.getId());
                break;
            case "CERRAR":
                throw new IllegalArgumentException("Use el endpoint específico /cerrar para cerrar solicitudes");
            default:
                throw new IllegalArgumentException("Acción no válida: " + tipoTransicion);
        }

        return solicitudRepository.guardar(solicitud);
    }
}
