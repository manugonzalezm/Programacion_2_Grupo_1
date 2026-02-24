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
    void registrarYDeshacerOrdenLIFO() {
        historial.registrarAccion(new Accion("1", "a"));
        historial.registrarAccion(new Accion("2", "b"));
        assertEquals("2", historial.deshacerUltimaAccion().getTipo());
        assertEquals("1", historial.deshacerUltimaAccion().getTipo());
        assertNull(historial.deshacerUltimaAccion());
    }

    @Test
    void listarUltimasRetornaMasRecientes() {
        historial.registrarAccion(new Accion("1", "a"));
        historial.registrarAccion(new Accion("2", "b"));
        historial.registrarAccion(new Accion("3", "c"));
        List<Accion> lista = historial.listarUltimas(2);
        assertEquals(2, lista.size());
        assertEquals("3", lista.get(0).getTipo());
    }

    @Test
    void listarNoModificaPila() {
        historial.registrarAccion(new Accion("A", "x"));
        historial.listarUltimas(10);
        assertTrue(historial.hayAcciones());
    }
}
