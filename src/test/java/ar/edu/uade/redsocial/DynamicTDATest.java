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

    @Test
    void pilaApilarDesapilarLIFO() {
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

    @Test
    void colaAcolarDesacolarFIFO() {
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
    void conjuntoAgregarPerteneceYSacar() {
        ConjuntoTDA<Integer> c = new DynamicConjuntoTDA<>();
        c.InicializarConjunto();
        c.Agregar(5);
        c.Agregar(10);
        assertTrue(c.Pertenece(5));
        c.Sacar(5);
        assertFalse(c.Pertenece(5));
        assertTrue(c.Pertenece(10));
    }

    @Test
    void conjuntoNoDuplicados() {
        ConjuntoTDA<Integer> c = new DynamicConjuntoTDA<>();
        c.InicializarConjunto();
        c.Agregar(5);
        c.Agregar(5); // duplicado ignorado
        c.Sacar(5);
        assertTrue(c.ConjuntoVacio());
    }
}
