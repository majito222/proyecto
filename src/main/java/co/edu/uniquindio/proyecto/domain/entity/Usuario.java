package co.edu.uniquindio.proyecto.domain.entity;

import co.edu.uniquindio.proyecto.domain.valueobject.*;
import lombok.Getter;

import java.util.Objects;

@Getter
public class Usuario {

    private final IdUsuario id;
    private String nombre;
    private Email email;
    private final TipoUsuario tipo;
    private EstadoUsuario estado;

    public Usuario(IdUsuario id,
                   String nombre,
                   Email email,
                   TipoUsuario tipo) {

        this.id = Objects.requireNonNull(id);
        this.nombre = Objects.requireNonNull(nombre);
        this.email = Objects.requireNonNull(email);
        this.tipo = Objects.requireNonNull(tipo);
        this.estado = EstadoUsuario.ACTIVO;
    }

    public boolean estaActivo() {
        return estado == EstadoUsuario.ACTIVO;
    }

    public boolean puedeRegistrarSolicitudes() {
        return tipo == TipoUsuario.ESTUDIANTE && estaActivo();
    }

    public boolean puedeAtenderSolicitudes() {
        return tipo == TipoUsuario.FUNCIONARIO && estaActivo();
    }

    public boolean puedeAdministrarSolicitudes() {
        return tipo == TipoUsuario.ADMINISTRADOR && estaActivo();
    }

    public void desactivarUsuario() {
        this.estado = EstadoUsuario.INACTIVO;
    }

    public void activarUsuario() {
        this.estado = EstadoUsuario.ACTIVO;
    }

    public void cambiarEmail(Email nuevoEmail) {
        this.email = Objects.requireNonNull(nuevoEmail);
    }

    public void cambiarNombre(String nuevoNombre) {
        this.nombre = Objects.requireNonNull(nuevoNombre);
    }
}