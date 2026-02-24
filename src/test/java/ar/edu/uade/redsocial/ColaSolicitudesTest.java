package ar.edu.uade.redsocial;

import ar.edu.uade.redsocial.model.SolicitudSeguimiento;
import ar.edu.uade.redsocial.services.ColaSolicitudesSeguimiento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColaSolicitudesTest {

    private ColaSolicitudesSeguimiento cola;

    @BeforeEach
    void setUp() {
        cola = new ColaSolicitudesSeguimiento();
    }

    @Test
    void agregarYProcesarEnOrdenFIFO() {
        cola.agregarSolicitud(new SolicitudSeguimiento("A", "B"));
        cola.agregarSolicitud(new SolicitudSeguimiento("C", "D"));
        assertEquals("A", cola.procesarSolicitud().getSolicitante());
        assertEquals("C", cola.procesarSolicitud().getSolicitante());
        assertFalse(cola.haySolicitudes());
    }

    @Test
    void procesarColaVacia() {
        assertNull(cola.procesarSolicitud());
    }

    @Test
    void quitarSolicitudEspecifica() {
        cola.agregarSolicitud(new SolicitudSeguimiento("A", "B"));
        cola.agregarSolicitud(new SolicitudSeguimiento("C", "D"));
        assertTrue(cola.quitarSolicitud(new SolicitudSeguimiento("A", "B")));
        assertEquals("C", cola.procesarSolicitud().getOrigen());
    }

    @Test
    void listarPendientesNoModificaCola() {
        cola.agregarSolicitud(new SolicitudSeguimiento("A", "B"));
        cola.listarPendientes();
        assertTrue(cola.haySolicitudes());
    }
}
