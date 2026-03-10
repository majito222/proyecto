package co.edu.uniquindio.proyecto.domain.valueobject;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value object para el correo electronico.
 */
public record Email(String valor) {

    private static final Pattern FORMATO_EMAIL =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    /**
     * Valida el formato y contenido del email.
     *
     * @param valor direccion de correo
     */
    public Email {
        Objects.requireNonNull(valor, "El email no puede ser nulo");

        if (valor.isBlank()) {
            throw new IllegalArgumentException("El email no puede estar vacÃ­o");
        }

        if (!FORMATO_EMAIL.matcher(valor).matches()) {
            throw new IllegalArgumentException("Formato de email invÃ¡lido");
        }
    }

    @Override
    public String toString() {
        return valor;
    }
}
