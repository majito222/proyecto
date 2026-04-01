package co.edu.uniquindio.proyecto.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO para crear una nueva solicitud.
 * Representa la intención de crear una solicitud académica.
 */
public record CrearSolicitudRequest(
    
    @NotBlank(message = "El ID del estudiante es requerido")
    String estudianteId,
    
    @NotNull(message = "El canal es requerido")
    CanalSolicitud canal,
    
    @NotNull(message = "El tipo de solicitud es requerido")
    TipoSolicitudDto tipo,
    
    @NotBlank(message = "La descripción es requerida")
    @Size(min = 20, max = 1000, message = "La descripción debe tener entre 20 y 1000 caracteres")
    String descripcion
) {
    
    /**
     * Canales de solicitud disponibles para la API.
     * Desacoplado del dominio interno.
     */
    public enum CanalSolicitud {
        CSU,
        CORREO,
        SAC,
        TELEFONICO
    }
    
    /**
     * Tipos de solicitud disponibles para la API.
     * Desacoplado del dominio interno.
     */
    public enum TipoSolicitudDto {
        REGISTRO_ASIGNATURA,
        HOMOLOGACION,
        CANCELACION,
        SOLICITUD_CUPO,
        CONSULTA_ACADEMICA
    }
}
