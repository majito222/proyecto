
package co.edu.uniquindio.proyecto.infrastructure.jpa;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import org.springframework.stereotype.Component;

/**
 * Mapper básico - SOLO lo esencial para compilar
 */
@Component
public class SolicitudPersistenceMapper {

    public SolicitudEntity toEntity(Solicitud domain) {
        SolicitudEntity entity = new SolicitudEntity();
        entity.setCodigo(domain.getCodigo().toString());
        entity.setDescripcion(domain.getDescripcion().toString());
        entity.setEstado(SolicitudEntity.EstadoSolicitudEnum.REGISTRADA); // Default
        return entity;
    }

    public Solicitud toDomain(SolicitudEntity entity) {
        // Retorna null temporal
        return null;
    }
}