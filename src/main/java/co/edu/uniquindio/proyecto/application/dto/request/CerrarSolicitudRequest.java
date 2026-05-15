package co.edu.uniquindio.proyecto.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CerrarSolicitudRequest(
        @NotBlank(message = "La observacion es requerida")
        @Size(min = 10, max = 500, message = "La observacion debe tener entre 10 y 500 caracteres")
        String observacion
) {
}
