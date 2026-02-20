package ar.edu.uade.redsocial;

import ar.edu.uade.redsocial.basic_tdas.implementation.StaticColaTDA;
import ar.edu.uade.redsocial.basic_tdas.tda.ColaTDA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StaticColaTDATest {

    private ColaTDA<Integer> cola;

    @BeforeEach
    void setUp() {
        cola = new StaticColaTDA<>();
        cola.InicializarCola();
    }

    @Test
    void colaVaciaAlIniciar() {
        assertTrue(cola.ColaVacia());
    }

    @Test
    void acolarYPrimero() {
        cola.Acolar(5);
        assertFalse(cola.ColaVacia());
        assertEquals(5, cola.Primero());
    }

    @Test
    void desacolar() {
        cola.Acolar(1);
        cola.Acolar(2);
        assertEquals(1, cola.Primero());
        cola.Desacolar();
        assertEquals(2, cola.Primero());
        cola.Desacolar();
        assertTrue(cola.ColaVacia());
    }

    @Test
    void ordenFIFO() {
        cola.Acolar(10);
        cola.Acolar(20);
        cola.Acolar(30);
        assertEquals(10, cola.Primero());
        cola.Desacolar();
        assertEquals(20, cola.Primero());
        cola.Desacolar();
        assertEquals(30, cola.Primero());
    }

    @Test
    void multipleProceso() {
        for (int i = 0; i < 20; i++) {
            cola.Acolar(i);
        }
        for (int i = 0; i < 20; i++) {
            assertEquals(i, cola.Primero());
            cola.Desacolar();
        }
        assertTrue(cola.ColaVacia());
    }
}
