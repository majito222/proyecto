package co.edu.uniquindio.proyecto.domain.exception;

/**
 * Excepcion para operaciones sobre una solicitud cerrada.
 */
public class SolicitudCerradaException extends ReglaDominioException {

    /**
     * Crea la excepcion con el mensaje por defecto.
     */
    public SolicitudCerradaException() {
        super("La solicitud estÃ¡ cerrada y no puede modificarse.");
    }
}
