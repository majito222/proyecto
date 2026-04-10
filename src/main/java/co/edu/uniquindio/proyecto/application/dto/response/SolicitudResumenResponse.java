package co.edu.uniquindio.proyecto.application.dto.response;

public record SolicitudResumenResponse(
        String codigo,
        String estudianteId,
        String estudianteNombre,
        String tipo,
        String estado,
        String prioridad
) {
}
