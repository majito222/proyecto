package co.edu.uniquindio.proyecto.application.usecase;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.CodigoSolicitud;
import org.springframework.stereotype.Service;

/**
 * Caso de uso para consultar una solicitud por su código.
 */
@Service
public class ConsultarSolicitudPorCodigoUseCase {

    private final SolicitudRepository solicitudRepository;

    public ConsultarSolicitudPorCodigoUseCase(SolicitudRepository solicitudRepository) {
        this.solicitudRepository = solicitudRepository;
    }

    public Solicitud ejecutar(CodigoSolicitud codigo) {
        return solicitudRepository.buscarPorCodigo(codigo);
    }
}