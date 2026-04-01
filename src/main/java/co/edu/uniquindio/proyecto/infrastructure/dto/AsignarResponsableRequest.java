package co.edu.uniquindio.proyecto.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para asignar responsable a solicitud.
 */
public record AsignarResponsableRequest(
    
    @NotBlank(message = "El ID del funcionario es requerido")
    String funcionarioId
) {}
