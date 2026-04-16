package co.edu.uniquindio.proyecto.application;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CrearSolicitudUseCaseTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private CrearSolicitudUseCase useCase;

    @Test
    void ejecutar_deberiaCrearSolicitudYGuardar() {
        IdUsuario id = new IdUsuario("123456");
        Usuario usuario = new Usuario(id, "Ana Perez", new Email("ana@uq.edu.co"), TipoUsuario.ESTUDIANTE);
        DescripcionSolicitud descripcion =
                new DescripcionSolicitud("Descripcion valida para crear una solicitud de prueba.");

        Solicitud solicitudMock = org.mockito.Mockito.mock(Solicitud.class);
        when(usuarioRepository.buscarPorId(id)).thenReturn(usuario);
        when(solicitudRepository.guardar(any())).thenReturn(solicitudMock);

        Solicitud resultado = useCase.ejecutar(
                id,
                TipoCanal.CSU,
                TipoSolicitud.CONSULTA_ACADEMICA,
                descripcion
        );

        assertNotNull(resultado);
        verify(usuarioRepository, times(1)).buscarPorId(id);
        verify(solicitudRepository, times(1)).guardar(any(Solicitud.class));
    }
}
