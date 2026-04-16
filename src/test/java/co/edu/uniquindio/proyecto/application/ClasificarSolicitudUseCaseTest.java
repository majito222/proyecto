package co.edu.uniquindio.proyecto.application;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClasificarSolicitudUseCaseTest {

    @Mock private SolicitudRepository solicitudRepository;
    @Mock private UsuarioRepository usuarioRepository;
    @InjectMocks private ClasificarSolicitudUseCase useCase;

    @Test
    void ejecutar_deberiaClasificarSolicitud() {
        // GIVEN
        CodigoSolicitud codigo = mock(CodigoSolicitud.class);
        IdUsuario funcionarioId = mock(IdUsuario.class);
        TipoSolicitud tipo = mock(TipoSolicitud.class);

        Solicitud solicitudMock = mock(Solicitud.class);
        Usuario funcionarioMock = mock(Usuario.class);

        when(solicitudRepository.buscarPorCodigo(codigo)).thenReturn(solicitudMock);
        when(usuarioRepository.buscarPorId(funcionarioId)).thenReturn(funcionarioMock);
        when(solicitudRepository.guardar(any())).thenReturn(solicitudMock);

        // WHEN
        Solicitud resultado = useCase.ejecutar(codigo, funcionarioId, tipo);

        // THEN
        assertNotNull(resultado);
        verify(solicitudMock).clasificarSolicitud(eq(tipo), any());
        verify(solicitudRepository).guardar(solicitudMock);
    }
}
