package ar.edu.uade.redsocial;

import ar.edu.uade.redsocial.model.SolicitudSeguimiento;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SolicitudSeguimientoTest {

    @Test
    void getters() {
        SolicitudSeguimiento s = new SolicitudSeguimiento("Alice", "Bob");
        assertEquals("Alice", s.getSolicitante());
        assertEquals("Bob", s.getSolicitado());
    }

    @Test
    void toStringTieneAmbosNombres() {
        SolicitudSeguimiento s = new SolicitudSeguimiento("A", "B");
        assertTrue(s.toString().contains("A"));
        assertTrue(s.toString().contains("B"));
    }
}
