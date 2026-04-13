package co.edu.uniquindio.proyecto.domain.entity;

import co.edu.uniquindio.proyecto.domain.exception.TransicionEstadoInvalidaException;
import co.edu.uniquindio.proyecto.domain.valueobject.*;
import lombok.Getter;

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

    @Getter
    private final CodigoSolicitud codigo;
    @Getter
    private final IdUsuario estudianteId;
    private final String estudianteNombre;
    private final TipoCanal canal;
    private final DescripcionSolicitud descripcion;
    private EstadoSolicitud estado;
    private TipoSolicitud tipo;
    private PrioridadSolicitud prioridad;
    private IdUsuario responsableId;

    private static final Comparator<Historial> HISTORIAL_POR_FECHA =
            Comparator.comparing(Historial::fecha);

    private final NavigableSet<Historial> historial = new TreeSet<>(HISTORIAL_POR_FECHA);

    // 🔥 MÉTODO ESTÁTICO AGREGADO (DDD correcto)
    public static Solicitud crear(
            CodigoSolicitud codigo,
            IdUsuario estudianteId,
            String estudianteNombre,
            TipoCanal canal,
            TipoSolicitud tipo,
            DescripcionSolicitud descripcion) {

        return new Solicitud(
                codigo,
                estudianteId,
                estudianteNombre,
                canal,
                tipo,
                descripcion
        );
    }

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

    public void clasificarSolicitud(TipoSolicitud tipo, IdUsuario administradorId) {

        if (estado != EstadoSolicitud.REGISTRADA) {
            throw new TransicionEstadoInvalidaException("Solo solicitudes registradas pueden clasificarse");
        }

        this.tipo = Objects.requireNonNull(tipo);
        this.estado = EstadoSolicitud.CLASIFICADA;

        registrarEvento("Solicitud clasificada por administrador " + administradorId);
    }

    public void asignarPrioridad(PrioridadSolicitud prioridad, IdUsuario administradorId) {

        if (estado != EstadoSolicitud.CLASIFICADA) {
            throw new TransicionEstadoInvalidaException("Solo solicitudes clasificadas pueden priorizarse");
        }

        this.prioridad = Objects.requireNonNull(prioridad);

        registrarEvento("Prioridad asignada por administrador " + administradorId);
    }

    public void asignarResponsable(IdUsuario responsableId) {

        if (estado != EstadoSolicitud.CLASIFICADA) {
            throw new TransicionEstadoInvalidaException("Solo solicitudes clasificadas pueden asignar responsable");
        }

        this.responsableId = Objects.requireNonNull(responsableId);

        registrarEvento("Responsable asignado: " + responsableId);
    }

    public void iniciarAtencion(IdUsuario funcionarioId) {

        if (estado != EstadoSolicitud.CLASIFICADA) {
            throw new TransicionEstadoInvalidaException("Solo solicitudes clasificadas pueden ser atendidas");
        }

        this.estado = EstadoSolicitud.EN_ATENCION;

        registrarEvento("Atencion iniciada por funcionario " + funcionarioId);
    }

    public void marcarAtendida(IdUsuario funcionarioId) {

        if (estado != EstadoSolicitud.EN_ATENCION) {
            throw new TransicionEstadoInvalidaException("Solo solicitudes en atencion pueden marcarse como atendidas");
        }

        this.estado = EstadoSolicitud.ATENDIDA;

        registrarEvento("Solicitud atendida por funcionario " + funcionarioId);
    }

    public void cerrarSolicitud(IdUsuario administradorId, String observacion) {

        if (estado != EstadoSolicitud.ATENDIDA) {
            throw new TransicionEstadoInvalidaException("Solo solicitudes atendidas pueden cerrarse");
        }

        this.estado = EstadoSolicitud.CERRADA;

        registrarEvento("Solicitud cerrada por administrador " + administradorId + ". Observacion: " + observacion);
    }

    public void cancelarSolicitud(IdUsuario responsableId, String observacion) {

        if (estado == EstadoSolicitud.CERRADA || estado == EstadoSolicitud.CANCELADA) {
            throw new TransicionEstadoInvalidaException("No es posible cancelar una solicitud cerrada o cancelada");
        }

        this.estado = EstadoSolicitud.CANCELADA;

        registrarEvento("Solicitud cancelada", responsableId.valor(), observacion);
    }

    public void registrarConsulta() {
        registrarEvento("Solicitud consultada");
    }

    public List<Historial> obtenerHistorial() {
        return List.copyOf(historial);
    }

    public String getEstudianteNombre() {
        return estudianteNombre;
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

    private void registrarEvento(String descripcion) {
        registrarEvento(descripcion, "SISTEMA", "");
    }

    private void registrarEvento(String descripcion, String responsable, String observacion) {
        LocalDateTime fecha = LocalDateTime.now();
        Historial evento = new Historial(descripcion, responsable, observacion, fecha);

        while (!historial.add(evento)) {
            fecha = fecha.plusNanos(1);
            evento = new Historial(descripcion, responsable, observacion, fecha);
        }
    }

    private static Historial marcadorHistorial(LocalDateTime fecha) {
        return new Historial("BUSQUEDA", "SISTEMA", "", fecha);
    }
}
