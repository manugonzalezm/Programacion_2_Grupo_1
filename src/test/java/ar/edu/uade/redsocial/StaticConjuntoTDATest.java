package ar.edu.uade.redsocial;

import ar.edu.uade.redsocial.basic_tdas.implementation.StaticConjuntoTDA;
import ar.edu.uade.redsocial.basic_tdas.tda.ConjuntoTDA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StaticConjuntoTDATest {

    private ConjuntoTDA conjunto;

    @BeforeEach
    void setUp() {
        conjunto = new StaticConjuntoTDA();
        conjunto.InicializarConjunto();
    }

    @Test
    void conjuntoVacioAlIniciar() {
        assertTrue(conjunto.ConjuntoVacio());
    }

    @Test
    void agregar() {
        conjunto.Agregar(5);
        assertFalse(conjunto.ConjuntoVacio());
        assertTrue(conjunto.Pertenece(5));
    }

    @Test
    void agregarDuplicado() {
        conjunto.Agregar(5);
        conjunto.Agregar(5);
        assertTrue(conjunto.Pertenece(5));
        conjunto.Sacar(5);
        assertTrue(conjunto.ConjuntoVacio());
    }

    @Test
    void sacar() {
        conjunto.Agregar(10);
        conjunto.Agregar(20);
        conjunto.Sacar(10);
        assertFalse(conjunto.Pertenece(10));
        assertTrue(conjunto.Pertenece(20));
    }

    @Test
    void sacarNoExistente() {
        conjunto.Agregar(1);
        conjunto.Sacar(999);
        assertTrue(conjunto.Pertenece(1));
    }

    @Test
    void elegir() {
        conjunto.Agregar(42);
        assertEquals(42, conjunto.Elegir());
    }

    @Test
    void perteneceNoExistente() {
        assertFalse(conjunto.Pertenece(99));
    }

    @Test
    void multipleElementos() {
        conjunto.Agregar(1);
        conjunto.Agregar(2);
        conjunto.Agregar(3);
        assertTrue(conjunto.Pertenece(1));
        assertTrue(conjunto.Pertenece(2));
        assertTrue(conjunto.Pertenece(3));
        assertFalse(conjunto.Pertenece(4));
    }
}
