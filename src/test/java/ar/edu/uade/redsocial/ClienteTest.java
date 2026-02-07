package ar.edu.uade.redsocial;

import ar.edu.uade.redsocial.model.Cliente;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

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

    @Test
    void constructorConListas() {
        List<String> sig = Arrays.asList("A", "B");
        List<String> con = Arrays.asList("C");
        Cliente c = new Cliente("X", 50, sig, con);
        assertEquals(2, c.getSiguiendo().size());
        assertEquals(1, c.getConexiones().size());
    }

    @Test
    void constructorSimple() {
        Cliente c = new Cliente("Y", 30);
        assertTrue(c.getSiguiendo().isEmpty());
        assertTrue(c.getConexiones().isEmpty());
    }

    @Test
    void listasNoModificables() {
        Cliente c = new Cliente("Z", 10);
        assertThrows(UnsupportedOperationException.class, () -> c.getSiguiendo().add("hack"));
        assertThrows(UnsupportedOperationException.class, () -> c.getConexiones().add("hack"));
    }

    @Test
    void constructorConNulls() {
        Cliente c = new Cliente("N", 1, null, null);
        assertNotNull(c.getSiguiendo());
        assertNotNull(c.getConexiones());
        assertTrue(c.getSiguiendo().isEmpty());
        assertTrue(c.getConexiones().isEmpty());
    }
}
