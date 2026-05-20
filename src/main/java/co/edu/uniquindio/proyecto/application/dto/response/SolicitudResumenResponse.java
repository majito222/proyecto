package co.edu.uniquindio.proyecto.application.dto.response;

import java.time.LocalDateTime;

public record SolicitudResumenResponse(
        String codigo,
        String estudianteId,
        String estudianteNombre,
        String tipo,
        String estado,
        String prioridad,
        String descripcion,
        LocalDateTime fechaCreacion
) {
    public SolicitudResumenResponse(
            String codigo,
            String estudianteId,
            String estudianteNombre,
            String tipo,
            String estado,
            String prioridad
    ) {
        this(codigo, estudianteId, estudianteNombre, tipo, estado, prioridad, null, null);
    }
}
