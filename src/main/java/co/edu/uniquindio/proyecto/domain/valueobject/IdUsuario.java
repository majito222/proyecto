package co.edu.uniquindio.proyecto.domain.valueobject;

import java.util.Objects;
import java.util.Random;

/**
 * Value object para el identificador de usuario.
 */
public record IdUsuario(String valor) {

    /**
     * Valida que el id sea numerico y tenga longitud minima.
     *
     * @param valor id del usuario
     */
    public IdUsuario {
        Objects.requireNonNull(valor, "El id del usuario es obligatorio");

        if (valor.isBlank()) {
            throw new IllegalArgumentException("El id del usuario no puede estar vacÃ­o");
        }

        if (!valor.matches("\\d+")) {
            throw new IllegalArgumentException(
                    "El id del usuario solo puede contener nÃºmeros"
            );
        }

        if (valor.length() < 6) {
            throw new IllegalArgumentException(
                    "El id del usuario debe tener al menos 6 dÃ­gitos"
            );
        }
    }

    /**
     * Genera un id numerico aleatorio de 6 digitos.
     *
     * @return id generado
     */
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
