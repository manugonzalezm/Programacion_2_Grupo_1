package ar.edu.uade.redsocial.basic_tdas.implementation;

import ar.edu.uade.redsocial.basic_tdas.tda.ColaTDA;

public class StaticColaTDA<T> implements ColaTDA<T> {

    Object[] a;
    int indice;

    public void InicializarCola() { // complejidad O(1)
        a = new Object[100];
        indice = 0;
    }

    public void Acolar(T x) { // complejidad O(1)
        a[indice] = x;
        indice++;
    }

    public void Desacolar() { // complejidad O(n), desplaza elementos
        for (int i = 0; i < indice - 1; i++) {
            a[i] = a[i + 1];
        }
        indice--;
    }

    public boolean ColaVacia() { // complejidad O(1)
        return (indice == 0);
    }

    @SuppressWarnings("unchecked")
    public T Primero() { // complejidad O(1)
        return (T) a[0];
    }
}
