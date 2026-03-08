package co.edu.uniquindio.proyecto.domain.valueobject;

import java.time.LocalDateTime;
import java.util.Objects;

public record Historial(
        String accion,
        String responsable,
        String observacion
) {

    public Historial {
        Objects.requireNonNull(accion, "La acción no puede ser nula");
        Objects.requireNonNull(responsable, "El responsable no puede ser nulo");

        LocalDateTime fecha = null;
        if (fecha == null) {
            fecha = LocalDateTime.now();
        }
    }
    
}