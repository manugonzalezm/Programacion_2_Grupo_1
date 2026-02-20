package ar.edu.uade.redsocial.basic_tdas.implementation;

import ar.edu.uade.redsocial.basic_tdas.tda.PilaTDA;

public class StaticPilaTDA<T> implements PilaTDA<T> {

    Object[] a;
    int indice;

    public void InicializarPila() { // complejidad O(1)
        a = new Object[100];
        indice = 0;
    }

    public void Apilar(T x) { // complejidad O(1)
        a[indice] = x;
        indice++;
    }

    public void Desapilar() { // complejidad O(1)
        indice--;
    }

    public boolean PilaVacia() { // complejidad O(1)
        return (indice == 0);
    }

    @SuppressWarnings("unchecked")
    public T Tope() { // complejidad O(1)
        return (T) a[indice - 1];
    }
}
