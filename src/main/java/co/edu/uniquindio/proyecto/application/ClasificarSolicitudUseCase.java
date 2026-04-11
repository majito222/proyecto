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
 * Caso de uso para clasificar una solicitud.
 */
@Service
@RequiredArgsConstructor
public class ClasificarSolicitudUseCase {

    private final SolicitudRepository solicitudRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * Clasifica una solicitud con tipo específico.
     * @param solicitudId identificador de la solicitud
     * @param funcionarioId identificador del funcionario
     * @param tipo tipo de solicitud asignado
     * @return solicitud clasificada
     */
    @Transactional
    public Solicitud ejecutar(CodigoSolicitud solicitudId,
                              IdUsuario funcionarioId,
                              TipoSolicitud tipo) {

        // ✅ OPCION 2: Tus métodos originales (más simple)
        Solicitud solicitud = solicitudRepository.buscarPorCodigo(solicitudId);
        Usuario funcionario = usuarioRepository.buscarPorId(funcionarioId);

        solicitud.clasificarSolicitud(tipo, funcionario.getId());

        return solicitudRepository.save(solicitud);
    }
}