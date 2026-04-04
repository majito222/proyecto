package co.edu.uniquindio.proyecto.infrastructure.mapper;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.infrastructure.dto.SolicitudResponse;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper para convertir entre entidades de dominio y DTOs de solicitud.
 */
@Component
public class SolicitudMapper {

    /**
     * Convierte una entidad Solicitud a SolicitudResponse.
     */
    public SolicitudResponse toResponse(Solicitud solicitud) {
        return new SolicitudResponse(
                solicitud.getCodigo().valor(),
                solicitud.getEstudianteId().valor(),
                solicitud.getEstudianteNombre(),
                solicitud.getCanal().name(),
                solicitud.getTipo().name(),
                solicitud.getDescripcion().valor(),
                solicitud.getEstado().name(),
                solicitud.getPrioridad() != null ?
                        solicitud.getPrioridad().nivel().name() + " - " + solicitud.getPrioridad().justificacion()
                        : null,
                solicitud.obtenerHistorial().stream()
                        .map(h -> new SolicitudResponse.HistorialDto(
                                h.accion(),
                                h.responsable(),
                                h.observacion(),
                                h.fecha()
                        ))
                        .toList()
        );
    }

    /**
     * Convierte una lista de solicitudes a responses.
     */
    public List<SolicitudResponse> toResponseList(List<Solicitud> solicitudes) {
        return solicitudes.stream()
                .map(this::toResponse)
                .toList();
    }
}