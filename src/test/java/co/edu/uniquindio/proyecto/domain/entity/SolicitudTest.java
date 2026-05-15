package co.edu.uniquindio.proyecto.domain.entity;

import co.edu.uniquindio.proyecto.domain.valueobject.*;
import co.edu.uniquindio.proyecto.domain.service.SolicitudService;
import co.edu.uniquindio.proyecto.domain.exception.TransicionEstadoInvalidaException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SolicitudTest {

    @Test
    void crearSolicitudRegistraEventoInicial() {
        // Regla: una solicitud nueva se registra con un evento inicial.
        Solicitud solicitud = solicitudBase();

        assertEquals(1, solicitud.obtenerHistorial().size());
        assertEquals("Solicitud registrada", solicitud.obtenerHistorial().get(0).accion());
    }

    @Test
    void clasificarSolicitudCuandoEstaRegistradaDebeFuncionar() {
        // Regla: solo solicitudes REGISTRADAS pueden clasificarse.
        Solicitud solicitud = solicitudBase();

        assertDoesNotThrow(() ->
                solicitud.clasificarSolicitud(TipoSolicitud.HOMOLOGACION, idAdministrador()));
        assertEquals(2, solicitud.obtenerHistorial().size());
    }

    @Test
    void clasificarSolicitudCuandoNoEstaRegistradaDebeFallar() {
        // Regla: no se puede clasificar una solicitud que ya no esta REGISTRADA.
        Solicitud solicitud = solicitudBase();
        solicitud.clasificarSolicitud(TipoSolicitud.HOMOLOGACION, idAdministrador());

        assertThrows(TransicionEstadoInvalidaException.class, () ->
                solicitud.clasificarSolicitud(TipoSolicitud.CANCELACION, idAdministrador()));
    }

    @Test
    void asignarPrioridadCuandoClasificadaDebeFuncionar() {
        // Regla: solo solicitudes CLASIFICADAS pueden priorizarse.
        Solicitud solicitud = solicitudBase();
        solicitud.clasificarSolicitud(TipoSolicitud.HOMOLOGACION, idAdministrador());

        assertDoesNotThrow(() ->
                solicitud.asignarPrioridad(prioridadAlta(), idAdministrador()));
        assertEquals(3, solicitud.obtenerHistorial().size());
    }

    @Test
    void asignarPrioridadCuandoNoClasificadaDebeFallar() {
        // Regla: no se puede priorizar una solicitud sin clasificar.
        Solicitud solicitud = solicitudBase();

        assertThrows(TransicionEstadoInvalidaException.class, () ->
                solicitud.asignarPrioridad(prioridadAlta(), idAdministrador()));
    }

    @Test
    void iniciarAtencionCuandoClasificadaDebeFuncionar() {
        // Regla: solo solicitudes CLASIFICADAS pueden entrar en atencion.
        Solicitud solicitud = solicitudBase();
        solicitud.clasificarSolicitud(TipoSolicitud.CONSULTA_ACADEMICA, idAdministrador());

        assertDoesNotThrow(() ->
                solicitud.iniciarAtencion(idFuncionario()));
        assertEquals(3, solicitud.obtenerHistorial().size());
    }

    @Test
    void iniciarAtencionCuandoNoClasificadaDebeFallar() {
        // Regla: no se puede iniciar atencion si no esta CLASIFICADA.
        Solicitud solicitud = solicitudBase();

        assertThrows(TransicionEstadoInvalidaException.class, () ->
                solicitud.iniciarAtencion(idFuncionario()));
    }

    @Test
    void marcarAtendidaCuandoEnAtencionDebeFuncionar() {
        // Regla: solo solicitudes EN_ATENCION pueden marcarse atendidas.
        Solicitud solicitud = solicitudBase();
        solicitud.clasificarSolicitud(TipoSolicitud.CONSULTA_ACADEMICA, idAdministrador());
        solicitud.iniciarAtencion(idFuncionario());

        assertDoesNotThrow(() ->
                solicitud.marcarAtendida(idFuncionario()));
        assertEquals(4, solicitud.obtenerHistorial().size());
    }

    @Test
    void marcarAtendidaCuandoNoEnAtencionDebeFallar() {
        // Regla: no se puede marcar atendida si no esta EN_ATENCION.
        Solicitud solicitud = solicitudBase();

        assertThrows(TransicionEstadoInvalidaException.class, () ->
                solicitud.marcarAtendida(idFuncionario()));
    }

    @Test
    void cerrarSolicitudCuandoAtendidaDebeFuncionar() {
        // Regla: solo solicitudes ATENDIDAS pueden cerrarse.
        Solicitud solicitud = solicitudBase();
        solicitud.clasificarSolicitud(TipoSolicitud.CONSULTA_ACADEMICA, idAdministrador());
        solicitud.iniciarAtencion(idFuncionario());
        solicitud.marcarAtendida(idFuncionario());

        assertDoesNotThrow(() ->
                solicitud.cerrarSolicitud(idAdministrador(), "Cierre con observacion"));
        assertEquals(5, solicitud.obtenerHistorial().size());
    }

    @Test
    void cerrarSolicitudCuandoNoAtendidaDebeFallar() {
        // Regla: no se puede cerrar si no esta ATENDIDA.
        Solicitud solicitud = solicitudBase();

        assertThrows(TransicionEstadoInvalidaException.class, () ->
                solicitud.cerrarSolicitud(idAdministrador(), "Cierre invalido"));
    }

    @Test
    void registrarConsultaDebeAgregarEventoConFecha() {
        Solicitud solicitud = solicitudBase();

        solicitud.registrarConsulta();

        assertEquals(2, solicitud.obtenerHistorial().size());
        Historial evento = solicitud.obtenerHistorial().get(1);
        assertEquals("Solicitud consultada", evento.accion());
        assertNotNull(evento.fecha());
    }

    @Test
    void cancelarSolicitudDebeCambiarEstadoYRegistrarEvento() {
        Solicitud solicitud = solicitudBase();

        solicitud.cancelarSolicitud(idAdministrador(), "Cancelada por solicitud del estudiante");

        assertEquals(EstadoSolicitud.CANCELADA, solicitud.getEstado());
        assertEquals(2, solicitud.obtenerHistorial().size());
        Historial evento = solicitud.obtenerHistorial().get(1);
        assertEquals("Solicitud cancelada", evento.accion());
        assertEquals("654321", evento.responsable());
        assertEquals("Cancelada por solicitud del estudiante", evento.observacion());
        assertNotNull(evento.fecha());
    }

    @Test
    void buscarHistorialPorEstudianteDebeRetornarOrdenCronologico() {
        // Regla: el historial combinado debe venir ordenado cronologicamente.
        Solicitud solicitud = solicitudBase();
        solicitud.clasificarSolicitud(TipoSolicitud.HOMOLOGACION, idAdministrador());
        solicitud.asignarPrioridad(prioridadAlta(), idAdministrador());

        SolicitudService service = new SolicitudService();

        List<Historial> historial = service.buscarHistorialPorEstudianteId(
                List.of(solicitud),
                solicitud.getEstudianteId()
        );

        assertEquals(3, historial.size());
        assertTrue(historial.get(0).fecha().isBefore(historial.get(1).fecha())
                || historial.get(0).fecha().isEqual(historial.get(1).fecha()));
        assertTrue(historial.get(1).fecha().isBefore(historial.get(2).fecha())
                || historial.get(1).fecha().isEqual(historial.get(2).fecha()));
    }

    private Solicitud solicitudBase() {
        return new Solicitud(
                new CodigoSolicitud("SOL-001"),
                new IdUsuario("123456"),
                "Estudiante Prueba",
                TipoCanal.CSU,
                TipoSolicitud.REGISTRO_ASIGNATURA,
                new DescripcionSolicitud("Descripcion valida")
        );
    }

    private IdUsuario idAdministrador() {
        return new IdUsuario("654321");
    }

    private IdUsuario idFuncionario() {
        return new IdUsuario("765432");
    }

    private PrioridadSolicitud prioridadAlta() {
        return new PrioridadSolicitud(PrioridadSolicitud.Nivel.ALTA, "Justificacion valida");
    }
}
