package co.edu.uniquindio.proyecto.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PriorizarSolicitudRequest(
        @NotBlank(message = "El ID del funcionario es requerido")
        String funcionarioId,
        @NotNull(message = "El nivel de prioridad es requerido")
        NivelPrioridad nivel,
        @NotBlank(message = "La justificacion es requerida")
        @Size(min = 5, max = 500, message = "La justificacion debe tener entre 5 y 500 caracteres")
        String justificacion
) {
    public enum NivelPrioridad {
        BAJA,
        MEDIA,
        ALTA,
        CRITICA
    }
}
