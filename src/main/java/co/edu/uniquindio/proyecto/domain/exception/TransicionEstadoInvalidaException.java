package co.edu.uniquindio.proyecto.domain.exception;

/**
 * Excepcion para transiciones de estado no validas.
 */
public class TransicionEstadoInvalidaException extends ReglaDominioException {

    /**
     * Crea la excepcion con el mensaje indicado.
     *
     * @param message detalle de la transicion invalida
     */
    public TransicionEstadoInvalidaException(String message) {
        super(message);
    }
}
