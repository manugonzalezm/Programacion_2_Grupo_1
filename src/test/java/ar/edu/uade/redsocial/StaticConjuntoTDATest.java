package ar.edu.uade.redsocial;

import ar.edu.uade.redsocial.basic_tdas.implementation.StaticConjuntoTDA;
import ar.edu.uade.redsocial.basic_tdas.tda.ConjuntoTDA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StaticConjuntoTDATest {

    private ConjuntoTDA<Integer> conjunto;

    @BeforeEach
    void setUp() {
        conjunto = new StaticConjuntoTDA<>();
        conjunto.InicializarConjunto();
    }

    @Test
    void agregarPerteneceYSacar() {
        conjunto.Agregar(5);
        conjunto.Agregar(10);
        assertTrue(conjunto.Pertenece(5));
        conjunto.Sacar(5);
        assertFalse(conjunto.Pertenece(5));
        assertTrue(conjunto.Pertenece(10));
    }

    @Test
    void noDuplicados() {
        conjunto.Agregar(5);
        conjunto.Agregar(5); // duplicado ignorado
        conjunto.Sacar(5);
        assertTrue(conjunto.ConjuntoVacio());
    }

    @Test
    void elegir() {
        conjunto.Agregar(42);
        assertEquals(42, conjunto.Elegir());
    }
}
