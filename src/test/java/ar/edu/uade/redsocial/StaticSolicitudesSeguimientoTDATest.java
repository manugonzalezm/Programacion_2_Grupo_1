package ar.edu.uade.redsocial;

import ar.edu.uade.redsocial.implementation.StaticSolicitudesSeguimientoTDA;
import ar.edu.uade.redsocial.model.SolicitudSeguimiento;
import ar.edu.uade.redsocial.tda.SolicitudesSeguimientoTDA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StaticSolicitudesSeguimientoTDATest {

    private SolicitudesSeguimientoTDA cola;

    @BeforeEach
    void setUp() {
        cola = new StaticSolicitudesSeguimientoTDA();
    }

    @Test
    void iniciaVacia() {
        assertFalse(cola.haySolicitudes());
    }

    @Test
    void agregarYProcesar() {
        cola.agregarSolicitud(new SolicitudSeguimiento("A", "B"));
        assertTrue(cola.haySolicitudes());
        SolicitudSeguimiento s = cola.procesarSolicitud();
        assertEquals("A", s.getOrigen());
        assertEquals("B", s.getDestino());
        assertFalse(cola.haySolicitudes());
    }

    @Test
    void procesarVacia() {
        assertNull(cola.procesarSolicitud());
    }

    @Test
    void quitarSolicitud() {
        cola.agregarSolicitud(new SolicitudSeguimiento("A", "B"));
        cola.agregarSolicitud(new SolicitudSeguimiento("C", "D"));
        assertTrue(cola.quitarSolicitud(new SolicitudSeguimiento("A", "B")));
        assertEquals("C", cola.procesarSolicitud().getOrigen());
    }

    @Test
    void quitarSolicitudNoExistente() {
        cola.agregarSolicitud(new SolicitudSeguimiento("A", "B"));
        assertFalse(cola.quitarSolicitud(new SolicitudSeguimiento("X", "Y")));
    }

    @Test
    void listarPendientes() {
        cola.agregarSolicitud(new SolicitudSeguimiento("A", "B"));
        cola.agregarSolicitud(new SolicitudSeguimiento("C", "D"));
        List<SolicitudSeguimiento> lista = cola.listarPendientes();
        assertEquals(2, lista.size());
        assertEquals("A", lista.get(0).getOrigen());
        assertEquals("C", lista.get(1).getOrigen());
    }

    @Test
    void listarPendientesVacia() {
        assertTrue(cola.listarPendientes().isEmpty());
    }

    @Test
    void listarPendientesNoModificaCola() {
        cola.agregarSolicitud(new SolicitudSeguimiento("A", "B"));
        cola.listarPendientes();
        assertTrue(cola.haySolicitudes());
        assertEquals("A", cola.procesarSolicitud().getOrigen());
    }

    @Test
    void ordenFIFO() {
        cola.agregarSolicitud(new SolicitudSeguimiento("1", "a"));
        cola.agregarSolicitud(new SolicitudSeguimiento("2", "b"));
        cola.agregarSolicitud(new SolicitudSeguimiento("3", "c"));
        assertEquals("1", cola.procesarSolicitud().getOrigen());
        assertEquals("2", cola.procesarSolicitud().getOrigen());
        assertEquals("3", cola.procesarSolicitud().getOrigen());
    }
}
