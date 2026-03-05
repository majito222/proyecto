package co.edu.uniquindio.proyecto.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

public record CodigoSolicitud(String valor) {

    public CodigoSolicitud {
        Objects.requireNonNull(valor);
        if (valor.isBlank()) {
            throw new IllegalArgumentException("El código no puede estar vacío");
        }
    }

    public static CodigoSolicitud generar() {
        return new CodigoSolicitud(UUID.randomUUID().toString());
    }
}