package co.edu.uniquindio.proyecto.domain.exception;

public class SolicitudCerradaException extends ReglaDominioException {

    public SolicitudCerradaException() {
        super("La solicitud está cerrada y no puede modificarse.");
    }
}