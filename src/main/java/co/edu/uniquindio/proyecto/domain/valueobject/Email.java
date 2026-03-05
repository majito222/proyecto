package co.edu.uniquindio.proyecto.domain.valueobject;

import java.util.Objects;
import java.util.regex.Pattern;

public record Email(String valor) {

    private static final Pattern FORMATO_EMAIL =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public Email {
        Objects.requireNonNull(valor, "El email no puede ser nulo");

        if (valor.isBlank()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }

        if (!FORMATO_EMAIL.matcher(valor).matches()) {
            throw new IllegalArgumentException("Formato de email inválido");
        }
    }

    @Override
    public String toString() {
        return valor;
    }
}
