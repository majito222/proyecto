package co.edu.uniquindio.proyecto.domain.entity;

import co.edu.uniquindio.proyecto.domain.valueobject.*;
import lombok.Getter;

import java.util.Objects;

@Getter
public class Usuario {

    private final IdUsuario id;
    private final String nombre;
    private final Email email;
    private final TipoUsuario tipo;
    private EstadoUsuario estado;

    public Usuario(IdUsuario id, String nombre, Email email, TipoUsuario tipo) {
        this.id = Objects.requireNonNull(id, "El id es obligatorio");
        this.nombre = Objects.requireNonNull(nombre, "El nombre es obligatorio");
        this.email = Objects.requireNonNull(email, "El email es obligatorio");
        this.tipo = Objects.requireNonNull(tipo, "El tipo es obligatorio");
        this.estado = EstadoUsuario.ACTIVO;
    }

    public boolean estaActivo() {
        return estado == EstadoUsuario.ACTIVO;
    }
}