package co.edu.uniquindio.proyecto.application.dto.response;

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
        List<HistorialResponse> historial
) {
}
