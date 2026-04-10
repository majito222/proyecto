package co.edu.uniquindio.proyecto.application.usecase;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.EstadoSolicitud;

import java.util.List;

public class ConsultarSolicitudesUseCase {

    private final ListarSolicitudesUseCase listarSolicitudesUseCase;
    private final ConsultarSolicitudesPorEstadoUseCase consultarSolicitudesPorEstadoUseCase;

    public ConsultarSolicitudesUseCase(
            ListarSolicitudesUseCase listarSolicitudesUseCase,
            ConsultarSolicitudesPorEstadoUseCase consultarSolicitudesPorEstadoUseCase) {
        this.listarSolicitudesUseCase = listarSolicitudesUseCase;
        this.consultarSolicitudesPorEstadoUseCase = consultarSolicitudesPorEstadoUseCase;
    }

    public List<Solicitud> ejecutar(String estado) {
        if (estado == null || estado.isBlank()) {
            return listarSolicitudesUseCase.ejecutar();
        }

        return consultarSolicitudesPorEstadoUseCase.ejecutar(
                EstadoSolicitud.valueOf(estado.toUpperCase())
        );
    }
}
