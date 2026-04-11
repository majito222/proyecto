package co.edu.uniquindio.proyecto.application;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ListarSolicitudesUseCase {

    private final SolicitudRepository solicitudRepository;

    @Transactional(readOnly = true)
    public List<Solicitud> ejecutar() {
        return solicitudRepository.listarTodas();
    }
}