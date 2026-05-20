package co.edu.uniquindio.proyecto.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CrearSolicitudRequest(
        @NotNull(message = "El canal es requerido")
        CanalSolicitud canal,
        @NotNull(message = "El tipo de solicitud es requerido")
        TipoSolicitud tipo,
        @NotBlank(message = "La descripcion es requerida")
        @Size(min = 20, max = 1000, message = "La descripcion debe tener entre 20 y 1000 caracteres")
        String descripcion
) {
    public enum CanalSolicitud {
        CSU,
        CORREO,
        SAC,
        TELEFONICO
    }

    public enum TipoSolicitud {
        REGISTRO_ASIGNATURA,
        HOMOLOGACION,
        CANCELACION,
        SOLICITUD_CUPO,
        CONSULTA_ACADEMICA
    }
}
