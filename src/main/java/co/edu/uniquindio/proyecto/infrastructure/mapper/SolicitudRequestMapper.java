package co.edu.uniquindio.proyecto.infrastructure.mapper;

import co.edu.uniquindio.proyecto.domain.valueobject.*;
import co.edu.uniquindio.proyecto.infrastructure.dto.CrearSolicitudRequest;
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
            new IdUsuario(request.estudianteId()),
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
    private TipoSolicitud convertirTipo(CrearSolicitudRequest.TipoSolicitudDto tipo) {
        return TipoSolicitud.valueOf(tipo.name());
    }

    /**
     * Record interno para agrupar datos de solicitud.
     */
    public record SolicitudData(
        IdUsuario estudianteId,
        TipoCanal canal,
        TipoSolicitud tipo,
        DescripcionSolicitud descripcion
    ) {}
}
