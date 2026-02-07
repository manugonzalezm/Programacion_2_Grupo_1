package ar.edu.uade.redsocial;

import ar.edu.uade.redsocial.basic_tdas.implementation.StaticPilaTDA;
import ar.edu.uade.redsocial.basic_tdas.tda.PilaTDA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StaticPilaTDATest {

    private PilaTDA pila;

    @BeforeEach
    void setUp() {
        pila = new StaticPilaTDA();
        pila.InicializarPila();
    }

    @Test
    void pilaVaciaAlIniciar() {
        assertTrue(pila.PilaVacia());
    }

    @Test
    void apilarYTope() {
        pila.Apilar(42);
        assertFalse(pila.PilaVacia());
        assertEquals(42, pila.Tope());
    }

    @Test
    void desapilar() {
        pila.Apilar(1);
        pila.Apilar(2);
        assertEquals(2, pila.Tope());
        pila.Desapilar();
        assertEquals(1, pila.Tope());
        pila.Desapilar();
        assertTrue(pila.PilaVacia());
    }

    @Test
    void ordenLIFO() {
        pila.Apilar(10);
        pila.Apilar(20);
        pila.Apilar(30);
        assertEquals(30, pila.Tope());
        pila.Desapilar();
        assertEquals(20, pila.Tope());
        pila.Desapilar();
        assertEquals(10, pila.Tope());
    }

    @Test
    void multipleApilarDesapilar() {
        for (int i = 0; i < 50; i++) {
            pila.Apilar(i);
        }
        for (int i = 49; i >= 0; i--) {
            assertEquals(i, pila.Tope());
            pila.Desapilar();
        }
        assertTrue(pila.PilaVacia());
    }
}
