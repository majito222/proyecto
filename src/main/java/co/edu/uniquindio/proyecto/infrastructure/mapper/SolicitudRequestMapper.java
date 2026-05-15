package co.edu.uniquindio.proyecto.infrastructure.mapper;

import co.edu.uniquindio.proyecto.application.dto.request.CrearSolicitudRequest;
import co.edu.uniquindio.proyecto.domain.valueobject.*;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir DTOs de solicitud a value objects del dominio.
 */
@Component
public class SolicitudRequestMapper {

    /**
     * Convierte CrearSolicitudRequest a value objects del dominio.
     */
    public SolicitudData toDomainData(CrearSolicitudRequest request) {
        return new SolicitudData(
            convertirCanal(request.canal()),
            convertirTipo(request.tipo()),
            new DescripcionSolicitud(request.descripcion())
        );
    }

    /**
     * Convierte canal de API a dominio.
     */
    private TipoCanal convertirCanal(CrearSolicitudRequest.CanalSolicitud canal) {
        return TipoCanal.valueOf(canal.name());
    }

    /**
     * Convierte tipo de API a dominio.
     */
    /**
     * Convierte tipo de API a dominio.
     * Quitamos el "Dto" del final del parámetro.
     */
    private TipoSolicitud convertirTipo(CrearSolicitudRequest.TipoSolicitud tipo) {
        return TipoSolicitud.valueOf(tipo.name());
    }

    /**
     * Record interno para agrupar datos de solicitud.
     */
    public record SolicitudData(
        TipoCanal canal,
        TipoSolicitud tipo,
        DescripcionSolicitud descripcion
    ) {}
}
