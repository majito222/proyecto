package co.edu.uniquindio.proyecto.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO para login de usuario.
 */
public record LoginRequest(

        @NotBlank(message = "El email es requerido")
        @Email(message = "El email debe ser valido")
        String email,

        @NotBlank(message = "La contrasena es requerida")
        String password

) {}
