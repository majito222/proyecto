package co.edu.uniquindio.proyecto.domain.entity;

import co.edu.uniquindio.proyecto.domain.valueobject.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Solicitud {

    private final CodigoSolicitud codigo;
    private final IdUsuario estudianteId;
    private final TipoCanal canal;
    private final DescripcionSolicitud descripcion;
    private EstadoSolicitud estado;
    private TipoSolicitud tipo;
    private PrioridadSolicitud prioridad;

    private final List<Historial> historial = new ArrayList<>();

    public Solicitud(CodigoSolicitud codigo,
                     IdUsuario estudianteId,
                     TipoCanal canal,
                     DescripcionSolicitud descripcion) {

        this.codigo = Objects.requireNonNull(codigo);
        this.estudianteId = Objects.requireNonNull(estudianteId);
        this.canal = Objects.requireNonNull(canal);
        this.descripcion = Objects.requireNonNull(descripcion);

        this.estado = EstadoSolicitud.REGISTRADA;

        registrarEvento("Solicitud registrada");
    }

    public void clasificarSolicitud(TipoSolicitud tipo, IdUsuario administradorId) {

        if (estado != EstadoSolicitud.REGISTRADA) {
            throw new IllegalStateException("Solo solicitudes registradas pueden clasificarse");
        }

        this.tipo = Objects.requireNonNull(tipo);
        this.estado = EstadoSolicitud.CLASIFICADA;

        registrarEvento("Solicitud clasificada por administrador " + administradorId);
    }

    public void asignarPrioridad(PrioridadSolicitud prioridad, IdUsuario administradorId) {

        if (estado != EstadoSolicitud.CLASIFICADA) {
            throw new IllegalStateException("Solo solicitudes clasificadas pueden priorizarse");
        }

        this.prioridad = Objects.requireNonNull(prioridad);

        registrarEvento("Prioridad asignada por administrador " + administradorId);
    }

    public void iniciarAtencion(IdUsuario funcionarioId) {

        if (estado != EstadoSolicitud.CLASIFICADA) {
            throw new IllegalStateException("Solo solicitudes clasificadas pueden ser atendidas");
        }

        this.estado = EstadoSolicitud.EN_ATENCION;

        registrarEvento("Atención iniciada por funcionario " + funcionarioId);
    }

    public void cerrarSolicitud(IdUsuario administradorId, String observacion) {

        if (estado != EstadoSolicitud.EN_ATENCION) {
            throw new IllegalStateException("Solo solicitudes en atención pueden cerrarse");
        }

        this.estado = EstadoSolicitud.CERRADA;

        registrarEvento("Solicitud cerrada por administrador " + administradorId + ". Observación: " + observacion);
    }

    private void registrarEvento(String descripcion) {
        historial.add(new Historial(descripcion, LocalDateTime.now()));
    }

}