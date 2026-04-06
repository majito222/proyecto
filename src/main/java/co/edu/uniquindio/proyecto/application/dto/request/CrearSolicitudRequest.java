package co.edu.uniquindio.proyecto.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CrearSolicitudRequest(

        @NotBlank(message = "La descripción no puede estar vacía")
        @Size(min = 5, message = "Debe tener mínimo 5 caracteres")
        String descripcion) {
}