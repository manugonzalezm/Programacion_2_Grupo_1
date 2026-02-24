package ar.edu.uade.redsocial;

import ar.edu.uade.redsocial.model.Accion;
import ar.edu.uade.redsocial.services.HistorialAcciones;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistorialAccionesTest {

    private HistorialAcciones h;

    @BeforeEach
    void setUp() {
        h = new HistorialAcciones();
    }

    @Test
    void registrarYDeshacerOrdenLIFO() {
        h.registrarAccion(new Accion("1", "a"));
        h.registrarAccion(new Accion("2", "b"));
        assertEquals("2", h.deshacerUltimaAccion().getTipo());
        assertEquals("1", h.deshacerUltimaAccion().getTipo());
        assertNull(h.deshacerUltimaAccion());
    }

    @Test
    void listarUltimasRetornaMasRecientes() {
        h.registrarAccion(new Accion("1", "a"));
        h.registrarAccion(new Accion("2", "b"));
        h.registrarAccion(new Accion("3", "c"));
        List<Accion> lista = h.listarUltimas(2);
        assertEquals(2, lista.size());
        assertEquals("3", lista.get(0).getTipo());
    }

    @Test
    void listarNoModificaPila() {
        h.registrarAccion(new Accion("A", "x"));
        h.listarUltimas(10);
        assertTrue(h.hayAcciones());
        assertEquals("A", h.deshacerUltimaAccion().getTipo());
    }
}
