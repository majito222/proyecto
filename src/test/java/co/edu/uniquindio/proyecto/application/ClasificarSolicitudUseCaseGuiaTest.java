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

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClasificarSolicitudUseCaseGuiaTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ClasificarSolicitudUseCase useCase;

    @Test
    void ejecutar_deberiaClasificarSolicitudCuandoSolicitudYFuncionarioExisten() {
        CodigoSolicitud solicitudId = new CodigoSolicitud("SOL-300");
        IdUsuario funcionarioId = new IdUsuario("810001");
        Solicitud solicitud = Solicitud.crear(
                solicitudId,
                new IdUsuario("123456"),
                "Estudiante Prueba",
                TipoCanal.CSU,
                TipoSolicitud.REGISTRO_ASIGNATURA,
                new DescripcionSolicitud("Descripcion valida para una solicitud registrada.")
        );
        Usuario funcionario = new Usuario(
                funcionarioId,
                "Funcionario Clasificador",
                new Email("clasificador@uq.edu.co"),
                TipoUsuario.FUNCIONARIO
        );

        when(solicitudRepository.buscarPorCodigo(solicitudId)).thenReturn(solicitud);
        when(usuarioRepository.buscarPorId(funcionarioId)).thenReturn(funcionario);
        when(solicitudRepository.save(solicitud)).thenReturn(solicitud);

        Solicitud resultado = useCase.ejecutar(
                solicitudId,
                funcionarioId,
                TipoSolicitud.HOMOLOGACION
        );

        assertEquals("CLASIFICADA", resultado.getEstado().name());
        assertEquals(TipoSolicitud.HOMOLOGACION, resultado.getTipo());
        verify(solicitudRepository).buscarPorCodigo(solicitudId);
        verify(usuarioRepository).buscarPorId(funcionarioId);
        verify(solicitudRepository).save(solicitud);
    }

    @Test
    void ejecutar_deberiaLanzarExcepcionCuandoSolicitudNoExiste() {
        CodigoSolicitud solicitudId = new CodigoSolicitud("SOL-301");
        IdUsuario funcionarioId = new IdUsuario("810002");

        when(solicitudRepository.buscarPorCodigo(solicitudId))
                .thenThrow(new NoSuchElementException("Solicitud no encontrada: SOL-301"));

        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> useCase.ejecutar(solicitudId, funcionarioId, TipoSolicitud.HOMOLOGACION)
        );

        assertEquals("Solicitud no encontrada: SOL-301", exception.getMessage());
        verify(solicitudRepository).buscarPorCodigo(solicitudId);
        verify(usuarioRepository, never()).buscarPorId(any());
        verify(solicitudRepository, never()).save(any());
    }
}
