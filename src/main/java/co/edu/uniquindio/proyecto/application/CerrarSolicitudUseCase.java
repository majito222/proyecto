package co.edu.uniquindio.proyecto.application;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.exception.RolNoAutorizadoException;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.CodigoSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.IdUsuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CerrarSolicitudUseCase {

    private final SolicitudRepository solicitudRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Solicitud ejecutar(CodigoSolicitud solicitudId,
                              IdUsuario administradorId,
                              String observacion) {

        Solicitud solicitud = solicitudRepository.buscarPorCodigo(solicitudId);
        Usuario administrador = usuarioRepository.buscarPorId(administradorId);

        if (!administrador.puedeAdministrarSolicitudes()) {
            throw new RolNoAutorizadoException("Solo un administrador activo puede cerrar solicitudes");
        }

        solicitud.cerrarSolicitud(administrador.getId(), observacion);

        return solicitudRepository.guardar(solicitud);
    }
}
