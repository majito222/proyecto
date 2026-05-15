package co.edu.uniquindio.proyecto.application.dto.response;

/**
 * DTO de respuesta para autenticación.
 */
public record LoginResponse(

        String token,
        String tipo,
        long expiresIn

) {}
