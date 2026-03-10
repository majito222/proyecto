package co.edu.uniquindio.proyecto.domain.valueobject;

import java.util.Objects;

/**
 * Value object que representa la prioridad de una solicitud.
 */
public record PrioridadSolicitud(Nivel nivel, String justificacion) {

    /**
     * Niveles posibles de prioridad.
     */
    public enum Nivel {
        BAJA,
        MEDIA,
        ALTA,
        CRITICA
    }

    /**
     * Valida que exista nivel y justificacion.
     *
     * @param nivel nivel de prioridad
     * @param justificacion texto justificando la prioridad
     */
    public PrioridadSolicitud {
        Objects.requireNonNull(nivel);
        Objects.requireNonNull(justificacion);
        if (justificacion.isBlank()) {
            throw new IllegalArgumentException("Debe existir justificaciÃ³n");
        }
    }
}
