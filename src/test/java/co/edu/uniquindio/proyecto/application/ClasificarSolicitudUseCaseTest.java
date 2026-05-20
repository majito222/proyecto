package co.edu.uniquindio.proyecto.application;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.CodigoSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.DescripcionSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.Email;
import co.edu.uniquindio.proyecto.domain.valueobject.IdUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.TipoCanal;
import co.edu.uniquindio.proyecto.domain.valueobject.TipoSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.TipoUsuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClasificarSolicitudUseCaseTest {

    @Mock private SolicitudRepository solicitudRepository;
    @Mock private UsuarioRepository usuarioRepository;
    @InjectMocks private ClasificarSolicitudUseCase useCase;

    @Test
    void ejecutar_deberiaClasificarSolicitud() {
        CodigoSolicitud codigo = new CodigoSolicitud("SOL-900");
        IdUsuario funcionarioId = new IdUsuario("700001");
        TipoSolicitud tipo = TipoSolicitud.HOMOLOGACION;

        Solicitud solicitudMock = Solicitud.crear(
                codigo,
                new IdUsuario("123456"),
                "Ana Perez",
                TipoCanal.CSU,
                TipoSolicitud.CONSULTA_ACADEMICA,
                new DescripcionSolicitud("Descripcion valida para clasificar una solicitud de prueba.")
        );
        Usuario funcionarioMock = new Usuario(
                funcionarioId,
                "Funcionario",
                new Email("funcionario@uq.edu.co"),
                TipoUsuario.FUNCIONARIO
        );

        when(solicitudRepository.buscarPorCodigo(codigo)).thenReturn(solicitudMock);
        when(usuarioRepository.buscarPorId(funcionarioId)).thenReturn(funcionarioMock);
        when(solicitudRepository.guardar(solicitudMock)).thenReturn(solicitudMock);

        Solicitud resultado = useCase.ejecutar(codigo, funcionarioId, tipo);

        assertNotNull(resultado);
        assertEquals("CLASIFICADA", resultado.getEstado().name());
        assertEquals(TipoSolicitud.HOMOLOGACION, resultado.getTipo());
        verify(solicitudRepository).guardar(solicitudMock);
    }
}
