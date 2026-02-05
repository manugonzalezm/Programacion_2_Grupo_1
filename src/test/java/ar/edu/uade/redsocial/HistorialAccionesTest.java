package ar.edu.uade.redsocial;

import ar.edu.uade.redsocial.model.Accion;
import ar.edu.uade.redsocial.services.HistorialAcciones;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HistorialAccionesTest {

    @Test
    void registrarYDeshacer() {
        HistorialAcciones h = new HistorialAcciones();
        h.registrarAccion(new Accion("agregar", "cliente1"));
        Accion a = h.deshacerUltimaAccion();
        assertNotNull(a);
        assertEquals("agregar", a.getTipo());
        assertEquals("cliente1", a.getDetalle());
    }

    @Test
    void deshacerVacio() {
        HistorialAcciones h = new HistorialAcciones();
        assertNull(h.deshacerUltimaAccion());
    }

    @Test
    void hayAcciones() {
        HistorialAcciones h = new HistorialAcciones();
        assertFalse(h.hayAcciones());
        h.registrarAccion(new Accion("x", "y"));
        assertTrue(h.hayAcciones());
        h.deshacerUltimaAccion();
        assertFalse(h.hayAcciones());
    }

    @Test
    void ordenDeshacer() {
        HistorialAcciones h = new HistorialAcciones();
        h.registrarAccion(new Accion("1", "a"));
        h.registrarAccion(new Accion("2", "b"));
        assertEquals("2", h.deshacerUltimaAccion().getTipo());
        assertEquals("1", h.deshacerUltimaAccion().getTipo());
        assertNull(h.deshacerUltimaAccion());
    }
}
