package co.edu.uniquindio.proyecto.application;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        // GIVEN
        IdUsuario idMock = mock(IdUsuario.class);
        TipoCanal canalMock = mock(TipoCanal.class);
        TipoSolicitud tipoMock = mock(TipoSolicitud.class);
        DescripcionSolicitud descMock = mock(DescripcionSolicitud.class);

        Solicitud solicitudMock = mock(Solicitud.class);
        when(solicitudRepository.save(any())).thenReturn(solicitudMock);

        // WHEN
        Solicitud resultado = useCase.ejecutar(idMock, canalMock, tipoMock, descMock);

        // THEN
        assertNotNull(resultado);
        verify(solicitudRepository, times(1)).save(any(Solicitud.class));
    }
}