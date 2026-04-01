package co.edu.uniquindio.proyecto.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para cerrar solicitud.
 * Representa la intención de cierre con justificación.
 */
public record CerrarSolicitudRequest(
    
    @NotBlank(message = "El ID del administrador es requerido")
    String administradorId,
    
    @NotBlank(message = "La observación es requerida")
    @Size(min = 10, max = 500, message = "La observación debe tener entre 10 y 500 caracteres")
    String observacion
) {}
