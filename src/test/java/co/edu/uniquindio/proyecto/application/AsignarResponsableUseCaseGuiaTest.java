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
class AsignarResponsableUseCaseGuiaTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private AsignarResponsableUseCase useCase;

    @Test
    void ejecutar_deberiaAsignarResponsableCuandoSolicitudYFuncionarioExisten() {
        CodigoSolicitud solicitudId = new CodigoSolicitud("SOL-200");
        IdUsuario funcionarioId = new IdUsuario("800001");
        Solicitud solicitud = crearSolicitudClasificada();
        Usuario funcionario = new Usuario(
                funcionarioId,
                "Funcionario Prueba",
                new Email("funcionario@uq.edu.co"),
                TipoUsuario.FUNCIONARIO
        );

        when(solicitudRepository.buscarPorCodigo(solicitudId)).thenReturn(solicitud);
        when(usuarioRepository.buscarPorId(funcionarioId)).thenReturn(funcionario);
        when(solicitudRepository.save(solicitud)).thenReturn(solicitud);

        Solicitud resultado = useCase.ejecutar(solicitudId, funcionarioId);

        assertEquals(3, resultado.obtenerHistorial().size());
        verify(solicitudRepository).buscarPorCodigo(solicitudId);
        verify(usuarioRepository).buscarPorId(funcionarioId);
        verify(solicitudRepository).save(solicitud);
    }

    @Test
    void ejecutar_deberiaLanzarExcepcionCuandoFuncionarioNoExiste() {
        CodigoSolicitud solicitudId = new CodigoSolicitud("SOL-201");
        IdUsuario funcionarioId = new IdUsuario("899999");
        Solicitud solicitud = crearSolicitudClasificada();

        when(solicitudRepository.buscarPorCodigo(solicitudId)).thenReturn(solicitud);
        when(usuarioRepository.buscarPorId(funcionarioId))
                .thenThrow(new NoSuchElementException("Usuario no encontrado: 899999"));

        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> useCase.ejecutar(solicitudId, funcionarioId)
        );

        assertEquals("Usuario no encontrado: 899999", exception.getMessage());
        verify(solicitudRepository).buscarPorCodigo(solicitudId);
        verify(usuarioRepository).buscarPorId(funcionarioId);
        verify(solicitudRepository, never()).save(any());
    }

    private Solicitud crearSolicitudClasificada() {
        Solicitud solicitud = Solicitud.crear(
                new CodigoSolicitud("SOL-CLASIFICADA"),
                new IdUsuario("123456"),
                "Estudiante Prueba",
                TipoCanal.CSU,
                TipoSolicitud.CONSULTA_ACADEMICA,
                new DescripcionSolicitud("Descripcion valida para una solicitud clasificada.")
        );
        solicitud.clasificarSolicitud(TipoSolicitud.CONSULTA_ACADEMICA, new IdUsuario("700001"));
        return solicitud;
    }
}
