package co.edu.uniquindio.proyecto.application.dto.request;

import jakarta.validation.constraints.NotNull;

public record ClasificarSolicitudRequest(
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
