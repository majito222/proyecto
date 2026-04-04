package co.edu.uniquindio.proyecto.infrastructure.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para respuesta de solicitud.
 */
public record SolicitudResponse(

        String codigo,
        String estudianteId,
        String estudianteNombre,
        String canal,
        String tipo,
        String descripcion,
        String estado,
        String prioridad,
        List<HistorialDto> historial

) {

    public record HistorialDto(
            String accion,
            String responsable,
            String observacion,
            LocalDateTime fecha
    ) {}
}