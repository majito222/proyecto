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
 * Caso de uso para crear una nueva solicitud.
 */
@Service
@RequiredArgsConstructor
public class CrearSolicitudUseCase {

    private final SolicitudRepository solicitudRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Solicitud ejecutar(IdUsuario estudianteId,
                              CodigoSolicitud codigo,
                              TipoCanal canal,
                              TipoSolicitud tipo,
                              DescripcionSolicitud descripcion) {

        Usuario estudiante = usuarioRepository.buscarPorId(estudianteId);

        if (solicitudRepository.existePorCodigo(codigo)) {
            throw new IllegalArgumentException("Ya existe una solicitud con el codigo " + codigo.valor());
        }

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

    @Transactional
    public Solicitud ejecutar(IdUsuario estudianteId,
                              TipoCanal canal,
                              TipoSolicitud tipo,
                              DescripcionSolicitud descripcion) {
        return ejecutar(estudianteId, CodigoSolicitud.generar(), canal, tipo, descripcion);
    }
}
