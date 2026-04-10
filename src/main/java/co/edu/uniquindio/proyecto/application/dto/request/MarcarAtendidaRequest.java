package co.edu.uniquindio.proyecto.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MarcarAtendidaRequest(
        @NotBlank(message = "El ID del funcionario es requerido")
        String funcionarioId
) {
}
