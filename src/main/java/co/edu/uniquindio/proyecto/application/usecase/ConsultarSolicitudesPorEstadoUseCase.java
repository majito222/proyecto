package co.edu.uniquindio.proyecto.application.usecase;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.EstadoSolicitud;

import java.util.List;

/**
 * Caso de uso para consultar solicitudes por estado.
 */
public class ConsultarSolicitudesPorEstadoUseCase {

    private final SolicitudRepository solicitudRepository;

    public ConsultarSolicitudesPorEstadoUseCase(SolicitudRepository solicitudRepository) {
        this.solicitudRepository = solicitudRepository;
    }

    /**
     * Consulta solicitudes filtrando por estado.
     * @param estado estado a filtrar
     * @return lista de solicitudes en el estado especificado
     */
    public List<Solicitud> ejecutar(EstadoSolicitud estado) {
        return solicitudRepository.buscarPorEstado(estado);
    }
}
