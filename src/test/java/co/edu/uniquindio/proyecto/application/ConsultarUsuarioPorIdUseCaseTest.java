package co.edu.uniquindio.proyecto.application;

import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.Email;
import co.edu.uniquindio.proyecto.domain.valueobject.IdUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.TipoUsuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsultarUsuarioPorIdUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ConsultarUsuarioPorIdUseCase useCase;

    @Test
    void ejecutar_deberiaRetornarUsuarioCuandoExiste() {
        IdUsuario id = new IdUsuario("123456");
        Usuario usuario = new Usuario(
                id,
                "Ana Perez",
                new Email("ana@uq.edu.co"),
                TipoUsuario.ESTUDIANTE
        );

        when(usuarioRepository.buscarPorId(id)).thenReturn(usuario);

        Usuario resultado = useCase.ejecutar(id);

        assertEquals("123456", resultado.getId().valor());
        assertEquals("Ana Perez", resultado.getNombre());
        verify(usuarioRepository).buscarPorId(id);
    }

    @Test
    void ejecutar_deberiaLanzarExcepcionCuandoUsuarioNoExiste() {
        IdUsuario id = new IdUsuario("000000");

        when(usuarioRepository.buscarPorId(id))
                .thenThrow(new NoSuchElementException("Usuario no encontrado: 000000"));

        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> useCase.ejecutar(id)
        );

        assertEquals("Usuario no encontrado: 000000", exception.getMessage());
        verify(usuarioRepository).buscarPorId(id);
    }
}
