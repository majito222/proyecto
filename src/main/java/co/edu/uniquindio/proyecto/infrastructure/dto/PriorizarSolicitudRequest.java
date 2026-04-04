package co.edu.uniquindio.proyecto.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO para priorizar una solicitud.
 */
public record PriorizarSolicitudRequest(

        @NotBlank(message = "El ID del funcionario es requerido")
        String funcionarioId,

        @NotNull(message = "El nivel de prioridad es requerido")
        NivelPrioridad nivel,

        @NotBlank(message = "La justificación es requerida")
        @Size(min = 5, max = 500, message = "La justificación debe tener entre 5 y 500 caracteres")
        String justificacion

) {

    public enum NivelPrioridad {
        BAJA,
        MEDIA,
        ALTA,
        CRITICA
    }
}