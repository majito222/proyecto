package co.edu.uniquindio.proyecto.application;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.CodigoSolicitud;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso para consultar una solicitud por su código.
 */
@Service
@RequiredArgsConstructor
public class ConsultarSolicitudPorCodigoUseCase {

    private final SolicitudRepository solicitudRepository;

    @Transactional(readOnly = true)
    public Solicitud ejecutar(CodigoSolicitud codigo) {
        return solicitudRepository.buscarConHistorial(codigo)
                .orElseGet(() -> solicitudRepository.buscarPorCodigo(codigo));
    }
}
