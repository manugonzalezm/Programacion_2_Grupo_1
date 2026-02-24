package ar.edu.uade.redsocial;

import ar.edu.uade.redsocial.model.SolicitudSeguimiento;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SolicitudSeguimientoTest {

    @Test
    void gettersYToString() {
        SolicitudSeguimiento s = new SolicitudSeguimiento("Alice", "Bob");
        assertEquals("Alice", s.getSolicitante());
        assertEquals("Bob", s.getSolicitado());
        assertEquals("Alice -> Bob", s.toString());
    }

    @Test
    void origenYDestino() {
        SolicitudSeguimiento s = new SolicitudSeguimiento("X", "Y");
        assertEquals("X", s.getOrigen());
        assertEquals("Y", s.getDestino());
    }
}
