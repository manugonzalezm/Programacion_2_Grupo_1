package ar.edu.uade.redsocial;

import ar.edu.uade.redsocial.implementation.StaticHistorialAccionesTDA;
import ar.edu.uade.redsocial.model.Accion;
import ar.edu.uade.redsocial.tda.HistorialAccionesTDA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StaticHistorialAccionesTDATest {

    private HistorialAccionesTDA historial;

    @BeforeEach
    void setUp() {
        historial = new StaticHistorialAccionesTDA();
    }

    @Test
    void iniciaVacio() {
        assertFalse(historial.hayAcciones());
    }

    @Test
    void registrarYDeshacer() {
        historial.registrarAccion(new Accion("t1", "d1"));
        assertTrue(historial.hayAcciones());
        Accion a = historial.deshacerUltimaAccion();
        assertEquals("t1", a.getTipo());
        assertFalse(historial.hayAcciones());
    }

    @Test
    void deshacerVacio() {
        assertNull(historial.deshacerUltimaAccion());
    }

    @Test
    void listarUltimasVacio() {
        List<Accion> lista = historial.listarUltimas(5);
        assertTrue(lista.isEmpty());
    }

    @Test
    void listarUltimas() {
        historial.registrarAccion(new Accion("1", "a"));
        historial.registrarAccion(new Accion("2", "b"));
        historial.registrarAccion(new Accion("3", "c"));
        List<Accion> lista = historial.listarUltimas(2);
        assertEquals(2, lista.size());
        assertEquals("3", lista.get(0).getTipo());
        assertEquals("2", lista.get(1).getTipo());
    }

    @Test
    void listarUltimasNoModificaPila() {
        historial.registrarAccion(new Accion("A", "x"));
        historial.listarUltimas(10);
        assertTrue(historial.hayAcciones());
        assertEquals("A", historial.deshacerUltimaAccion().getTipo());
    }
}
