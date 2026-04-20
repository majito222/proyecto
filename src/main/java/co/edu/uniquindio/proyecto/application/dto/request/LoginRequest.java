package co.edu.uniquindio.proyecto.application.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para login de usuario.
 */
public record LoginRequest(

        @NotBlank(message = "El ID del usuario es requerido")
        String usuarioId

) {}
