package co.edu.uniquindio.proyecto.domain.entity;

import co.edu.uniquindio.proyecto.domain.valueobject.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SolicitudTest {

    @Test
    void debeCrearSolicitudEnEstadoRegistradaYRegistrarHistorial() {
        // Reglas base: al crear la solicitud queda REGISTRADA y se agrega un evento al historial.
        Solicitud solicitud = nuevaSolicitud();

        EstadoSolicitud estado = leerCampo(solicitud, "estado", EstadoSolicitud.class);
        List<?> historial = leerCampo(solicitud, "historial", List.class);

        assertEquals(EstadoSolicitud.REGISTRADA, estado);
        assertEquals(1, historial.size());
    }

    @Test
    void debeClasificarSolicitudSiEstaRegistrada() {
        // Éxito: clasificar es válido solo cuando la solicitud está REGISTRADA.
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
        // Falla: no se puede clasificar dos veces.
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
    void noDebeAsignarPrioridadSiNoEstaClasificada() {
        // Falla: no se puede asignar prioridad sin clasificación previa.
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
    void debeAsignarPrioridadSiEstaClasificada() {
        // Éxito: con clasificación previa, se puede asignar prioridad.
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
    void noDebeIniciarAtencionSiNoEstaClasificada() {
        // Falla: solo solicitudes CLASIFICADAS pueden pasar a EN_ATENCION.
        Solicitud solicitud = nuevaSolicitud();

        assertThrows(IllegalStateException.class,
                () -> solicitud.iniciarAtencion(
                        new IdUsuario("777777")
                ));
    }

    @Test
    void debeIniciarAtencionSiEstaClasificada() {
        // Éxito: una solicitud clasificada puede iniciar atención.
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
    void noDebeCerrarSolicitudSiNoEstaEnAtencion() {
        // Falla: cerrar requiere estar en EN_ATENCION.
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
    void debeCerrarSolicitudSiEstaEnAtencion() {
        // Éxito: con atención iniciada, se puede cerrar.
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

    private static Solicitud nuevaSolicitud() {
        // Crea una solicitud válida para reutilizar en las pruebas.
        return new Solicitud(
                CodigoSolicitud.generar(),
                new IdUsuario("123456"),
                TipoCanal.SAC,
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
