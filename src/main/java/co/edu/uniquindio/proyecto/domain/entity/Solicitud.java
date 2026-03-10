package co.edu.uniquindio.proyecto.domain.entity;

import co.edu.uniquindio.proyecto.domain.valueobject.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Entidad de dominio que representa una solicitud academica.
 */
public class Solicitud {

    private final CodigoSolicitud codigo;
    private final IdUsuario estudianteId;
    private final TipoCanal canal;
    private final DescripcionSolicitud descripcion;
    private EstadoSolicitud estado;
    private TipoSolicitud tipo;
    private PrioridadSolicitud prioridad;

    private final List<Historial> historial = new ArrayList<>();

    /**
     * Crea una solicitud en estado REGISTRADA y registra el evento inicial.
     *
     * @param codigo codigo unico de la solicitud
     * @param estudianteId identificador del estudiante
     * @param canal canal por el que se registra la solicitud
     * @param tipo tipo de solicitud
     * @param descripcion descripcion inicial
     */
    public Solicitud(CodigoSolicitud codigo,
                     IdUsuario estudianteId,
                     TipoCanal canal,
                     TipoSolicitud tipo,
                     DescripcionSolicitud descripcion) {

        this.codigo = Objects.requireNonNull(codigo);
        this.estudianteId = Objects.requireNonNull(estudianteId);
        this.canal = Objects.requireNonNull(canal);
        this.tipo = Objects.requireNonNull(tipo);
        this.descripcion = Objects.requireNonNull(descripcion);

        this.estado = EstadoSolicitud.REGISTRADA;

        registrarEvento("Solicitud registrada");
    }

    /**
     * Clasifica la solicitud. Solo aplica si esta REGISTRADA.
     *
     * @param tipo tipo de solicitud asignado por el administrador
     * @param administradorId identificador del administrador
     */
    public void clasificarSolicitud(TipoSolicitud tipo, IdUsuario administradorId) {

        if (estado != EstadoSolicitud.REGISTRADA) {
            throw new IllegalStateException("Solo solicitudes registradas pueden clasificarse");
        }

        this.tipo = Objects.requireNonNull(tipo);
        this.estado = EstadoSolicitud.CLASIFICADA;

        registrarEvento("Solicitud clasificada por administrador " + administradorId);
    }

    /**
     * Asigna la prioridad. Solo aplica si esta CLASIFICADA.
     *
     * @param prioridad prioridad definida por el administrador
     * @param administradorId identificador del administrador
     */
    public void asignarPrioridad(PrioridadSolicitud prioridad, IdUsuario administradorId) {

        if (estado != EstadoSolicitud.CLASIFICADA) {
            throw new IllegalStateException("Solo solicitudes clasificadas pueden priorizarse");
        }

        this.prioridad = Objects.requireNonNull(prioridad);

        registrarEvento("Prioridad asignada por administrador " + administradorId);
    }

    /**
     * Inicia la atencion. Solo aplica si esta CLASIFICADA.
     *
     * @param funcionarioId identificador del funcionario
     */
    public void iniciarAtencion(IdUsuario funcionarioId) {

        if (estado != EstadoSolicitud.CLASIFICADA) {
            throw new IllegalStateException("Solo solicitudes clasificadas pueden ser atendidas");
        }

        this.estado = EstadoSolicitud.EN_ATENCION;

        registrarEvento("Atención iniciada por funcionario " + funcionarioId);
    }

    /**
     * Cierra la solicitud. Solo aplica si esta EN_ATENCION.
     *
     * @param administradorId identificador del administrador
     * @param observacion observacion de cierre
     */
    public void cerrarSolicitud(IdUsuario administradorId, String observacion) {

        if (estado != EstadoSolicitud.EN_ATENCION) {
            throw new IllegalStateException("Solo solicitudes en atención pueden cerrarse");
        }

        this.estado = EstadoSolicitud.CERRADA;

        registrarEvento("Solicitud cerrada por administrador " + administradorId + ". Observación: " + observacion);
    }

    /**
     * Registra un evento en el historial con marca de tiempo.
     *
     * @param descripcion descripcion del evento
     */
    private void registrarEvento(String descripcion) {
        historial.add(new Historial(descripcion, LocalDateTime.now()));
    }

}
