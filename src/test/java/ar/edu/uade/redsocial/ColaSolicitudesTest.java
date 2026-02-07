package ar.edu.uade.redsocial;

import ar.edu.uade.redsocial.model.SolicitudSeguimiento;
import ar.edu.uade.redsocial.services.ColaSolicitudesSeguimiento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ColaSolicitudesTest {

    private ColaSolicitudesSeguimiento cola;

    @BeforeEach
    void setUp() {
        cola = new ColaSolicitudesSeguimiento();
    }

    @Test
    void agregarYProcesar() {
        cola.agregarSolicitud(new SolicitudSeguimiento("Alice", "Bob"));
        SolicitudSeguimiento s = cola.procesarSolicitud();
        assertNotNull(s);
        assertEquals("Alice", s.getSolicitante());
        assertEquals("Bob", s.getSolicitado());
    }

    @Test
    void procesarVacia() {
        assertNull(cola.procesarSolicitud());
    }

    @Test
    void ordenFIFO() {
        cola.agregarSolicitud(new SolicitudSeguimiento("A", "B"));
        cola.agregarSolicitud(new SolicitudSeguimiento("C", "D"));
        assertEquals("A", cola.procesarSolicitud().getSolicitante());
        assertEquals("C", cola.procesarSolicitud().getSolicitante());
    }

    @Test
    void haySolicitudes() {
        assertFalse(cola.haySolicitudes());
        cola.agregarSolicitud(new SolicitudSeguimiento("X", "Y"));
        assertTrue(cola.haySolicitudes());
        cola.procesarSolicitud();
        assertFalse(cola.haySolicitudes());
    }

    @Test
    void quitarSolicitudExistente() {
        cola.agregarSolicitud(new SolicitudSeguimiento("A", "B"));
        cola.agregarSolicitud(new SolicitudSeguimiento("C", "D"));
        boolean ok = cola.quitarSolicitud(new SolicitudSeguimiento("A", "B"));
        assertTrue(ok);
        assertTrue(cola.haySolicitudes());
        SolicitudSeguimiento siguiente = cola.procesarSolicitud();
        assertEquals("C", siguiente.getOrigen());
    }

    @Test
    void quitarSolicitudNoExistente() {
        cola.agregarSolicitud(new SolicitudSeguimiento("A", "B"));
        boolean ok = cola.quitarSolicitud(new SolicitudSeguimiento("X", "Y"));
        assertFalse(ok);
        assertTrue(cola.haySolicitudes());
    }

    @Test
    void quitarSolicitudDeColaVacia() {
        boolean ok = cola.quitarSolicitud(new SolicitudSeguimiento("A", "B"));
        assertFalse(ok);
    }

    @Test
    void listarPendientesVacia() {
        List<SolicitudSeguimiento> pendientes = cola.listarPendientes();
        assertTrue(pendientes.isEmpty());
    }

    @Test
    void listarPendientesConSolicitudes() {
        cola.agregarSolicitud(new SolicitudSeguimiento("A", "B"));
        cola.agregarSolicitud(new SolicitudSeguimiento("C", "D"));
        List<SolicitudSeguimiento> pendientes = cola.listarPendientes();
        assertEquals(2, pendientes.size());
        assertEquals("A", pendientes.get(0).getOrigen());
        assertEquals("C", pendientes.get(1).getOrigen());
    }

    @Test
    void listarPendientesNoModificaCola() {
        cola.agregarSolicitud(new SolicitudSeguimiento("A", "B"));
        cola.listarPendientes();
        assertTrue(cola.haySolicitudes());
        assertEquals("A", cola.procesarSolicitud().getOrigen());
    }

    @Test
    void listarPendientesDespuesDeProcesar() {
        cola.agregarSolicitud(new SolicitudSeguimiento("A", "B"));
        cola.agregarSolicitud(new SolicitudSeguimiento("C", "D"));
        cola.procesarSolicitud();
        List<SolicitudSeguimiento> pendientes = cola.listarPendientes();
        assertEquals(1, pendientes.size());
        assertEquals("C", pendientes.get(0).getOrigen());
    }
}
