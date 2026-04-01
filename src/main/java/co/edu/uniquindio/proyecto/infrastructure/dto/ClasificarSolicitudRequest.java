package co.edu.uniquindio.proyecto.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para clasificar una solicitud.
 * Representa la intención de clasificar con tipo específico.
 */
public record ClasificarSolicitudRequest(
    
    @NotBlank(message = "El ID del funcionario es requerido")
    String funcionarioId,
    
    @NotBlank(message = "El tipo de solicitud es requerido")
    TipoSolicitudDto tipo
) {
    
    /**
     * Tipos de solicitud para clasificación.
     */
    public enum TipoSolicitudDto {
        REGISTRO_ASIGNATURA,
        HOMOLOGACION,
        CANCELACION,
        SOLICITUD_CUPO,
        CONSULTA_ACADEMICA
    }
}
