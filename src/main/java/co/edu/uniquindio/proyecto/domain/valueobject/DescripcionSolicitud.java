package co.edu.uniquindio.proyecto.domain.valueobject;

import java.util.Objects;

/**
 * Value object para la descripcion de una solicitud.
 */
public record DescripcionSolicitud(String valor) {

    /**
     * Valida que la descripcion tenga un minimo de longitud.
     *
     * @param valor texto de la descripcion
     */
    public DescripcionSolicitud {
        Objects.requireNonNull(valor);
        if (valor.isBlank() || valor.length() < 10) {
            throw new IllegalArgumentException("La descripciÃ³n es demasiado corta");
        }
    }
}
