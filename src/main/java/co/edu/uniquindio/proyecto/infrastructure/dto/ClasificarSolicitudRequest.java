package co.edu.uniquindio.proyecto.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para clasificar una solicitud.
 */
public record ClasificarSolicitudRequest(

        @NotBlank(message = "El ID del funcionario es requerido")
        String funcionarioId,

        @NotNull(message = "El tipo de solicitud es requerido")
        TipoSolicitud tipo

) {

    public enum TipoSolicitud {
        REGISTRO_ASIGNATURA,
        HOMOLOGACION,
        CANCELACION,
        SOLICITUD_CUPO,
        CONSULTA_ACADEMICA
    }
}