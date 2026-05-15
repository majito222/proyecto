package co.edu.uniquindio.proyecto.application;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.EstadoSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.TipoCanal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConsultarSolicitudesUseCase {

    private final SolicitudRepository solicitudRepository;

    @Transactional(readOnly = true)
    public Page<Solicitud> ejecutar(String estado, String canal, Pageable pageable) {
        EstadoSolicitud estadoFiltro = estado == null || estado.isBlank()
                ? null
                : EstadoSolicitud.valueOf(estado.toUpperCase());
        TipoCanal canalFiltro = canal == null || canal.isBlank()
                ? null
                : TipoCanal.valueOf(canal.toUpperCase());

        return solicitudRepository.buscarPorFiltros(estadoFiltro, canalFiltro, pageable);
    }
}
