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
    void acolarDesacolarFIFO() {
        cola.Acolar(10);
        cola.Acolar(20);
        cola.Acolar(30);
        assertEquals(10, cola.Primero());
        cola.Desacolar();
        assertEquals(20, cola.Primero());
        cola.Desacolar();
        assertEquals(30, cola.Primero());
        cola.Desacolar();
        assertTrue(cola.ColaVacia());
    }
}
