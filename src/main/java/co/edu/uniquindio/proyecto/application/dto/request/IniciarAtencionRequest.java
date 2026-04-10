package co.edu.uniquindio.proyecto.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record IniciarAtencionRequest(
        @NotBlank(message = "El ID del funcionario es requerido")
        String funcionarioId
) {
}
