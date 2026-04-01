package co.edu.uniquindio.proyecto.infrastructure.dto;

/**
 * DTO para respuestas de error.
 */
public record ErrorResponse(
    
    String mensaje,
    String codigo,
    long timestamp
) {
    public ErrorResponse(String mensaje, String codigo) {
        this(mensaje, codigo, System.currentTimeMillis());
    }
}
