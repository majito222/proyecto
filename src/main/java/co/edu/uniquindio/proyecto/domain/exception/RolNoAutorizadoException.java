package co.edu.uniquindio.proyecto.domain.exception;

public class RolNoAutorizadoException extends RuntimeException {

    public RolNoAutorizadoException(String mensaje) {
        super(mensaje);
    }
}