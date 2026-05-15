package co.edu.uniquindio.proyecto.infrastructure.jpa;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.CodigoSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.DescripcionSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.EstadoSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.IdUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.TipoCanal;
import co.edu.uniquindio.proyecto.domain.valueobject.TipoSolicitud;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class SolicitudJpaRepositoryImplTest {

    @Autowired
    private SolicitudJpaRepositoryImpl repository;

    @Test
    void deberiaGuardarYRecuperarSolicitudPorCodigo() {
        Solicitud solicitud = Solicitud.crear(
                new CodigoSolicitud("SOL-500"),
                new IdUsuario("123456"),
                "Juan Perez",
                TipoCanal.CSU,
                TipoSolicitud.CONSULTA_ACADEMICA,
                new DescripcionSolicitud("Descripcion valida para guardar en H2.")
        );

        repository.save(solicitud);
        Solicitud recuperada = repository.buscarPorCodigo(new CodigoSolicitud("SOL-500"));

        assertEquals("SOL-500", recuperada.getCodigo().valor());
        assertEquals("Juan Perez", recuperada.getEstudianteNombre());
        assertEquals(EstadoSolicitud.REGISTRADA, recuperada.getEstado());
        assertEquals(1, recuperada.obtenerHistorial().size());
    }

    @Test
    void deberiaEncontrarSolicitudesPorEstado() {
        Solicitud registrada = Solicitud.crear(
                new CodigoSolicitud("SOL-501"),
                new IdUsuario("123456"),
                "Juan Perez",
                TipoCanal.CSU,
                TipoSolicitud.CONSULTA_ACADEMICA,
                new DescripcionSolicitud("Descripcion valida de solicitud registrada.")
        );

        Solicitud clasificada = Solicitud.crear(
                new CodigoSolicitud("SOL-502"),
                new IdUsuario("123456"),
                "Juan Perez",
                TipoCanal.CORREO,
                TipoSolicitud.HOMOLOGACION,
                new DescripcionSolicitud("Descripcion valida de solicitud clasificada.")
        );
        clasificada.clasificarSolicitud(TipoSolicitud.HOMOLOGACION, new IdUsuario("654321"));

        repository.save(registrada);
        repository.save(clasificada);

        List<Solicitud> solicitudes = repository.findByEstado(EstadoSolicitud.REGISTRADA);

        assertTrue(solicitudes.stream().allMatch(s -> s.getEstado() == EstadoSolicitud.REGISTRADA));
        assertTrue(solicitudes.stream().anyMatch(s -> "SOL-501".equals(s.getCodigo().valor())));
    }
}
