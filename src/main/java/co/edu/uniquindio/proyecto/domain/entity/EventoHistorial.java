package co.edu.uniquindio.proyecto.domain.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class EventoHistorial {

    private final LocalDateTime fecha;
    private final String accion;
    private final String responsable;
    private final String observacion;

    public EventoHistorial(String accion,
                           String responsable,
                           String observacion) {

        this.fecha = LocalDateTime.now();
        this.accion = Objects.requireNonNull(accion);
        this.responsable = Objects.requireNonNull(responsable);
        this.observacion = observacion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getAccion() {
        return accion;
    }

    public String getResponsable() {
        return responsable;
    }

    public String getObservacion() {
        return observacion;
    }
}