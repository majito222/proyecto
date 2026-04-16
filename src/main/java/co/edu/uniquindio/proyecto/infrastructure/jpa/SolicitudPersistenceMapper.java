package co.edu.uniquindio.proyecto.infrastructure.jpa;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.CodigoSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.DescripcionSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.Historial;
import co.edu.uniquindio.proyecto.domain.valueobject.IdUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.PrioridadSolicitud;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class SolicitudPersistenceMapper {

    public SolicitudEntity toEntity(Solicitud domain) {
        SolicitudEntity entity = new SolicitudEntity();
        entity.setCodigo(domain.getCodigo().valor());
        entity.setSolicitanteId(domain.getEstudianteId().valor());
        entity.setEstudianteNombre(domain.getEstudianteNombre());
        entity.setCanalOrigen(domain.getCanal());
        entity.setTipo(domain.getTipo());
        entity.setDescripcion(domain.getDescripcion().valor());
        entity.setEstado(domain.getEstado());
        entity.setPrioridad(toEmbeddable(domain.getPrioridad()));
        entity.setResponsableId(domain.getResponsableId() == null ? null : domain.getResponsableId().valor());

        List<Historial> historial = domain.obtenerHistorial();
        LocalDateTime fechaCreacion = historial.isEmpty() ? LocalDateTime.now() : historial.getFirst().fecha();
        LocalDateTime fechaActualizacion = historial.isEmpty() ? fechaCreacion : historial.getLast().fecha();
        entity.setFechaCreacion(fechaCreacion);
        entity.setFechaActualizacion(fechaActualizacion);
        entity.setHistorial(historial.stream().map(this::toEmbeddable).toList());
        return entity;
    }

    public Solicitud toDomain(SolicitudEntity entity) {
        List<Historial> historial = entity.getHistorial() == null
                ? List.of()
                : entity.getHistorial().stream().map(this::toDomain).toList();

        return Solicitud.reconstituir(
                new CodigoSolicitud(entity.getCodigo()),
                new IdUsuario(entity.getSolicitanteId()),
                entity.getEstudianteNombre(),
                entity.getCanalOrigen(),
                entity.getTipo(),
                new DescripcionSolicitud(entity.getDescripcion()),
                entity.getEstado(),
                toDomain(entity.getPrioridad()),
                entity.getResponsableId() == null ? null : new IdUsuario(entity.getResponsableId()),
                historial
        );
    }

    private PrioridadEmbeddable toEmbeddable(PrioridadSolicitud prioridad) {
        if (prioridad == null) {
            return null;
        }

        return new PrioridadEmbeddable(prioridad.nivel(), prioridad.justificacion());
    }

    private PrioridadSolicitud toDomain(PrioridadEmbeddable prioridad) {
        if (prioridad == null) {
            return null;
        }

        return new PrioridadSolicitud(prioridad.getNivel(), prioridad.getJustificacion());
    }

    private EventoHistorialEmbeddable toEmbeddable(Historial historial) {
        return new EventoHistorialEmbeddable(
                historial.fecha(),
                historial.accion(),
                historial.responsable(),
                historial.observacion()
        );
    }

    private Historial toDomain(EventoHistorialEmbeddable historial) {
        return new Historial(
                historial.getAccion(),
                historial.getResponsable(),
                historial.getObservacion(),
                historial.getFechaHora()
        );
    }
}
