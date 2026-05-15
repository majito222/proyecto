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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CrearSolicitudUseCaseGuiaTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private CrearSolicitudUseCase useCase;

    @Test
    void ejecutar_deberiaCrearSolicitudCuandoEstudianteExiste() {
        IdUsuario estudianteId = new IdUsuario("123456");
        Usuario estudiante = new Usuario(
                estudianteId,
                "Ana Perez",
                new Email("ana@uq.edu.co"),
                TipoUsuario.ESTUDIANTE
        );
        DescripcionSolicitud descripcion =
                new DescripcionSolicitud("Descripcion valida para crear una solicitud nueva.");

        when(usuarioRepository.buscarPorId(estudianteId)).thenReturn(estudiante);
        when(solicitudRepository.guardar(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Solicitud resultado = useCase.ejecutar(
                estudianteId,
                TipoCanal.CSU,
                TipoSolicitud.CONSULTA_ACADEMICA,
                descripcion
        );

        ArgumentCaptor<Solicitud> captor = ArgumentCaptor.forClass(Solicitud.class);
        verify(usuarioRepository).buscarPorId(estudianteId);
        verify(solicitudRepository).guardar(captor.capture());
        assertEquals("123456", resultado.getEstudianteId().valor());
        assertEquals("REGISTRADA", resultado.getEstado().name());
        assertEquals("Ana Perez", captor.getValue().getEstudianteNombre());
        assertTrue(captor.getValue().getCodigo().valor() != null && !captor.getValue().getCodigo().valor().isBlank());
    }

    @Test
    void ejecutar_deberiaLanzarExcepcionCuandoEstudianteNoExiste() {
        IdUsuario estudianteId = new IdUsuario("654321");
        DescripcionSolicitud descripcion =
                new DescripcionSolicitud("Descripcion valida para intentar crear una solicitud.");

        when(usuarioRepository.buscarPorId(estudianteId))
                .thenThrow(new NoSuchElementException("Usuario no encontrado: 654321"));

        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> useCase.ejecutar(
                        estudianteId,
                        TipoCanal.CSU,
                        TipoSolicitud.CONSULTA_ACADEMICA,
                        descripcion
                )
        );

        assertEquals("Usuario no encontrado: 654321", exception.getMessage());
        verify(usuarioRepository).buscarPorId(estudianteId);
        verify(solicitudRepository, never()).guardar(any());
    }
}
