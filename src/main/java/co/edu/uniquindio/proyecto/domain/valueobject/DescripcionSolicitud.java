package co.edu.uniquindio.proyecto.domain.valueobject;

import java.util.Objects;

public record DescripcionSolicitud(String valor) {

    public DescripcionSolicitud {
        Objects.requireNonNull(valor);
        if (valor.isBlank() || valor.length() < 10) {
            throw new IllegalArgumentException("La descripción es demasiado corta");
        }
    }
}