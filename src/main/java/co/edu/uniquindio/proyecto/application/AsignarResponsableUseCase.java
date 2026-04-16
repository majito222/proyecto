package co.edu.uniquindio.proyecto.application;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.CodigoSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.IdUsuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso para asignar responsable a una solicitud.
 */
@Service
@RequiredArgsConstructor
public class AsignarResponsableUseCase {

    private final SolicitudRepository solicitudRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * Asigna un funcionario responsable a una solicitud.
     *
     * @param solicitudId identificador de la solicitud
     * @param funcionarioId identificador del funcionario
     * @return solicitud actualizada
     */
    @Transactional
    public Solicitud ejecutar(CodigoSolicitud solicitudId, IdUsuario funcionarioId) {

        // 1. Obtener entidades desde repositorios
        Solicitud solicitud = solicitudRepository.buscarPorCodigo(solicitudId);
        Usuario funcionario = usuarioRepository.buscarPorId(funcionarioId);

        // 2. Delegar comportamiento al dominio
        solicitud.asignarResponsable(funcionario.getId());

        // 3. Guardar cambios
        return solicitudRepository.guardar(solicitud);
    }
}
