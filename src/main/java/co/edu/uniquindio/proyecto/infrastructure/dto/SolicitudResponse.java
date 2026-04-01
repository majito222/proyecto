package co.edu.uniquindio.proyecto.infrastructure.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para respuesta de solicitud.
 * Expone solo los datos necesarios para la API.
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
    List<HistorialDto> historial,
    LocalDateTime fechaCreacion
) {
    
    /**
     * DTO para historial de solicitud.
     * Expone información controlada del historial.
     */
    public record HistorialDto(
        String accion,
        String responsable,
        String observacion,
        LocalDateTime fecha
    ) {}
}
