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
    void agregarYProcesarEnOrdenFIFO() {
        cola.agregarSolicitud(new SolicitudSeguimiento("1", "a"));
        cola.agregarSolicitud(new SolicitudSeguimiento("2", "b"));
        assertEquals("1", cola.procesarSolicitud().getOrigen());
        assertEquals("2", cola.procesarSolicitud().getOrigen());
        assertFalse(cola.haySolicitudes());
    }

    @Test
    void procesarColaVacia() {
        assertNull(cola.procesarSolicitud());
    }

    @Test
    void quitarSolicitudYListarRestantes() {
        cola.agregarSolicitud(new SolicitudSeguimiento("A", "B"));
        cola.agregarSolicitud(new SolicitudSeguimiento("C", "D"));
        assertTrue(cola.quitarSolicitud(new SolicitudSeguimiento("A", "B")));
        List<SolicitudSeguimiento> lista = cola.listarPendientes();
        assertEquals(1, lista.size());
        assertEquals("C", lista.get(0).getOrigen());
    }
}
