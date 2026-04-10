package co.edu.uniquindio.proyecto.infrastructure.mapper;

import co.edu.uniquindio.proyecto.application.dto.response.HistorialResponse;
import co.edu.uniquindio.proyecto.application.dto.response.SolicitudDetalleResponse;
import co.edu.uniquindio.proyecto.application.dto.response.SolicitudResumenResponse;
import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.Historial;
import co.edu.uniquindio.proyecto.domain.valueobject.PrioridadSolicitud;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SolicitudMapper {

    @Mapping(target = "codigo", source = "codigo.valor")
    @Mapping(target = "estudianteId", source = "estudianteId.valor")
    @Mapping(target = "canal", source = "canal")
    @Mapping(target = "tipo", source = "tipo")
    @Mapping(target = "descripcion", source = "descripcion.valor")
    @Mapping(target = "estado", source = "estado")
    @Mapping(target = "prioridad", source = "prioridad", qualifiedByName = "mapPrioridadDetalle")
    @Mapping(target = "historial", expression = "java(toHistorialResponseList(solicitud.obtenerHistorial()))")
    SolicitudDetalleResponse toDetalleResponse(Solicitud solicitud);

    @Mapping(target = "codigo", source = "codigo.valor")
    @Mapping(target = "estudianteId", source = "estudianteId.valor")
    @Mapping(target = "tipo", source = "tipo")
    @Mapping(target = "estado", source = "estado")
    @Mapping(target = "prioridad", source = "prioridad", qualifiedByName = "mapPrioridadResumen")
    SolicitudResumenResponse toResumenResponse(Solicitud solicitud);

    List<SolicitudResumenResponse> toResumenResponseList(List<Solicitud> solicitudes);

    HistorialResponse toHistorialResponse(Historial historial);

    List<HistorialResponse> toHistorialResponseList(List<Historial> historial);

    @Named("mapPrioridadDetalle")
    default String mapPrioridadDetalle(PrioridadSolicitud prioridad) {
        if (prioridad == null) {
            return null;
        }
        return prioridad.nivel().name() + " - " + prioridad.justificacion();
    }

    @Named("mapPrioridadResumen")
    default String mapPrioridadResumen(PrioridadSolicitud prioridad) {
        return prioridad == null ? null : prioridad.nivel().name();
    }
}
