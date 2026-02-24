package ar.edu.uade.redsocial;

import ar.edu.uade.redsocial.basic_tdas.implementation.StaticPilaTDA;
import ar.edu.uade.redsocial.basic_tdas.tda.PilaTDA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StaticPilaTDATest {

    private PilaTDA<Integer> pila;

    @BeforeEach
    void setUp() {
        pila = new StaticPilaTDA<>();
        pila.InicializarPila();
    }

    @Test
    void apilarDesapilarLIFO() {
        pila.Apilar(10);
        pila.Apilar(20);
        pila.Apilar(30);
        assertEquals(30, pila.Tope());
        pila.Desapilar();
        assertEquals(20, pila.Tope());
        pila.Desapilar();
        assertEquals(10, pila.Tope());
        pila.Desapilar();
        assertTrue(pila.PilaVacia());
    }
}
