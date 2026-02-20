package ar.edu.uade.redsocial;

import ar.edu.uade.redsocial.basic_tdas.implementation.StaticDiccionarioSimpleTDA;
import ar.edu.uade.redsocial.basic_tdas.tda.ConjuntoTDA;
import ar.edu.uade.redsocial.basic_tdas.tda.DiccionarioSimpleTDA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StaticDiccionarioSimpleTDATest {

    private DiccionarioSimpleTDA<Integer, Integer> dic;

    @BeforeEach
    void setUp() {
        dic = new StaticDiccionarioSimpleTDA<>();
        dic.InicializarDiccionario();
    }

    @Test
    void clavesVacioAlIniciar() {
        ConjuntoTDA<Integer> claves = dic.Claves();
        assertTrue(claves.ConjuntoVacio());
    }

    @Test
    void agregarYRecuperar() {
        dic.Agregar(1, 100);
        assertEquals(100, dic.Recuperar(1));
    }

    @Test
    void agregarActualizaValor() {
        dic.Agregar(1, 100);
        dic.Agregar(1, 200);
        assertEquals(200, dic.Recuperar(1));
    }

    @Test
    void eliminar() {
        dic.Agregar(1, 100);
        dic.Agregar(2, 200);
        dic.Eliminar(1);
        ConjuntoTDA<Integer> claves = dic.Claves();
        assertFalse(claves.ConjuntoVacio());
        assertEquals(2, claves.Elegir());
    }

    @Test
    void eliminarNoExistente() {
        dic.Agregar(1, 100);
        dic.Eliminar(999);
        assertEquals(100, dic.Recuperar(1));
    }

    @Test
    void multipleClaves() {
        dic.Agregar(1, 10);
        dic.Agregar(2, 20);
        dic.Agregar(3, 30);
        assertEquals(10, dic.Recuperar(1));
        assertEquals(20, dic.Recuperar(2));
        assertEquals(30, dic.Recuperar(3));
    }

    @Test
    void clavesDevuelveTodasLasClaves() {
        dic.Agregar(5, 50);
        dic.Agregar(10, 100);
        ConjuntoTDA<Integer> claves = dic.Claves();
        int count = 0;
        while (!claves.ConjuntoVacio()) {
            int k = claves.Elegir();
            claves.Sacar(k);
            assertTrue(k == 5 || k == 10);
            count++;
        }
        assertEquals(2, count);
    }
}
