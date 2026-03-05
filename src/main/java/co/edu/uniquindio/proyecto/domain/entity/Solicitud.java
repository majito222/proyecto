package co.edu.uniquindio.proyecto.domain.entity;

import co.edu.uniquindio.proyecto.domain.exception.*;
import co.edu.uniquindio.proyecto.domain.valueobject.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.*;

@Getter
public class Solicitud {

    private final CodigoSolicitud codigo;
    private final IdUsuario solicitante;
    private final TipoCanal canal;
    private final LocalDateTime fechaRegistro;

    private DescripcionSolicitud descripcion;
    private TipoSolicitud tipo;
    private EstadoSolicitud estado;
    private PrioridadSolicitud prioridad;

    private final List<EventoHistorial> historial;

    public Solicitud(Usuario solicitante,
                     TipoCanal canal,
                     DescripcionSolicitud descripcion) {

        Objects.requireNonNull(solicitante, "El solicitante es obligatorio");

        if (solicitante.getTipo() != TipoUsuario.ESTUDIANTE) {
            throw new RolNoAutorizadoException(
                    "Solo un estudiante puede registrar solicitudes"
            );
        }

        this.codigo = CodigoSolicitud.generar();
        this.solicitante = solicitante.getId();
        this.canal = Objects.requireNonNull(canal, "El canal es obligatorio");
        this.descripcion = Objects.requireNonNull(descripcion, "La descripción es obligatoria");

        this.fechaRegistro = LocalDateTime.now();
        this.estado = EstadoSolicitud.REGISTRADA;
        this.historial = new ArrayList<>();

        registrarEvento("Solicitud registrada",
                solicitante.getId().valor(),
                null);
    }

    public void clasificar(TipoSolicitud tipo, Usuario funcionario) {
        verificarNoCerrada();
        validarFuncionario(funcionario);
        validarTransicion(EstadoSolicitud.CLASIFICADA);

        this.tipo = Objects.requireNonNull(tipo);
        this.estado = EstadoSolicitud.CLASIFICADA;

        registrarEvento("Solicitud clasificada",
                funcionario.getId().valor(),
                null);
    }

    public void priorizar(PrioridadSolicitud prioridad, Usuario funcionario) {
        verificarNoCerrada();
        validarFuncionario(funcionario);

        this.prioridad = Objects.requireNonNull(prioridad);

        registrarEvento("Solicitud priorizada",
                funcionario.getId().valor(),
                prioridad.justificacion());
    }

    public void atender(Usuario funcionario) {
        verificarNoCerrada();
        validarFuncionario(funcionario);
        validarTransicion(EstadoSolicitud.EN_ATENCION);

        this.estado = EstadoSolicitud.EN_ATENCION;

        registrarEvento("Solicitud en atención",
                funcionario.getId().valor(),
                null);
    }

    public void marcarAtendida(Usuario funcionario) {
        verificarNoCerrada();
        validarFuncionario(funcionario);
        validarTransicion(EstadoSolicitud.ATENDIDA);

        this.estado = EstadoSolicitud.ATENDIDA;

        registrarEvento("Solicitud atendida",
                funcionario.getId().valor(),
                null);
    }

    public void cerrar(Usuario funcionario, String observacion) {
        verificarNoCerrada();
        validarFuncionario(funcionario);

        if (estado != EstadoSolicitud.ATENDIDA) {
            throw new TransicionEstadoInvalidaException(
                    "Solo puede cerrarse si está atendida"
            );
        }

        this.estado = EstadoSolicitud.CERRADA;

        registrarEvento("Solicitud cerrada",
                funcionario.getId().valor(),
                observacion);
    }

    private void validarTransicion(EstadoSolicitud nuevoEstado) {

        if (estado == EstadoSolicitud.REGISTRADA &&
                nuevoEstado != EstadoSolicitud.CLASIFICADA) {
            throw new TransicionEstadoInvalidaException("Transición inválida");
        }

        if (estado == EstadoSolicitud.CLASIFICADA &&
                nuevoEstado != EstadoSolicitud.EN_ATENCION) {
            throw new TransicionEstadoInvalidaException("Transición inválida");
        }

        if (estado == EstadoSolicitud.EN_ATENCION &&
                nuevoEstado != EstadoSolicitud.ATENDIDA) {
            throw new TransicionEstadoInvalidaException("Transición inválida");
        }
    }

    private void verificarNoCerrada() {
        if (estado == EstadoSolicitud.CERRADA) {
            throw new SolicitudCerradaException();
        }
    }

    private void validarFuncionario(Usuario usuario) {

        Objects.requireNonNull(usuario, "El usuario es obligatorio");

        if (usuario.getTipo() != TipoUsuario.FUNCIONARIO) {
            throw new RolNoAutorizadoException(
                    "Solo un funcionario puede ejecutar esta acción"
            );
        }

        if (!usuario.estaActivo()) {
            throw new RolNoAutorizadoException(
                    "El funcionario debe estar activo"
            );
        }
    }

    private void registrarEvento(String accion,
                                 String responsable,
                                 String observacion) {

        historial.add(new EventoHistorial(
                accion,
                responsable,
                observacion
        ));
    }

    public List<EventoHistorial> getHistorial() {
        return Collections.unmodifiableList(historial);
    }
}