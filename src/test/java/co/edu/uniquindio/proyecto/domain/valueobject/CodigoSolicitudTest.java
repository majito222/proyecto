package co.edu.uniquindio.proyecto.domain.valueobject;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CodigoSolicitudTest {

    @Test
    void debeGenerarCodigoValido() {
        CodigoSolicitud codigo = CodigoSolicitud.generar();
        assertNotNull(codigo.valor());
    }

    @Test
    void noDebePermitirCodigoVacio() {
        assertThrows(IllegalArgumentException.class,
                () -> new CodigoSolicitud(""));
    }
}