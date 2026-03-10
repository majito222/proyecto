package co.edu.uniquindio.proyecto.domain.entity;

import co.edu.uniquindio.proyecto.domain.valueobject.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    void debeCrearseUsuarioActivoPorDefecto() {
        // RN-12 (exito): el usuario se crea activo por defecto.
        Usuario usuario = nuevoUsuario(TipoUsuario.ESTUDIANTE);

        assertTrue(usuario.estaActivo());
    }

    @Test
    void noDebeEstarActivoSiSeDesactiva() {
        // RN-12 (falla): al desactivar, el usuario deja de estar activo.
        Usuario usuario = nuevoUsuario(TipoUsuario.ESTUDIANTE);

        usuario.desactivarUsuario();

        assertFalse(usuario.estaActivo());
    }

    @Test
    void debePermitirRegistrarSolicitudesSiEsEstudianteActivo() {
        // RN-14 (exito): estudiante activo puede registrar solicitudes.
        Usuario usuario = nuevoUsuario(TipoUsuario.ESTUDIANTE);

        assertTrue(usuario.puedeRegistrarSolicitudes());
    }

    @Test
    void noDebePermitirRegistrarSolicitudesSiNoEsEstudiante() {
        // RN-14 (falla): un rol distinto a ESTUDIANTE no puede registrar.
        Usuario usuario = nuevoUsuario(TipoUsuario.ADMINISTRADOR);

        assertFalse(usuario.puedeRegistrarSolicitudes());
    }

    @Test
    void debePermitirAtenderSolicitudesSiEsFuncionarioActivo() {
        // RN-13/RN-15 (exito): funcionario activo puede atender solicitudes.
        Usuario usuario = nuevoUsuario(TipoUsuario.FUNCIONARIO);

        assertTrue(usuario.puedeAtenderSolicitudes());
    }

    @Test
    void noDebePermitirAtenderSolicitudesSiNoEsFuncionario() {
        // RN-13/RN-15 (falla): un rol distinto a FUNCIONARIO no puede atender.
        Usuario usuario = nuevoUsuario(TipoUsuario.ADMINISTRADOR);

        assertFalse(usuario.puedeAtenderSolicitudes());
    }

    @Test
    void debePermitirAdministrarSolicitudesSiEsAdministradorActivo() {
        // RN-13/RN-15 (exito): administrador activo puede gestionar solicitudes.
        Usuario usuario = nuevoUsuario(TipoUsuario.ADMINISTRADOR);

        assertTrue(usuario.puedeAdministrarSolicitudes());
    }

    @Test
    void noDebePermitirAdministrarSolicitudesSiNoEsAdministrador() {
        // RN-13/RN-15 (falla): un rol distinto a ADMINISTRADOR no puede administrar.
        Usuario usuario = nuevoUsuario(TipoUsuario.FUNCIONARIO);

        assertFalse(usuario.puedeAdministrarSolicitudes());
    }

    @Test
    void debeAceptarEmailValido() {
        // RN-08 (exito): email con formato valido.
        assertDoesNotThrow(() -> new Email("juan@uniquindio.edu.co"));
    }

    @Test
    void noDebePermitirEmailInvalido() {
        // RN-08 (falla): email con formato invalido.
        assertThrows(IllegalArgumentException.class,
                () -> new Email("correo-invalido"));
    }

    @Test
    void debeAceptarIdNumerico() {
        // RN-09 (exito): id con solo numeros.
        assertDoesNotThrow(() -> new IdUsuario("123456"));
    }

    @Test
    void noDebePermitirIdNoNumerico() {
        // RN-09 (falla): id con caracteres no numericos.
        assertThrows(IllegalArgumentException.class,
                () -> new IdUsuario("ABC123"));
    }

    private static Usuario nuevoUsuario(TipoUsuario tipo) {
        return new Usuario(
                IdUsuario.generar(),
                "Juan Perez",
                new Email("juan@uniquindio.edu.co"),
                tipo
        );
    }
}
