package co.edu.uniquindio.proyecto.application;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.EstadoSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.PrioridadSolicitud;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultarSolicitudesAvanzadasUseCase {

    private final SolicitudRepository solicitudRepository;

    @Transactional(readOnly = true)
    public List<Solicitud> buscarPorEstadoPrioridad(EstadoSolicitud estado) {
        return solicitudRepository.buscarPorEstadoPrioridad(estado);
    }

    @Transactional(readOnly = true)
    public List<Solicitud> buscarPorTexto(String texto) {
        return solicitudRepository.buscarPorTexto(texto);
    }

    @Transactional(readOnly = true)
    public Page<Solicitud> buscarActivasPaginadas(Pageable pageable) {
        return solicitudRepository.buscarActivasPaginadas(pageable);
    }

    @Transactional(readOnly = true)
    public List<Solicitud> buscarSinAsignarAltaPrioridad(PrioridadSolicitud.Nivel nivel) {
        return solicitudRepository.buscarSinAsignarAltaPrioridad(nivel);
    }
}
