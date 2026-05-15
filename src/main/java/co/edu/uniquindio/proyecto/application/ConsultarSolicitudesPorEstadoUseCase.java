package co.edu.uniquindio.proyecto.application;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.EstadoSolicitud;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Caso de uso para consultar solicitudes por estado.
 */
@Service
@RequiredArgsConstructor
public class ConsultarSolicitudesPorEstadoUseCase {

    private final SolicitudRepository solicitudRepository;

    /**
     * Consulta solicitudes filtrando por estado.
     * @param estado estado a filtrar
     * @return lista de solicitudes en el estado especificado
     */
    @Transactional(readOnly = true)
    public List<Solicitud> ejecutar(EstadoSolicitud estado) {
        return solicitudRepository.buscarPorEstado(estado);
    }
}