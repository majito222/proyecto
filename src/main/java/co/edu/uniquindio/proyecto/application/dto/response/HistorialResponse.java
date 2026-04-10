package co.edu.uniquindio.proyecto.application.dto.response;

import java.time.LocalDateTime;

public record HistorialResponse(
        String accion,
        String responsable,
        String observacion,
        LocalDateTime fecha
) {
}
