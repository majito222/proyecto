package co.edu.uniquindio.proyecto.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para cambiar estado de solicitud.
 * Representa una intención de negocio específica.
 */
public record CambiarEstadoRequest(
    
    @NotBlank(message = "El ID del usuario es requerido")
    String usuarioId,
    
    @NotBlank(message = "La acción es requerida")
    AccionSolicitud accion
) {
    
    /**
     * Acciones de negocio permitidas para solicitudes.
     * Representa intenciones claras del dominio.
     */
    public enum AccionSolicitud {
        CLASIFICAR,
        PRIORIZAR,
        INICIAR_ATENCION,
        MARCAR_ATENDIDA,
        CERRAR
    }
}
