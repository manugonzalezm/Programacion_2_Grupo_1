package ar.edu.uade.redsocial;

import ar.edu.uade.redsocial.model.Cliente;
import ar.edu.uade.redsocial.model.SolicitudSeguimiento;
import org.junit.jupiter.api.Test;

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
    void solicitudesRecibidasInmutablesDesdeAfuera() {
        Cliente c = new Cliente("Alice", 95);
        assertTrue(c.getSolicitudesRecibidas().isEmpty());
        assertThrows(UnsupportedOperationException.class,
                () -> c.getSolicitudesRecibidas().add(new SolicitudSeguimiento("hack", "Alice")));
    }

    @Test
    void agregarYEliminarSolicitudRecibida() {
        Cliente c = new Cliente("Bob", 80);
        SolicitudSeguimiento s = new SolicitudSeguimiento("Alice", "Bob");

        c.agregarSolicitudRecibida(s);
        assertEquals(1, c.getSolicitudesRecibidas().size());
        assertEquals("Alice", c.getSolicitudesRecibidas().get(0).getOrigen());

        SolicitudSeguimiento eliminada = c.eliminarSolicitudRecibidaPorIndice(0);
        assertNotNull(eliminada);
        assertEquals("Alice", eliminada.getOrigen());
        assertTrue(c.getSolicitudesRecibidas().isEmpty());
    }

    @Test
    void eliminarSolicitudInexistente() {
        Cliente c = new Cliente("Bob", 80);
        assertNull(c.eliminarSolicitudRecibidaPorIndice(0));
        assertNull(c.eliminarSolicitudRecibidaPorIndice(-1));
    }

    @Test
    void eliminarSolicitudPorOrigen() {
        Cliente c = new Cliente("Bob", 80);
        c.agregarSolicitudRecibida(new SolicitudSeguimiento("Alice", "Bob"));
        c.agregarSolicitudRecibida(new SolicitudSeguimiento("Charlie", "Bob"));

        assertTrue(c.eliminarSolicitudRecibida(new SolicitudSeguimiento("Alice", "Bob")));
        assertEquals(1, c.getSolicitudesRecibidas().size());
        assertEquals("Charlie", c.getSolicitudesRecibidas().get(0).getOrigen());
    }
}
