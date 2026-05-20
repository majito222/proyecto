package co.edu.uniquindio.proyecto.infrastructure.jpa;

import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.valueobject.Email;
import co.edu.uniquindio.proyecto.domain.valueobject.IdUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.TipoUsuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UsuarioJpaRepositoryImplTest {

    @Autowired
    private UsuarioJpaRepositoryImpl repository;

    @Test
    void deberiaGuardarYRecuperarUsuarioPorId() {
        Usuario usuario = new Usuario(
                new IdUsuario("888888"),
                "Ana Persistida",
                new Email("ana.persistida@uq.edu.co"),
                "$2a$10$abcdefghijklmnopqrstuv12345678901234567890123456789012",
                TipoUsuario.ESTUDIANTE
        );

        repository.save(usuario);

        Usuario recuperado = repository.buscarPorId(new IdUsuario("888888"));

        assertEquals("888888", recuperado.getId().valor());
        assertEquals("Ana Persistida", recuperado.getNombre());
        assertEquals("ana.persistida@uq.edu.co", recuperado.getEmail().valor());
    }

    @Test
    void deberiaEncontrarUsuarioPorEmail() {
        Usuario usuario = new Usuario(
                new IdUsuario("777777"),
                "Funcionario Persistido",
                new Email("func.persistido@uq.edu.co"),
                "$2a$10$abcdefghijklmnopqrstuv12345678901234567890123456789012",
                TipoUsuario.FUNCIONARIO
        );

        repository.save(usuario);

        assertTrue(repository.buscarPorEmail(new Email("func.persistido@uq.edu.co")).isPresent());
        assertEquals(
                TipoUsuario.FUNCIONARIO,
                repository.buscarPorEmail(new Email("func.persistido@uq.edu.co")).orElseThrow().getTipo()
        );
    }
}
