package co.edu.uniquindio.proyecto.application.dto.response;

import java.util.List;

/**
 * DTO de respuesta para autenticacion.
 */
public record LoginResponse(
        String token,
        String tipo,
        long expiresIn,
        List<String> roles
) {}
