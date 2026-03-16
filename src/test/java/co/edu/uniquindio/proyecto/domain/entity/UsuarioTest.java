package co.edu.uniquindio.proyecto.domain.entity;

import co.edu.uniquindio.proyecto.domain.valueobject.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    void usuarioSeCreaActivo() {
        // Regla: un usuario nuevo inicia en estado ACTIVO.
        Usuario usuario = estudianteActivo();

        assertTrue(usuario.estaActivo());
    }

    @Test
    void estudianteActivoPuedeRegistrarSolicitudes() {
        // Regla: solo un estudiante activo puede registrar solicitudes.
        Usuario usuario = estudianteActivo();

        assertTrue(usuario.puedeRegistrarSolicitudes());
    }

    @Test
    void usuarioNoEstudianteNoPuedeRegistrarSolicitudes() {
        // Regla: un usuario que no es estudiante no puede registrar solicitudes.
        Usuario usuario = funcionarioActivo();

        assertFalse(usuario.puedeRegistrarSolicitudes());
    }

    @Test
    void estudianteInactivoNoPuedeRegistrarSolicitudes() {
        // Regla: un estudiante inactivo no puede registrar solicitudes.
        Usuario usuario = estudianteActivo();
        usuario.desactivarUsuario();

        assertFalse(usuario.puedeRegistrarSolicitudes());
    }

    @Test
    void funcionarioActivoPuedeAtenderSolicitudes() {
        // Regla: solo un funcionario activo puede atender solicitudes.
        Usuario usuario = funcionarioActivo();

        assertTrue(usuario.puedeAtenderSolicitudes());
    }

    @Test
    void usuarioNoFuncionarioNoPuedeAtenderSolicitudes() {
        // Regla: un usuario que no es funcionario no puede atender solicitudes.
        Usuario usuario = administradorActivo();

        assertFalse(usuario.puedeAtenderSolicitudes());
    }

    @Test
    void funcionarioInactivoNoPuedeAtenderSolicitudes() {
        // Regla: un funcionario inactivo no puede atender solicitudes.
        Usuario usuario = funcionarioActivo();
        usuario.desactivarUsuario();

        assertFalse(usuario.puedeAtenderSolicitudes());
    }

    @Test
    void administradorActivoPuedeAdministrarSolicitudes() {
        // Regla: solo un administrador activo puede administrar solicitudes.
        Usuario usuario = administradorActivo();

        assertTrue(usuario.puedeAdministrarSolicitudes());
    }

    @Test
    void usuarioNoAdministradorNoPuedeAdministrarSolicitudes() {
        // Regla: un usuario que no es administrador no puede administrar solicitudes.
        Usuario usuario = estudianteActivo();

        assertFalse(usuario.puedeAdministrarSolicitudes());
    }

    @Test
    void administradorInactivoNoPuedeAdministrarSolicitudes() {
        // Regla: un administrador inactivo no puede administrar solicitudes.
        Usuario usuario = administradorActivo();
        usuario.desactivarUsuario();

        assertFalse(usuario.puedeAdministrarSolicitudes());
    }

    @Test
    void desactivarUsuarioCambiaEstadoAInactivo() {
        // Regla: desactivar cambia el estado a INACTIVO.
        Usuario usuario = estudianteActivo();

        usuario.desactivarUsuario();

        assertFalse(usuario.estaActivo());
    }

    @Test
    void activarUsuarioCambiaEstadoAActivo() {
        // Regla: activar cambia el estado a ACTIVO.
        Usuario usuario = estudianteActivo();
        usuario.desactivarUsuario();

        usuario.activarUsuario();

        assertTrue(usuario.estaActivo());
    }

    @Test
    void cambiarEmailConValorValidoDebeFuncionar() {
        // Regla: el email debe actualizarse con un valor valido.
        Usuario usuario = estudianteActivo();
        Email nuevoEmail = new Email("nuevo@uniquindio.edu.co");

        usuario.cambiarEmail(nuevoEmail);

        assertEquals(nuevoEmail, usuario.getEmail());
    }

    @Test
    void cambiarEmailConNullDebeFallar() {
        // Regla: no se permite asignar email nulo.
        Usuario usuario = estudianteActivo();

        assertThrows(NullPointerException.class, () -> usuario.cambiarEmail(null));
    }

    @Test
    void cambiarNombreConValorValidoDebeFuncionar() {
        // Regla: el nombre debe actualizarse con un valor valido.
        Usuario usuario = estudianteActivo();

        usuario.cambiarNombre("Nombre actualizado");

        assertEquals("Nombre actualizado", usuario.getNombre());
    }

    @Test
    void cambiarNombreConNullDebeFallar() {
        // Regla: no se permite asignar nombre nulo.
        Usuario usuario = estudianteActivo();

        assertThrows(NullPointerException.class, () -> usuario.cambiarNombre(null));
    }

    private Usuario estudianteActivo() {
        return new Usuario(
                new IdUsuario("123456"),
                "Juan Perez",
                new Email("juan.perez@uniquindio.edu.co"),
                TipoUsuario.ESTUDIANTE
        );
    }

    private Usuario funcionarioActivo() {
        return new Usuario(
                new IdUsuario("234567"),
                "Laura Ruiz",
                new Email("laura.ruiz@uniquindio.edu.co"),
                TipoUsuario.FUNCIONARIO
        );
    }

    private Usuario administradorActivo() {
        return new Usuario(
                new IdUsuario("345678"),
                "Carlos Diaz",
                new Email("carlos.diaz@uniquindio.edu.co"),
                TipoUsuario.ADMINISTRADOR
        );
    }
}
