package co.edu.uniquindio.proyecto.domain.valueobject;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Registro de eventos asociados a una solicitud.
 */
public record Historial(
        String accion,
        String responsable,
        String observacion
) {

    /**
     * Valida los campos basicos del historial.
     *
     * @param accion descripcion de la accion
     * @param responsable responsable de la accion
     * @param observacion observacion adicional
     */
    public Historial {
        Objects.requireNonNull(accion, "La acciÃ³n no puede ser nula");
        Objects.requireNonNull(responsable, "El responsable no puede ser nulo");

        LocalDateTime fecha = null;
        if (fecha == null) {
            fecha = LocalDateTime.now();
        }
    }

}
