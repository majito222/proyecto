package co.edu.uniquindio.proyecto.domain.valueobject;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Registro de eventos asociados a una solicitud.
 */
public record Historial(
        String accion,
        String responsable,
        String observacion,
        LocalDateTime fecha
) {

    /**
     * Valida los campos basicos del historial.
     *
     * @param accion descripcion de la accion
     * @param responsable responsable de la accion
     * @param observacion observacion adicional
     * @param fecha marca de tiempo del evento
     */
    public Historial {
        Objects.requireNonNull(accion, "La accion no puede ser nula");
        Objects.requireNonNull(responsable, "El responsable no puede ser nulo");

        if (fecha == null) {
            fecha = LocalDateTime.now();
        }
    }

    public Historial(String accion, String responsable, String observacion) {
        this(accion, responsable, observacion, null);
    }
}
