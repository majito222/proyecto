package co.edu.uniquindio.proyecto.application.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record SolicitudDetalleResponse(
        String codigo,
        String estudianteId,
        String estudianteNombre,
        String canal,
        String tipo,
        String descripcion,
        String estado,
        String prioridad,
        LocalDateTime fechaCreacion,
        List<HistorialResponse> historial
) {
    public SolicitudDetalleResponse(
            String codigo,
            String estudianteId,
            String estudianteNombre,
            String canal,
            String tipo,
            String descripcion,
            String estado,
            String prioridad,
            List<HistorialResponse> historial
    ) {
        this(codigo, estudianteId, estudianteNombre, canal, tipo, descripcion, estado, prioridad, null, historial);
    }
}
