package ar.edu.uade.redsocial;

import ar.edu.uade.redsocial.basic_tdas.implementation.StaticDiccionarioMultipleTDA;
import ar.edu.uade.redsocial.basic_tdas.tda.ConjuntoTDA;
import ar.edu.uade.redsocial.basic_tdas.tda.DiccionarioMultipleTDA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StaticDiccionarioMultipleTDATest {

    private DiccionarioMultipleTDA dic;

    @BeforeEach
    void setUp() {
        dic = new StaticDiccionarioMultipleTDA();
        dic.InicializarDiccionario();
    }

    @Test
    void clavesVacioAlIniciar() {
        ConjuntoTDA claves = dic.Claves();
        assertTrue(claves.ConjuntoVacio());
    }

    @Test
    void agregarYRecuperar() {
        dic.Agregar(1, 10);
        dic.Agregar(1, 20);
        ConjuntoTDA valores = dic.Recuperar(1);
        assertTrue(valores.Pertenece(10));
        assertTrue(valores.Pertenece(20));
    }

    @Test
    void agregarValorDuplicado() {
        dic.Agregar(1, 10);
        dic.Agregar(1, 10);
        ConjuntoTDA valores = dic.Recuperar(1);
        assertTrue(valores.Pertenece(10));
        valores.Sacar(10);
        assertTrue(valores.ConjuntoVacio());
    }

    @Test
    void eliminarClave() {
        dic.Agregar(1, 10);
        dic.Agregar(2, 20);
        dic.Eliminar(1);
        ConjuntoTDA claves = dic.Claves();
        assertFalse(claves.ConjuntoVacio());
        assertEquals(2, claves.Elegir());
    }

    @Test
    void eliminarValor() {
        dic.Agregar(1, 10);
        dic.Agregar(1, 20);
        dic.EliminarValor(1, 10);
        ConjuntoTDA valores = dic.Recuperar(1);
        assertFalse(valores.Pertenece(10));
        assertTrue(valores.Pertenece(20));
    }

    @Test
    void eliminarValorUnicoEliminaClave() {
        dic.Agregar(1, 10);
        dic.EliminarValor(1, 10);
        ConjuntoTDA claves = dic.Claves();
        assertTrue(claves.ConjuntoVacio());
    }

    @Test
    void eliminarValorNoExistente() {
        dic.Agregar(1, 10);
        dic.EliminarValor(1, 999);
        ConjuntoTDA valores = dic.Recuperar(1);
        assertTrue(valores.Pertenece(10));
    }

    @Test
    void eliminarValorClaveNoExistente() {
        dic.EliminarValor(999, 10);
        ConjuntoTDA claves = dic.Claves();
        assertTrue(claves.ConjuntoVacio());
    }

    @Test
    void recuperarClaveNoExistente() {
        ConjuntoTDA valores = dic.Recuperar(999);
        assertTrue(valores.ConjuntoVacio());
    }

    @Test
    void eliminarClaveNoExistente() {
        dic.Agregar(1, 10);
        dic.Eliminar(999);
        ConjuntoTDA claves = dic.Claves();
        assertFalse(claves.ConjuntoVacio());
    }

    @Test
    void multiplesClavesYValores() {
        dic.Agregar(1, 10);
        dic.Agregar(1, 20);
        dic.Agregar(2, 30);
        ConjuntoTDA c = dic.Claves();
        int count = 0;
        while (!c.ConjuntoVacio()) {
            c.Sacar(c.Elegir());
            count++;
        }
        assertEquals(2, count);
    }
}
