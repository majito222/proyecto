package co.edu.uniquindio.proyecto.domain.entity;

import co.edu.uniquindio.proyecto.domain.exception.*;
import co.edu.uniquindio.proyecto.domain.valueobject.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SolicitudTest {

    private Usuario crearEstudiante() {
        return new Usuario(
                IdUsuario.generar(),
                "Juan Perez",
                new Email("juan@test.com"),
                TipoUsuario.ESTUDIANTE
        );
    }

    private Usuario crearFuncionario() {
        return new Usuario(
                IdUsuario.generar(),
                "Admin",
                new Email("admin@test.com"),
                TipoUsuario.FUNCIONARIO
        );
    }

    private Solicitud crearSolicitudBase() {
        return new Solicitud(
                crearEstudiante(),
                TipoCanal.CORREO,
                new DescripcionSolicitud("Solicitud de prueba válida")
        );
    }

    @Test
    void debeIniciarEnEstadoRegistrada() {
        Solicitud solicitud = crearSolicitudBase();
        assertEquals(EstadoSolicitud.REGISTRADA, solicitud.getEstado());
    }

    @Test
    void debePermitirFlujoCompletoCorrecto() {

        Solicitud solicitud = crearSolicitudBase();
        Usuario funcionario = crearFuncionario();
        solicitud.clasificar(TipoSolicitud.REGISTRO_ASIGNATURA, funcionario);
        solicitud.atender(funcionario);
        solicitud.marcarAtendida(funcionario);
        solicitud.cerrar(funcionario, "Proceso finalizado");

        assertEquals(EstadoSolicitud.CERRADA, solicitud.getEstado());
    }


    @Test
    void noDebeCerrarSiNoEstaAtendida() {

        Solicitud solicitud = crearSolicitudBase();
        Usuario funcionario = crearFuncionario();

        solicitud.clasificar(TipoSolicitud.REGISTRO_ASIGNATURA, funcionario);

        assertThrows(TransicionEstadoInvalidaException.class,
                () -> solicitud.cerrar(funcionario, "Intento inválido"));
    }

    @Test
    void noDebePermitirModificarSolicitudCerrada() {

        Solicitud solicitud = crearSolicitudBase();
        Usuario funcionario = crearFuncionario();

        solicitud.clasificar(TipoSolicitud.REGISTRO_ASIGNATURA, funcionario);
        solicitud.atender(funcionario);
        solicitud.marcarAtendida(funcionario);
        solicitud.cerrar(funcionario, "Finalizada");

        assertThrows(SolicitudCerradaException.class,
                () -> solicitud.clasificar(TipoSolicitud.CANCELACION, funcionario));
    }

    @Test
    void debeRegistrarEventosEnHistorial() {

        Solicitud solicitud = crearSolicitudBase();
        Usuario funcionario = crearFuncionario();

        int eventosIniciales = solicitud.getHistorial().size();

        solicitud.clasificar(TipoSolicitud.REGISTRO_ASIGNATURA, funcionario);

        assertEquals(eventosIniciales + 1,
                solicitud.getHistorial().size());
    }

    @Test
    void historialNoDebeSerModificableExternamente() {

        Solicitud solicitud = crearSolicitudBase();

        assertThrows(UnsupportedOperationException.class,
                () -> solicitud.getHistorial().clear());
    }

    @Test
    void noDebePermitirClasificarSiEsEstudiante() {

        Solicitud solicitud = crearSolicitudBase();
        Usuario estudiante = crearEstudiante();

        assertThrows(RolNoAutorizadoException.class,
                () -> solicitud.clasificar(TipoSolicitud.REGISTRO_ASIGNATURA, estudiante));
    }

    @Test
    void noDebePermitirRegistrarSolicitudSiEsFuncionario() {

        Usuario funcionario = crearFuncionario();

        assertThrows(RolNoAutorizadoException.class,
                () -> new Solicitud(
                        funcionario,
                        TipoCanal.CORREO,
                        new DescripcionSolicitud("Solicitud inválida")
                ));
    }
}