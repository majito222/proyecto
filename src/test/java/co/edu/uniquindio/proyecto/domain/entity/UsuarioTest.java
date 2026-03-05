package co.edu.uniquindio.proyecto.domain.entity;

import co.edu.uniquindio.proyecto.domain.valueobject.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    void debeCrearseUsuarioActivoPorDefecto() {
        // Arrange
        Usuario usuario = new Usuario(
                IdUsuario.generar(),
                "Juan Pérez",
                new Email("juan@uniquindio.edu.co"),
                TipoUsuario.ESTUDIANTE
        );

        // Assert
        assertTrue(usuario.estaActivo());
    }
}