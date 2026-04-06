package co.edu.uniquindio.proyecto.domain.model;

public class Solicitud {

    private String id;
    private String descripcion;

    public Solicitud(String id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public String getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }
}