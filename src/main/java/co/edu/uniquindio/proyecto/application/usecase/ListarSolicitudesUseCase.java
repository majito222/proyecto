package co.edu.uniquindio.proyecto.application.usecase;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;

import java.util.List;

public class ListarSolicitudesUseCase {

    private final SolicitudRepository solicitudRepository;

    public ListarSolicitudesUseCase(SolicitudRepository solicitudRepository) {
        this.solicitudRepository = solicitudRepository;
    }

    public List<Solicitud> ejecutar() {
        return solicitudRepository.listarTodas();
    }
}
