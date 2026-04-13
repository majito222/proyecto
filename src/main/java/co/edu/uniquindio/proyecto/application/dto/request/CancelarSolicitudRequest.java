package co.edu.uniquindio.proyecto.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CancelarSolicitudRequest(
        @NotBlank(message = "El ID del responsable es requerido")
        String responsableId,
        @NotBlank(message = "La observacion es requerida")
        @Size(min = 10, max = 500, message = "La observacion debe tener entre 10 y 500 caracteres")
        String observacion
) {
}
