package co.edu.uniquindio.proyecto.application.usecase;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.*;

/**
 * Caso de uso para crear una nueva solicitud.
 */
public class CrearSolicitudUseCase {

    private final SolicitudRepository solicitudRepository;
    private final UsuarioRepository usuarioRepository;

    public CrearSolicitudUseCase(SolicitudRepository solicitudRepository,
                                 UsuarioRepository usuarioRepository) {
        this.solicitudRepository = solicitudRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Solicitud ejecutar(IdUsuario estudianteId,
                              CodigoSolicitud codigo,
                              TipoCanal canal,
                              TipoSolicitud tipo,
                              DescripcionSolicitud descripcion) {

        Usuario estudiante = usuarioRepository.buscarPorId(estudianteId);

        Solicitud solicitud = Solicitud.crear(
                codigo,
                estudiante.getId(),
                estudiante.getNombre(),
                canal,
                tipo,
                descripcion
        );

        return solicitudRepository.guardar(solicitud);
    }
}
