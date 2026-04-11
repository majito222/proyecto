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
 * Caso de uso para cerrar una solicitud.
 */
@Service
@RequiredArgsConstructor
public class CerrarSolicitudUseCase {

    private final SolicitudRepository solicitudRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * Cierra una solicitud con observación.
     * @param solicitudId identificador de la solicitud
     * @param administradorId identificador del administrador
     * @param observacion observación de cierre
     * @return solicitud cerrada
     */
    @Transactional
    public Solicitud ejecutar(CodigoSolicitud solicitudId,
                              IdUsuario administradorId,
                              String observacion) {

        Solicitud solicitud = solicitudRepository.buscarPorCodigo(solicitudId);  // ✅ TU ESTILO
        Usuario administrador = usuarioRepository.buscarPorId(administradorId);  // ✅ TU ESTILO

        solicitud.cerrarSolicitud(administrador.getId(), observacion);

        return solicitudRepository.save(solicitud);
    }
}