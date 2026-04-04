package co.edu.uniquindio.proyecto.infrastructure.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO para crear usuario.
 */
public record CrearUsuarioRequest(

        @NotBlank(message = "El nombre es requerido")
        @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
        String nombre,

        @NotBlank(message = "El email es requerido")
        @Email(message = "El email debe ser válido")
        String email,

        @NotNull(message = "El tipo de usuario es requerido")
        TipoUsuario tipo

) {

    public enum TipoUsuario {
        ESTUDIANTE,
        FUNCIONARIO,
        ADMINISTRADOR
    }
}
