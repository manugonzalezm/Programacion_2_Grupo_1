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
    void registrarYDeshacer() {
        h.registrarAccion(new Accion("agregar", "cliente1"));
        Accion a = h.deshacerUltimaAccion();
        assertNotNull(a);
        assertEquals("agregar", a.getTipo());
        assertEquals("cliente1", a.getDetalle());
    }

    @Test
    void deshacerVacio() {
        assertNull(h.deshacerUltimaAccion());
    }

    @Test
    void hayAcciones() {
        assertFalse(h.hayAcciones());
        h.registrarAccion(new Accion("x", "y"));
        assertTrue(h.hayAcciones());
        h.deshacerUltimaAccion();
        assertFalse(h.hayAcciones());
    }

    @Test
    void ordenDeshacer() {
        h.registrarAccion(new Accion("1", "a"));
        h.registrarAccion(new Accion("2", "b"));
        assertEquals("2", h.deshacerUltimaAccion().getTipo());
        assertEquals("1", h.deshacerUltimaAccion().getTipo());
        assertNull(h.deshacerUltimaAccion());
    }

    @Test
    void listarUltimasVacio() {
        List<Accion> lista = h.listarUltimas(10);
        assertTrue(lista.isEmpty());
    }

    @Test
    void listarUltimasMenosQueDisponibles() {
        h.registrarAccion(new Accion("1", "a"));
        h.registrarAccion(new Accion("2", "b"));
        h.registrarAccion(new Accion("3", "c"));
        List<Accion> lista = h.listarUltimas(2);
        assertEquals(2, lista.size());
        assertEquals("3", lista.get(0).getTipo());
        assertEquals("2", lista.get(1).getTipo());
    }

    @Test
    void listarUltimasMasQueDisponibles() {
        h.registrarAccion(new Accion("1", "a"));
        h.registrarAccion(new Accion("2", "b"));
        List<Accion> lista = h.listarUltimas(10);
        assertEquals(2, lista.size());
        assertEquals("2", lista.get(0).getTipo());
        assertEquals("1", lista.get(1).getTipo());
    }

    @Test
    void listarUltimasNoModificaPila() {
        h.registrarAccion(new Accion("1", "a"));
        h.registrarAccion(new Accion("2", "b"));
        h.listarUltimas(10);
        assertTrue(h.hayAcciones());
        assertEquals("2", h.deshacerUltimaAccion().getTipo());
        assertEquals("1", h.deshacerUltimaAccion().getTipo());
    }

    @Test
    void listarUltimasExactamente() {
        for (int i = 1; i <= 5; i++) {
            h.registrarAccion(new Accion(String.valueOf(i), "d" + i));
        }
        List<Accion> lista = h.listarUltimas(5);
        assertEquals(5, lista.size());
        assertEquals("5", lista.get(0).getTipo());
        assertEquals("1", lista.get(4).getTipo());
    }
}
