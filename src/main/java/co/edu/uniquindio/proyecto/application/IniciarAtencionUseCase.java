package co.edu.uniquindio.proyecto.application;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.exception.RolNoAutorizadoException;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * Caso de uso para iniciar atención de una solicitud.
 */
@Service
@RequiredArgsConstructor
public class IniciarAtencionUseCase {

    private final SolicitudRepository solicitudRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * Inicia la atención de una solicitud.
     * @param solicitudId identificador de la solicitud
     * @param funcionarioId identificador del funcionario
     * @return solicitud en atención
     */
    @Transactional
    public Solicitud ejecutar(CodigoSolicitud solicitudId, IdUsuario funcionarioId) {

        Solicitud solicitud = solicitudRepository.buscarPorCodigo(solicitudId);
        Usuario funcionario = usuarioRepository.buscarPorId(funcionarioId);

        if (!funcionario.puedeAtenderSolicitudes()) {
            throw new RolNoAutorizadoException("Solo un funcionario activo puede iniciar atencion");
        }

        solicitud.iniciarAtencion(funcionario.getId());

        return solicitudRepository.guardar(solicitud);
    }
}
