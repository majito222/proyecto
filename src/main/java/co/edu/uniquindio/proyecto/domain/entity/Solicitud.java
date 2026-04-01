package co.edu.uniquindio.proyecto.domain.entity;

import co.edu.uniquindio.proyecto.domain.valueobject.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.TreeSet;

/**
 * Entidad de dominio que representa una solicitud academica.
 */
public class Solicitud {

    private final CodigoSolicitud codigo;
    private final IdUsuario estudianteId;
    private final String estudianteNombre;
    private final TipoCanal canal;
    private final DescripcionSolicitud descripcion;
    private EstadoSolicitud estado;
    private TipoSolicitud tipo;
    private PrioridadSolicitud prioridad;

    private static final Comparator<Historial> HISTORIAL_POR_FECHA =
            Comparator.comparing(Historial::fecha);

    private final NavigableSet<Historial> historial = new TreeSet<>(HISTORIAL_POR_FECHA);

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
                     String estudianteNombre,
                     TipoCanal canal,
                     TipoSolicitud tipo,
                     DescripcionSolicitud descripcion) {

        this.codigo = Objects.requireNonNull(codigo);
        this.estudianteId = Objects.requireNonNull(estudianteId);
        this.estudianteNombre = Objects.requireNonNull(estudianteNombre);
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

        registrarEvento("Atencion iniciada por funcionario " + funcionarioId);
    }

    /**
     * Marca la solicitud como atendida. Solo aplica si esta EN_ATENCION.
     *
     * @param funcionarioId identificador del funcionario
     */
    public void marcarAtendida(IdUsuario funcionarioId) {

        if (estado != EstadoSolicitud.EN_ATENCION) {
            throw new IllegalStateException("Solo solicitudes en atencion pueden marcarse como atendidas");
        }

        this.estado = EstadoSolicitud.ATENDIDA;

        registrarEvento("Solicitud atendida por funcionario " + funcionarioId);
    }

    /**
     * Cierra la solicitud. Solo aplica si esta ATENDIDA.
     *
     * @param administradorId identificador del administrador
     * @param observacion observacion de cierre
     */
    public void cerrarSolicitud(IdUsuario administradorId, String observacion) {

        if (estado != EstadoSolicitud.ATENDIDA) {
            throw new IllegalStateException("Solo solicitudes atendidas pueden cerrarse");
        }

        this.estado = EstadoSolicitud.CERRADA;

        registrarEvento("Solicitud cerrada por administrador " + administradorId + ". Observacion: " + observacion);
    }

    /**
     * Expone el historial como coleccion inmodificable.
     *
     * @return lista inmodificable de eventos
     */
    public List<Historial> obtenerHistorial() {
        return List.copyOf(historial);
    }

    public IdUsuario getEstudianteId() {
        return estudianteId;
    }

    public String getEstudianteNombre() {
        return estudianteNombre;
    }

    public CodigoSolicitud getCodigo() {
        return codigo;
    }

    public TipoCanal getCanal() {
        return canal;
    }

    public DescripcionSolicitud getDescripcion() {
        return descripcion;
    }

    public EstadoSolicitud getEstado() {
        return estado;
    }

    public TipoSolicitud getTipo() {
        return tipo;
    }

    public PrioridadSolicitud getPrioridad() {
        return prioridad;
    }

    /**
     * Busca eventos del historial por rango de fechas.
     *
     * @param desde fecha inicial (incluyente)
     * @param hasta fecha final (incluyente)
     * @return lista inmodificable de eventos encontrados
     */
    public List<Historial> buscarHistorialEntre(LocalDateTime desde, LocalDateTime hasta) {
        Objects.requireNonNull(desde, "La fecha inicial no puede ser nula");
        Objects.requireNonNull(hasta, "La fecha final no puede ser nula");

        if (desde.isAfter(hasta)) {
            throw new IllegalArgumentException("La fecha inicial no puede ser posterior a la fecha final");
        }

        Historial inicio = marcadorHistorial(desde);
        Historial fin = marcadorHistorial(hasta);

        return List.copyOf(historial.subSet(inicio, true, fin, true));
    }

    /**
     * Registra un evento en el historial con marca de tiempo.
     *
     * @param descripcion descripcion del evento
     */
    private void registrarEvento(String descripcion) {
        LocalDateTime fecha = LocalDateTime.now();
        Historial evento = new Historial(descripcion, "SISTEMA", "", fecha);

        while (!historial.add(evento)) {
            fecha = fecha.plusNanos(1);
            evento = new Historial(descripcion, "SISTEMA", "", fecha);
        }
    }

    private static Historial marcadorHistorial(LocalDateTime fecha) {
        return new Historial("BUSQUEDA", "SISTEMA", "", fecha);
    }
}
