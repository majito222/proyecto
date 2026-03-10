package co.edu.uniquindio.proyecto.domain.entity;

import co.edu.uniquindio.proyecto.domain.valueobject.*;
import lombok.Getter;

import java.util.Objects;

/**
 * Entidad de dominio que representa un usuario del sistema.
 */
@Getter
public class Usuario {

    private final IdUsuario id;
    private String nombre;
    private Email email;
    private final TipoUsuario tipo;
    private EstadoUsuario estado;

    /**
     * Crea un usuario activo con los datos basicos.
     *
     * @param id identificador del usuario
     * @param nombre nombre del usuario
     * @param email email del usuario
     * @param tipo rol del usuario
     */
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

    /**
     * Indica si el usuario esta activo.
     *
     * @return true si el estado es ACTIVO
     */
    public boolean estaActivo() {
        return estado == EstadoUsuario.ACTIVO;
    }

    /**
     * Un estudiante activo puede registrar solicitudes.
     *
     * @return true si el usuario puede registrar
     */
    public boolean puedeRegistrarSolicitudes() {
        return tipo == TipoUsuario.ESTUDIANTE && estaActivo();
    }

    /**
     * Un funcionario activo puede atender solicitudes.
     *
     * @return true si el usuario puede atender
     */
    public boolean puedeAtenderSolicitudes() {
        return tipo == TipoUsuario.FUNCIONARIO && estaActivo();
    }

    /**
     * Un administrador activo puede gestionar solicitudes.
     *
     * @return true si el usuario puede administrar
     */
    public boolean puedeAdministrarSolicitudes() {
        return tipo == TipoUsuario.ADMINISTRADOR && estaActivo();
    }

    /**
     * Cambia el estado del usuario a INACTIVO.
     */
    public void desactivarUsuario() {
        this.estado = EstadoUsuario.INACTIVO;
    }

    /**
     * Cambia el estado del usuario a ACTIVO.
     */
    public void activarUsuario() {
        this.estado = EstadoUsuario.ACTIVO;
    }

    /**
     * Actualiza el email del usuario.
     *
     * @param nuevoEmail nuevo email
     */
    public void cambiarEmail(Email nuevoEmail) {
        this.email = Objects.requireNonNull(nuevoEmail);
    }

    /**
     * Actualiza el nombre del usuario.
     *
     * @param nuevoNombre nuevo nombre
     */
    public void cambiarNombre(String nuevoNombre) {
        this.nombre = Objects.requireNonNull(nuevoNombre);
    }
}
