package co.edu.uniquindio.proyecto.infrastructure.dto;

import java.time.LocalDateTime;

/**
 * DTO para respuestas de error.
 */
public record ErrorResponse(

        String mensaje,
        String codigo,
        LocalDateTime timestamp

) {
    public ErrorResponse(String mensaje, String codigo) {
        this(mensaje, codigo, LocalDateTime.now());
    }
}