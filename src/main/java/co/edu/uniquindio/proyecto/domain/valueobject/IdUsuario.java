package co.edu.uniquindio.proyecto.domain.valueobject;

import java.util.Objects;
import java.util.Random;

public record IdUsuario(String valor) {

    public IdUsuario {
        Objects.requireNonNull(valor, "El id del usuario es obligatorio");

        if (valor.isBlank()) {
            throw new IllegalArgumentException("El id del usuario no puede estar vacío");
        }

        if (!valor.matches("\\d+")) {
            throw new IllegalArgumentException(
                    "El id del usuario solo puede contener números"
            );
        }

        if (valor.length() < 6) {
            throw new IllegalArgumentException(
                    "El id del usuario debe tener al menos 6 dígitos"
            );
        }
    }

    public static IdUsuario generar() {
        Random random = new Random();
        int numero = 100000 + random.nextInt(900000);
        return new IdUsuario(String.valueOf(numero));
    }


    @Override
    public String toString() {
        return valor;
    }
}
