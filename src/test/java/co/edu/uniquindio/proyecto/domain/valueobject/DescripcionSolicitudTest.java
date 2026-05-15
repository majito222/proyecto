package co.edu.uniquindio.proyecto.domain.valueobject;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DescripcionSolicitudTest {

    @Test
    void debeCrearDescripcionValida() {
        DescripcionSolicitud d =
                new DescripcionSolicitud("Solicitud de registro extraordinario");
        assertNotNull(d);
    }

    @Test
    void noDebePermitirDescripcionCorta() {
        assertThrows(IllegalArgumentException.class,
                () -> new DescripcionSolicitud("corta"));
    }
}