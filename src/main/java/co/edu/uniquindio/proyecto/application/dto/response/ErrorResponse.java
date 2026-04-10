package co.edu.uniquindio.proyecto.application.dto.response;

import java.time.LocalDateTime;

public record ErrorResponse(
        String mensaje,
        String codigo,
        LocalDateTime timestamp
) {
    public ErrorResponse(String mensaje, String codigo) {
        this(mensaje, codigo, LocalDateTime.now());
    }
}
