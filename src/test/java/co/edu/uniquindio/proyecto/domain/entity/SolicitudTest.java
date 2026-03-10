package co.edu.uniquindio.proyecto.domain.entity;

import co.edu.uniquindio.proyecto.domain.valueobject.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SolicitudTest {

    @Test
    void debeCrearSolicitudEnEstadoRegistradaYRegistrarHistorial() {
        // RN-01 (exito): al crear la solicitud queda REGISTRADA y se agrega un evento al historial.
        Solicitud solicitud = nuevaSolicitud();

        EstadoSolicitud estado = leerCampo(solicitud, "estado", EstadoSolicitud.class);
        List<?> historial = leerCampo(solicitud, "historial", List.class);

        assertEquals(EstadoSolicitud.REGISTRADA, estado);
        assertEquals(1, historial.size());
    }

    @Test
    void noDebeCrearSolicitudConDatosNulos() {
        // RN-18 (falla): codigo no puede ser nulo.
        assertThrows(NullPointerException.class,
                () -> new Solicitud(
                        null,
                        new IdUsuario("123456"),
                        TipoCanal.SAC,
                        TipoSolicitud.CONSULTA_ACADEMICA,
                        new DescripcionSolicitud("Solicitud valida de prueba")
                ));
    }

    @Test
    void debeCrearSolicitudConTipoYCanal() {
        // RN-18 (exito): tipo y canal definidos.
        Solicitud solicitud = new Solicitud(
                CodigoSolicitud.generar(),
                new IdUsuario("123456"),
                TipoCanal.CORREO,
                TipoSolicitud.HOMOLOGACION,
                new DescripcionSolicitud("Solicitud valida de prueba")
        );

        EstadoSolicitud estado = leerCampo(solicitud, "estado", EstadoSolicitud.class);
        assertEquals(EstadoSolicitud.REGISTRADA, estado);
    }

    @Test
    void noDebeCrearSolicitudSiCanalEsNulo() {
        // RN-18 (falla): canal obligatorio.
        assertThrows(NullPointerException.class,
                () -> new Solicitud(
                        CodigoSolicitud.generar(),
                        new IdUsuario("123456"),
                        null,
                        TipoSolicitud.CONSULTA_ACADEMICA,
                        new DescripcionSolicitud("Solicitud valida de prueba")
                ));
    }

    @Test
    void noDebeCrearSolicitudSiTipoEsNulo() {
        // RN-18 (falla): tipo obligatorio.
        assertThrows(NullPointerException.class,
                () -> new Solicitud(
                        CodigoSolicitud.generar(),
                        new IdUsuario("123456"),
                        TipoCanal.SAC,
                        null,
                        new DescripcionSolicitud("Solicitud valida de prueba")
                ));
    }

    @Test
    void debeClasificarSolicitudSiEstaRegistrada() {
        // RN-02 (exito): REGISTRADA -> CLASIFICADA.
        Solicitud solicitud = nuevaSolicitud();

        solicitud.clasificarSolicitud(
                TipoSolicitud.CANCELACION,
                new IdUsuario("654321")
        );

        EstadoSolicitud estado = leerCampo(solicitud, "estado", EstadoSolicitud.class);
        TipoSolicitud tipo = leerCampo(solicitud, "tipo", TipoSolicitud.class);
        List<?> historial = leerCampo(solicitud, "historial", List.class);

        assertEquals(EstadoSolicitud.CLASIFICADA, estado);
        assertEquals(TipoSolicitud.CANCELACION, tipo);
        assertEquals(2, historial.size());
    }

    @Test
    void noDebeClasificarSiNoEstaRegistrada() {
        // RN-02 (falla): no se puede clasificar dos veces.
        Solicitud solicitud = nuevaSolicitud();

        solicitud.clasificarSolicitud(
                TipoSolicitud.CONSULTA_ACADEMICA,
                new IdUsuario("654321")
        );

        assertThrows(IllegalStateException.class,
                () -> solicitud.clasificarSolicitud(
                        TipoSolicitud.HOMOLOGACION,
                        new IdUsuario("654321")
                ));
    }

    @Test
    void debeAsignarPrioridadSiEstaClasificada() {
        // RN-02 (exito): con clasificacion previa, se puede priorizar.
        Solicitud solicitud = nuevaSolicitud();

        solicitud.clasificarSolicitud(
                TipoSolicitud.REGISTRO_ASIGNATURA,
                new IdUsuario("654321")
        );

        solicitud.asignarPrioridad(
                new PrioridadSolicitud(
                        PrioridadSolicitud.Nivel.ALTA,
                        "Impacto alto"
                ),
                new IdUsuario("654321")
        );

        PrioridadSolicitud prioridad = leerCampo(
                solicitud,
                "prioridad",
                PrioridadSolicitud.class
        );
        EstadoSolicitud estado = leerCampo(solicitud, "estado", EstadoSolicitud.class);

        assertEquals(PrioridadSolicitud.Nivel.ALTA, prioridad.nivel());
        assertEquals(EstadoSolicitud.CLASIFICADA, estado);
    }

    @Test
    void noDebeAsignarPrioridadSiNoEstaClasificada() {
        // RN-02 (falla): no se puede priorizar sin estar CLASIFICADA.
        Solicitud solicitud = nuevaSolicitud();

        assertThrows(IllegalStateException.class,
                () -> solicitud.asignarPrioridad(
                        new PrioridadSolicitud(
                                PrioridadSolicitud.Nivel.ALTA,
                                "Impacto alto"
                        ),
                        new IdUsuario("654321")
                ));
    }

    @Test
    void debeIniciarAtencionSiEstaClasificada() {
        // RN-02 (exito): CLASIFICADA -> EN_ATENCION.
        Solicitud solicitud = nuevaSolicitud();

        solicitud.clasificarSolicitud(
                TipoSolicitud.SOLICITUD_CUPO,
                new IdUsuario("654321")
        );

        solicitud.iniciarAtencion(
                new IdUsuario("777777")
        );

        EstadoSolicitud estado = leerCampo(solicitud, "estado", EstadoSolicitud.class);
        assertEquals(EstadoSolicitud.EN_ATENCION, estado);
    }

    @Test
    void noDebeIniciarAtencionSiNoEstaClasificada() {
        // RN-02 (falla): solo CLASIFICADA puede pasar a EN_ATENCION.
        Solicitud solicitud = nuevaSolicitud();

        assertThrows(IllegalStateException.class,
                () -> solicitud.iniciarAtencion(
                        new IdUsuario("777777")
                ));
    }

    @Test
    void debeCerrarSolicitudSiEstaEnAtencion() {
        // RN-03 (exito): con atencion iniciada, se puede cerrar.
        Solicitud solicitud = nuevaSolicitud();

        solicitud.clasificarSolicitud(
                TipoSolicitud.CANCELACION,
                new IdUsuario("654321")
        );

        solicitud.iniciarAtencion(
                new IdUsuario("777777")
        );

        solicitud.cerrarSolicitud(
                new IdUsuario("654321"),
                "Observacion"
        );

        EstadoSolicitud estado = leerCampo(solicitud, "estado", EstadoSolicitud.class);
        assertEquals(EstadoSolicitud.CERRADA, estado);
    }

    @Test
    void noDebeCerrarSolicitudSiNoEstaEnAtencion() {
        // RN-03 (falla): cerrar requiere estar en EN_ATENCION.
        Solicitud solicitud = nuevaSolicitud();

        solicitud.clasificarSolicitud(
                TipoSolicitud.CANCELACION,
                new IdUsuario("654321")
        );

        assertThrows(IllegalStateException.class,
                () -> solicitud.cerrarSolicitud(
                        new IdUsuario("654321"),
                        "Observacion"
                ));
    }

    @Test
    void noDebePermitirModificarSiEstaCerrada() {
        // RN-04 (falla): una solicitud cerrada no se puede modificar.
        Solicitud solicitud = nuevaSolicitud();

        solicitud.clasificarSolicitud(
                TipoSolicitud.CANCELACION,
                new IdUsuario("654321")
        );

        solicitud.iniciarAtencion(
                new IdUsuario("777777")
        );

        solicitud.cerrarSolicitud(
                new IdUsuario("654321"),
                "Observacion"
        );

        assertThrows(IllegalStateException.class,
                () -> solicitud.clasificarSolicitud(
                        TipoSolicitud.HOMOLOGACION,
                        new IdUsuario("654321")
                ));
    }

    @Test
    void debeRegistrarEventoPorCadaAccion() {
        // RN-05/RN-17 (exito): cada accion agrega un evento al historial.
        Solicitud solicitud = nuevaSolicitud();

        List<?> historial = leerCampo(solicitud, "historial", List.class);
        assertEquals(1, historial.size());

        solicitud.clasificarSolicitud(
                TipoSolicitud.CANCELACION,
                new IdUsuario("654321")
        );
        assertEquals(2, historial.size());

        solicitud.asignarPrioridad(
                new PrioridadSolicitud(
                        PrioridadSolicitud.Nivel.MEDIA,
                        "Impacto medio"
                ),
                new IdUsuario("654321")
        );
        assertEquals(3, historial.size());

        solicitud.iniciarAtencion(
                new IdUsuario("777777")
        );
        assertEquals(4, historial.size());

        solicitud.cerrarSolicitud(
                new IdUsuario("654321"),
                "Observacion"
        );
        assertEquals(5, historial.size());
    }

    @Test
    void debeAsignarCodigoNoNulo() {
        // RN-16 (exito): el codigo no puede ser nulo en la solicitud creada.
        Solicitud solicitud = nuevaSolicitud();

        CodigoSolicitud codigo = leerCampo(solicitud, "codigo", CodigoSolicitud.class);
        assertNotNull(codigo);
    }

    @Test
    void noDebePermitirCodigoNulo() {
        // RN-16 (falla): el codigo no puede ser nulo.
        assertThrows(NullPointerException.class,
                () -> new Solicitud(
                        null,
                        new IdUsuario("123456"),
                        TipoCanal.SAC,
                        TipoSolicitud.CONSULTA_ACADEMICA,
                        new DescripcionSolicitud("Solicitud valida de prueba")
                ));
    }

    private static Solicitud nuevaSolicitud() {
        // Crea una solicitud valida para reutilizar en las pruebas.
        return new Solicitud(
                CodigoSolicitud.generar(),
                new IdUsuario("123456"),
                TipoCanal.SAC,
                TipoSolicitud.CONSULTA_ACADEMICA,
                new DescripcionSolicitud("Solicitud valida de prueba")
        );
    }

    private static <T> T leerCampo(Object objetivo, String campo, Class<T> tipo) {
        // Acceso controlado a campos privados para validar estado interno.
        try {
            Field field = objetivo.getClass().getDeclaredField(campo);
            field.setAccessible(true);
            return tipo.cast(field.get(objetivo));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException("No se pudo leer el campo: " + campo, e);
        }
    }
}
