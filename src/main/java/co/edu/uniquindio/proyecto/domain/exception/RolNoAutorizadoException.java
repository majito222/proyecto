package co.edu.uniquindio.proyecto.domain.exception;

/**
 * Excepcion para operaciones no permitidas por el rol.
 */
public class RolNoAutorizadoException extends RuntimeException {

    /**
     * Crea la excepcion con el mensaje indicado.
     *
     * @param mensaje detalle del error
     */
    public RolNoAutorizadoException(String mensaje) {
        super(mensaje);
    }
}
