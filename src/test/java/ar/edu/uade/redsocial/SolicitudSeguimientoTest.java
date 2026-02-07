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
    void getOrigenDestino() {
        SolicitudSeguimiento s = new SolicitudSeguimiento("Alice", "Bob");
        assertEquals("Alice", s.getOrigen());
        assertEquals("Bob", s.getDestino());
    }

    @Test
    void toStringTieneAmbosNombres() {
        SolicitudSeguimiento s = new SolicitudSeguimiento("A", "B");
        String str = s.toString();
        assertTrue(str.contains("A"));
        assertTrue(str.contains("B"));
    }

    @Test
    void toStringFormato() {
        SolicitudSeguimiento s = new SolicitudSeguimiento("X", "Y");
        assertEquals("X -> Y", s.toString());
    }
}
