package co.edu.uniquindio.proyecto.infrastructure.rest.mapper;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.application.dto.response.SolicitudResponse;
import org.mapstruct.Mapper;
import java.util.List;
import org.mapstruct.Mapping; // Importante importar esto

@Mapper(componentModel = "spring")
public interface SolicitudMapper {

    // Aquí le decimos: "Del objeto solicitud, busca el campo descripcion
    // y de ese campo saca el atributo 'valor' (o el nombre que tenga en tu Value Object)"
    @Mapping(source = "descripcion.valor", target = "descripcion")
    SolicitudResponse toResponse(Solicitud solicitud);

    List<SolicitudResponse> toResponseList(List<Solicitud> solicitudes);
}