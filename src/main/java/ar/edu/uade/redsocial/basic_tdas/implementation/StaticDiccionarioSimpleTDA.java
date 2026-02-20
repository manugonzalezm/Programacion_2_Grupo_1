package ar.edu.uade.redsocial.basic_tdas.implementation;

import ar.edu.uade.redsocial.basic_tdas.tda.ConjuntoTDA;
import ar.edu.uade.redsocial.basic_tdas.tda.DiccionarioSimpleTDA;

public class StaticDiccionarioSimpleTDA<K, V> implements DiccionarioSimpleTDA<K, V> {

    Object[] claves;
    Object[] valores;
    int cant;

    public void InicializarDiccionario() { // complejidad O(1)
        cant = 0;
        claves = new Object[100];
        valores = new Object[100];
    }

    public void Agregar(K clave, V valor) { // complejidad O(n)
        int pos = Clave2Indice(clave);

        if (pos == -1) {
            pos = cant;
            cant++;
        }

        claves[pos] = clave;
        valores[pos] = valor;
    }

    private int Clave2Indice(K clave) { // complejidad O(n)
        int i = cant - 1;

        while (i >= 0 && !claves[i].equals(clave)) {
            i--;
        }

        return i;
    }

    public void Eliminar(K clave) { // complejidad O(n)
        int pos = Clave2Indice(clave);

        if (pos != -1) {
            claves[pos] = claves[cant - 1];
            valores[pos] = valores[cant - 1];
            cant--;
        }
    }

    @SuppressWarnings("unchecked")
    public V Recuperar(K clave) { // complejidad O(n)
        int pos = Clave2Indice(clave);
        return (V) valores[pos];
    }

    @SuppressWarnings("unchecked")
    public ConjuntoTDA<K> Claves() { // complejidad O(n)
        ConjuntoTDA<K> c = new DynamicConjuntoTDA<>();
        c.InicializarConjunto();

        for (int i = 0; i < cant; i++) {
            c.Agregar((K) claves[i]);
        }

        return c;
    }
}
