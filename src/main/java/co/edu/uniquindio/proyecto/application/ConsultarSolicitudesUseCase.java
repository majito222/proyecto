package co.edu.uniquindio.proyecto.application;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.EstadoSolicitud;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultarSolicitudesUseCase {

    private final ListarSolicitudesUseCase listarSolicitudesUseCase;
    private final ConsultarSolicitudesPorEstadoUseCase consultarSolicitudesPorEstadoUseCase;

    @Transactional(readOnly = true)
    public List<Solicitud> ejecutar(String estado) {
        if (estado == null || estado.isBlank()) {
            return listarSolicitudesUseCase.ejecutar();
        }

        return consultarSolicitudesPorEstadoUseCase.ejecutar(
                EstadoSolicitud.valueOf(estado.toUpperCase())
        );
    }
}