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

@Service
@RequiredArgsConstructor
public class ConsultarSolicitudesAvanzadasUseCase {

    private final SolicitudRepository solicitudRepository;

    @Transactional(readOnly = true)
    public Page<Solicitud> buscarPorEstadoPrioridad(EstadoSolicitud estado, Pageable pageable) {
        return solicitudRepository.buscarPorEstadoPrioridad(estado, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Solicitud> buscarPorTexto(String texto, Pageable pageable) {
        return solicitudRepository.buscarPorTexto(texto, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Solicitud> buscarActivasPaginadas(Pageable pageable) {
        return solicitudRepository.buscarActivasPaginadas(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Solicitud> buscarSinAsignarAltaPrioridad(PrioridadSolicitud.Nivel nivel, Pageable pageable) {
        return solicitudRepository.buscarSinAsignarAltaPrioridad(nivel, pageable);
    }
}
