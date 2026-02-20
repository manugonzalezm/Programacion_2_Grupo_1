package ar.edu.uade.redsocial;

import ar.edu.uade.redsocial.basic_tdas.implementation.DynamicPilaTDA;
import ar.edu.uade.redsocial.basic_tdas.implementation.DynamicColaTDA;
import ar.edu.uade.redsocial.basic_tdas.implementation.DynamicConjuntoTDA;
import ar.edu.uade.redsocial.basic_tdas.tda.PilaTDA;
import ar.edu.uade.redsocial.basic_tdas.tda.ColaTDA;
import ar.edu.uade.redsocial.basic_tdas.tda.ConjuntoTDA;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DynamicTDATest {

    // --- DynamicPilaTDA ---

    @Test
    void pilaVaciaAlIniciar() {
        PilaTDA<Integer> pila = new DynamicPilaTDA<>();
        pila.InicializarPila();
        assertTrue(pila.PilaVacia());
    }

    @Test
    void pilaApilarDesapilar() {
        PilaTDA<Integer> pila = new DynamicPilaTDA<>();
        pila.InicializarPila();
        pila.Apilar(1);
        pila.Apilar(2);
        assertEquals(2, pila.Tope());
        pila.Desapilar();
        assertEquals(1, pila.Tope());
        pila.Desapilar();
        assertTrue(pila.PilaVacia());
    }

    // --- DynamicColaTDA ---

    @Test
    void colaVaciaAlIniciar() {
        ColaTDA<Integer> cola = new DynamicColaTDA<>();
        cola.InicializarCola();
        assertTrue(cola.ColaVacia());
    }

    @Test
    void colaAcolarDesacolar() {
        ColaTDA<Integer> cola = new DynamicColaTDA<>();
        cola.InicializarCola();
        cola.Acolar(10);
        cola.Acolar(20);
        assertEquals(10, cola.Primero());
        cola.Desacolar();
        assertEquals(20, cola.Primero());
        cola.Desacolar();
        assertTrue(cola.ColaVacia());
    }

    @Test
    void colaUnElemento() {
        ColaTDA<Integer> cola = new DynamicColaTDA<>();
        cola.InicializarCola();
        cola.Acolar(42);
        assertFalse(cola.ColaVacia());
        assertEquals(42, cola.Primero());
        cola.Desacolar();
        assertTrue(cola.ColaVacia());
    }

    // --- DynamicConjuntoTDA ---

    @Test
    void conjuntoVacioAlIniciar() {
        ConjuntoTDA<Integer> c = new DynamicConjuntoTDA<>();
        c.InicializarConjunto();
        assertTrue(c.ConjuntoVacio());
    }

    @Test
    void conjuntoAgregarPertenece() {
        ConjuntoTDA<Integer> c = new DynamicConjuntoTDA<>();
        c.InicializarConjunto();
        c.Agregar(5);
        assertTrue(c.Pertenece(5));
        assertFalse(c.Pertenece(10));
    }

    @Test
    void conjuntoSacarPrimero() {
        ConjuntoTDA<Integer> c = new DynamicConjuntoTDA<>();
        c.InicializarConjunto();
        c.Agregar(1);
        c.Agregar(2);
        c.Sacar(2);
        assertFalse(c.Pertenece(2));
        assertTrue(c.Pertenece(1));
    }

    @Test
    void conjuntoSacarMedio() {
        ConjuntoTDA<Integer> c = new DynamicConjuntoTDA<>();
        c.InicializarConjunto();
        c.Agregar(1);
        c.Agregar(2);
        c.Agregar(3);
        c.Sacar(2);
        assertFalse(c.Pertenece(2));
        assertTrue(c.Pertenece(1));
        assertTrue(c.Pertenece(3));
    }

    @Test
    void conjuntoSacarNoExistente() {
        ConjuntoTDA<Integer> c = new DynamicConjuntoTDA<>();
        c.InicializarConjunto();
        c.Agregar(1);
        c.Sacar(999);
        assertTrue(c.Pertenece(1));
    }

    @Test
    void conjuntoSacarDeVacio() {
        ConjuntoTDA<Integer> c = new DynamicConjuntoTDA<>();
        c.InicializarConjunto();
        c.Sacar(1);
        assertTrue(c.ConjuntoVacio());
    }

    @Test
    void conjuntoElegir() {
        ConjuntoTDA<Integer> c = new DynamicConjuntoTDA<>();
        c.InicializarConjunto();
        c.Agregar(77);
        assertEquals(77, c.Elegir());
    }

    @Test
    void conjuntoDuplicado() {
        ConjuntoTDA<Integer> c = new DynamicConjuntoTDA<>();
        c.InicializarConjunto();
        c.Agregar(5);
        c.Agregar(5);
        c.Sacar(5);
        assertTrue(c.ConjuntoVacio());
    }
}
