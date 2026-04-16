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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CerrarSolicitudUseCaseTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private CerrarSolicitudUseCase useCase;

    @Test
    void ejecutar_deberiaCerrarSolicitudCuandoExisteYEstadoEsValido() {
        CodigoSolicitud solicitudId = new CodigoSolicitud("SOL-100");
        IdUsuario administradorId = new IdUsuario("900001");
        Solicitud solicitud = crearSolicitudAtendida();
        Usuario administrador = crearAdministrador(administradorId);

        when(solicitudRepository.buscarPorCodigo(solicitudId)).thenReturn(solicitud);
        when(usuarioRepository.buscarPorId(administradorId)).thenReturn(administrador);
        when(solicitudRepository.guardar(solicitud)).thenReturn(solicitud);

        Solicitud resultado = useCase.ejecutar(solicitudId, administradorId, "Solicitud resuelta");

        assertEquals("CERRADA", resultado.getEstado().name());
        assertTrue(resultado.obtenerHistorial().stream()
                .anyMatch(evento -> evento.accion().contains("Solicitud cerrada")));
        verify(solicitudRepository).buscarPorCodigo(solicitudId);
        verify(usuarioRepository).buscarPorId(administradorId);
        verify(solicitudRepository).guardar(solicitud);
    }

    @Test
    void ejecutar_deberiaLanzarExcepcionCuandoAdministradorNoExiste() {
        CodigoSolicitud solicitudId = new CodigoSolicitud("SOL-101");
        IdUsuario administradorId = new IdUsuario("999999");
        Solicitud solicitud = crearSolicitudAtendida();

        when(solicitudRepository.buscarPorCodigo(solicitudId)).thenReturn(solicitud);
        when(usuarioRepository.buscarPorId(administradorId))
                .thenThrow(new NoSuchElementException("Usuario no encontrado: 999999"));

        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> useCase.ejecutar(solicitudId, administradorId, "Solicitud resuelta")
        );

        assertEquals("Usuario no encontrado: 999999", exception.getMessage());
        verify(solicitudRepository).buscarPorCodigo(solicitudId);
        verify(usuarioRepository).buscarPorId(administradorId);
        verify(solicitudRepository, never()).guardar(any());
    }

    private Solicitud crearSolicitudAtendida() {
        Solicitud solicitud = Solicitud.crear(
                new CodigoSolicitud("SOL-ATENDIDA"),
                new IdUsuario("123456"),
                "Estudiante Prueba",
                TipoCanal.CSU,
                TipoSolicitud.CONSULTA_ACADEMICA,
                new DescripcionSolicitud("Descripcion valida para una solicitud atendida.")
        );
        solicitud.clasificarSolicitud(TipoSolicitud.CONSULTA_ACADEMICA, new IdUsuario("700001"));
        solicitud.iniciarAtencion(new IdUsuario("700002"));
        solicitud.marcarAtendida(new IdUsuario("700002"));
        return solicitud;
    }

    private Usuario crearAdministrador(IdUsuario id) {
        return new Usuario(
                id,
                "Admin Prueba",
                new Email("admin@uq.edu.co"),
                TipoUsuario.ADMINISTRADOR
        );
    }
}
