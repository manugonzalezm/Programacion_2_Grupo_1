package ar.edu.uade.redsocial.basic_tdas.implementation;

import ar.edu.uade.redsocial.basic_tdas.entities.Nodo;
import ar.edu.uade.redsocial.basic_tdas.tda.ColaTDA;

public class DynamicColaTDA<T> implements ColaTDA<T> {

    Nodo<T> primero;
    Nodo<T> ultimo;

    public void InicializarCola() { // O(1)
        primero = null;
        ultimo = null;
    }

    public void Acolar(T x) { // O(1) — inserta al final con puntero a ultimo
        Nodo<T> aux = new Nodo<>();
        aux.info = x;
        aux.sig = null;

        if (ultimo != null)
            ultimo.sig = aux;

        ultimo = aux;

        if (primero == null)
            primero = ultimo;
    }

    public void Desacolar() { // O(1)
        primero = primero.sig;

        if (primero == null)
            ultimo = null;
    }

    public boolean ColaVacia() { // O(1)
        return (ultimo == null);
    }

    public T Primero() { // O(1)
        return primero.info;
    }
}
