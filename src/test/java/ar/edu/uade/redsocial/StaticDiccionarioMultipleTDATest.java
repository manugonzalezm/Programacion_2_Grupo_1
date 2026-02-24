package ar.edu.uade.redsocial;

import ar.edu.uade.redsocial.basic_tdas.implementation.StaticDiccionarioMultipleTDA;
import ar.edu.uade.redsocial.basic_tdas.tda.ConjuntoTDA;
import ar.edu.uade.redsocial.basic_tdas.tda.DiccionarioMultipleTDA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StaticDiccionarioMultipleTDATest {

    private DiccionarioMultipleTDA<Integer, Integer> dic;

    @BeforeEach
    void setUp() {
        dic = new StaticDiccionarioMultipleTDA<>();
        dic.InicializarDiccionario();
    }

    @Test
    void agregarVariosValoresPorClave() {
        dic.Agregar(1, 10);
        dic.Agregar(1, 20);
        ConjuntoTDA<Integer> valores = dic.Recuperar(1);
        assertTrue(valores.Pertenece(10));
        assertTrue(valores.Pertenece(20));
    }

    @Test
    void eliminarValorIndividual() {
        dic.Agregar(1, 10);
        dic.Agregar(1, 20);
        dic.EliminarValor(1, 10);
        assertFalse(dic.Recuperar(1).Pertenece(10));
        assertTrue(dic.Recuperar(1).Pertenece(20));
    }

    @Test
    void eliminarUltimoValorEliminaClave() {
        dic.Agregar(1, 10);
        dic.EliminarValor(1, 10);
        assertTrue(dic.Claves().ConjuntoVacio());
    }
}
