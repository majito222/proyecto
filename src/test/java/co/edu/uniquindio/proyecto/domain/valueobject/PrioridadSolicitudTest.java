package co.edu.uniquindio.proyecto.domain.valueobject;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PrioridadSolicitudTest {

    @Test
    void debeCrearPrioridadConJustificacion() {
        PrioridadSolicitud p =
                new PrioridadSolicitud(
                        PrioridadSolicitud.Nivel.ALTA,
                        "Impacto académico alto");

        assertEquals(PrioridadSolicitud.Nivel.ALTA, p.nivel());
    }

    @Test
    void noDebePermitirJustificacionVacia() {
        assertThrows(IllegalArgumentException.class,
                () -> new PrioridadSolicitud(
                        PrioridadSolicitud.Nivel.ALTA,
                        ""));
    }
}