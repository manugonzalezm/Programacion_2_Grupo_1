package ar.edu.uade.redsocial;

import ar.edu.uade.redsocial.model.Cliente;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    @Test
    void getters() {
        Cliente c = new Cliente("Maria", 88);
        assertEquals("Maria", c.getNombre());
        assertEquals(88, c.getScoring());
    }

    @Test
    void toStringTieneNombreYScoring() {
        Cliente c = new Cliente("Test", 100);
        String s = c.toString();
        assertTrue(s.contains("Test"));
        assertTrue(s.contains("100"));
    }
}
