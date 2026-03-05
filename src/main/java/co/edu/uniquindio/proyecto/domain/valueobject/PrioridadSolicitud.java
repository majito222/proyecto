package co.edu.uniquindio.proyecto.domain.valueobject;

import java.util.Objects;

public record PrioridadSolicitud(Nivel nivel, String justificacion) {

    public enum Nivel {
        BAJA,
        MEDIA,
        ALTA,
        CRITICA
    }

    public PrioridadSolicitud {
        Objects.requireNonNull(nivel);
        Objects.requireNonNull(justificacion);
        if (justificacion.isBlank()) {
            throw new IllegalArgumentException("Debe existir justificación");
        }
    }
}