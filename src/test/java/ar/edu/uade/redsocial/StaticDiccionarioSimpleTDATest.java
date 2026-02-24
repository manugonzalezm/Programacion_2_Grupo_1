package ar.edu.uade.redsocial;

import ar.edu.uade.redsocial.basic_tdas.implementation.StaticDiccionarioSimpleTDA;
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
    void agregarYRecuperar() {
        dic.Agregar(1, 100);
        assertEquals(100, dic.Recuperar(1));
        dic.Agregar(1, 200); // actualiza el valor
        assertEquals(200, dic.Recuperar(1));
    }

    @Test
    void eliminarClave() {
        dic.Agregar(1, 100);
        dic.Agregar(2, 200);
        dic.Eliminar(1);
        assertFalse(dic.Claves().Pertenece(1));
        assertEquals(200, dic.Recuperar(2));
    }

    @Test
    void claveNoExistente() {
        assertTrue(dic.Claves().ConjuntoVacio());
    }
}
