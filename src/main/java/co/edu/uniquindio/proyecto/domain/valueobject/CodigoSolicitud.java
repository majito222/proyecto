package co.edu.uniquindio.proyecto.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

/**
 * Value object para el codigo de una solicitud.
 */
public record CodigoSolicitud(String valor) {

    /**
     * Valida que el valor no sea nulo ni vacio.
     *
     * @param valor codigo en formato texto
     */
    public CodigoSolicitud {
        Objects.requireNonNull(valor);
        if (valor.isBlank()) {
            throw new IllegalArgumentException("El cÃ³digo no puede estar vacÃ­o");
        }
    }

    /**
     * Genera un codigo aleatorio.
     *
     * @return codigo generado
     */
    public static CodigoSolicitud generar() {
        return new CodigoSolicitud(UUID.randomUUID().toString());
    }
}
