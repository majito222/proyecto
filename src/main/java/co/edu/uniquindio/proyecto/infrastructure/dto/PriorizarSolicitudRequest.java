package co.edu.uniquindio.proyecto.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para priorizar una solicitud.
 * Representa la intención de asignar prioridad con justificación.
 */
public record PriorizarSolicitudRequest(
    
    @NotBlank(message = "El ID del funcionario es requerido")
    String funcionarioId,
    
    @NotNull(message = "El nivel de prioridad es requerido")
    NivelPrioridad nivel,
    
    @NotBlank(message = "La justificación es requerida")
    String justificacion
) {
    
    /**
     * Niveles de prioridad disponibles.
     */
    public enum NivelPrioridad {
        BAJA,
        MEDIA,
        ALTA,
        CRITICA
    }
}
