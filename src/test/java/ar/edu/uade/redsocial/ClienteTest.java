package ar.edu.uade.redsocial;

import ar.edu.uade.redsocial.model.Cliente;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    @Test
    void gettersYToString() {
        Cliente c = new Cliente("Maria", 88);
        assertEquals("Maria", c.getNombre());
        assertEquals(88, c.getScoring());
        assertTrue(c.toString().contains("Maria"));
        assertTrue(c.toString().contains("88"));
    }

    @Test
    void constructorConSolicitudesPendientes() {
        Cliente c = new Cliente("Alice", 95, Arrays.asList("Bob", "Charlie"));
        assertEquals(2, c.getSolicitudesPendientes().size());
        assertThrows(UnsupportedOperationException.class,
                () -> c.getSolicitudesPendientes().add("hack"));
    }
}
