package co.edu.uniquindio.proyecto.infrastructure.mapper;

import co.edu.uniquindio.proyecto.application.dto.response.HistorialResponse;
import co.edu.uniquindio.proyecto.application.dto.response.SolicitudDetalleResponse;
import co.edu.uniquindio.proyecto.application.dto.response.SolicitudResumenResponse;
import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper para convertir entre entidades de dominio y DTOs de solicitud.
 */
@Component
public class SolicitudMapper {

    public SolicitudDetalleResponse toDetalleResponse(Solicitud solicitud) {
        return new SolicitudDetalleResponse(
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
                        .map(h -> new HistorialResponse(
                                h.accion(),
                                h.responsable(),
                                h.observacion(),
                                h.fecha()
                        ))
                        .toList()
        );
    }

    public SolicitudResumenResponse toResumenResponse(Solicitud solicitud) {
        return new SolicitudResumenResponse(
                solicitud.getCodigo().valor(),
                solicitud.getEstudianteId().valor(),
                solicitud.getEstudianteNombre(),
                solicitud.getTipo().name(),
                solicitud.getEstado().name(),
                solicitud.getPrioridad() != null ? solicitud.getPrioridad().nivel().name() : null
        );
    }

    public List<SolicitudResumenResponse> toResumenResponseList(List<Solicitud> solicitudes) {
        return solicitudes.stream()
                .map(this::toResumenResponse)
                .toList();
    }
}
