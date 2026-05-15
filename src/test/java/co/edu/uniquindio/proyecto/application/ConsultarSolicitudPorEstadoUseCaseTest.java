package co.edu.uniquindio.proyecto.application;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.CodigoSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.DescripcionSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.EstadoSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.IdUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.TipoCanal;
import co.edu.uniquindio.proyecto.domain.valueobject.TipoSolicitud;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultarSolicitudesPorEstadoUseCaseTest {

    @Mock private SolicitudRepository solicitudRepository;
    @InjectMocks private ConsultarSolicitudesPorEstadoUseCase useCase;

    @Test
    void ejecutar_deberiaRetornarSolicitudesPorEstado() {
        // GIVEN
        EstadoSolicitud estado = EstadoSolicitud.REGISTRADA;
        List<Solicitud> solicitudesMock = List.of(
                Solicitud.crear(
                        new CodigoSolicitud("SOL-910"),
                        new IdUsuario("123456"),
                        "Ana Perez",
                        TipoCanal.CSU,
                        TipoSolicitud.CONSULTA_ACADEMICA,
                        new DescripcionSolicitud("Descripcion valida para consultar solicitudes por estado.")
                )
        );
        when(solicitudRepository.buscarPorEstado(estado)).thenReturn(solicitudesMock);

        // WHEN
        List<Solicitud> resultado = useCase.ejecutar(estado);

        // THEN
        assertEquals(1, resultado.size());
        verify(solicitudRepository).buscarPorEstado(estado);
    }
}
