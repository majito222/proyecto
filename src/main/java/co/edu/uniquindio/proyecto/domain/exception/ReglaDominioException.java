package co.edu.uniquindio.proyecto.domain.exception;

/**
 * Excepcion base para violaciones de reglas de dominio.
 */
public class ReglaDominioException extends RuntimeException {

    /**
     * Crea la excepcion con un mensaje descriptivo.
     *
     * @param message mensaje de error
     */
    public ReglaDominioException(String message) {
        super(message);
    }
}
