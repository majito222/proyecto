package co.edu.uniquindio.proyecto.domain.valueobject;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @Test
    void dosEmailsConMismoValorDebenSerIguales() {
        // Arrange
        Email e1 = new Email("usuario@uniquindio.edu.co");
        Email e2 = new Email("usuario@uniquindio.edu.co");

        // Assert
        assertEquals(e1, e2);
        assertEquals(e1.hashCode(), e2.hashCode());
    }

    @Test
    void noDebePermitirEmailInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> new Email("correo-invalido"));
    }
}