package ar.edu.uade.redsocial;

import ar.edu.uade.redsocial.model.SolicitudSeguimiento;
import ar.edu.uade.redsocial.services.ColaSolicitudesSeguimiento;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColaSolicitudesTest {

    @Test
    void agregarYProcesar() {
        ColaSolicitudesSeguimiento cola = new ColaSolicitudesSeguimiento();
        cola.agregarSolicitud(new SolicitudSeguimiento("Alice", "Bob"));
        SolicitudSeguimiento s = cola.procesarSolicitud();
        assertNotNull(s);
        assertEquals("Alice", s.getSolicitante());
        assertEquals("Bob", s.getSolicitado());
    }

    @Test
    void procesarVacia() {
        ColaSolicitudesSeguimiento cola = new ColaSolicitudesSeguimiento();
        assertNull(cola.procesarSolicitud());
    }

    @Test
    void ordenFIFO() {
        ColaSolicitudesSeguimiento cola = new ColaSolicitudesSeguimiento();
        cola.agregarSolicitud(new SolicitudSeguimiento("A", "B"));
        cola.agregarSolicitud(new SolicitudSeguimiento("C", "D"));
        assertEquals("A", cola.procesarSolicitud().getSolicitante());
        assertEquals("C", cola.procesarSolicitud().getSolicitante());
    }

    @Test
    void haySolicitudes() {
        ColaSolicitudesSeguimiento cola = new ColaSolicitudesSeguimiento();
        assertFalse(cola.haySolicitudes());
        cola.agregarSolicitud(new SolicitudSeguimiento("X", "Y"));
        assertTrue(cola.haySolicitudes());
        cola.procesarSolicitud();
        assertFalse(cola.haySolicitudes());
    }
}
