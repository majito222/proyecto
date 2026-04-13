package co.edu.uniquindio.proyecto.application.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        String codigo,
        String mensaje,
        int status,
        String error,
        String ruta,
        LocalDateTime timestamp,
        List<ErrorDetailResponse> detalles
) {
    public ErrorResponse(String codigo,
                         String mensaje,
                         int status,
                         String error,
                         String ruta) {
        this(codigo, mensaje, status, error, ruta, LocalDateTime.now(), List.of());
    }

    public ErrorResponse(String codigo,
                         String mensaje,
                         int status,
                         String error,
                         String ruta,
                         List<ErrorDetailResponse> detalles) {
        this(codigo, mensaje, status, error, ruta, LocalDateTime.now(), detalles);
    }
}
