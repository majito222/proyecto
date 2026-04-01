package co.edu.uniquindio.proyecto.application.usecase;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
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

    /**
     * Crea una nueva solicitud para un estudiante.
     *
     * @param estudianteId identificador del estudiante
     * @param codigo codigo de la solicitud
     * @param canal canal de registro
     * @param tipo tipo de solicitud
     * @param descripcion descripcion inicial
     * @return solicitud creada
     */
    public Solicitud ejecutar(IdUsuario estudianteId,
                            CodigoSolicitud codigo,
                            TipoCanal canal,
                            TipoSolicitud tipo,
                            DescripcionSolicitud descripcion) {

        Usuario estudiante = usuarioRepository.buscarPorId(estudianteId);
        
        Solicitud solicitud = new Solicitud(
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
