package ar.edu.uade.redsocial.basic_tdas.implementation;

import ar.edu.uade.redsocial.basic_tdas.entities.Nodo;
import ar.edu.uade.redsocial.basic_tdas.tda.ColaTDA;

public class DynamicColaTDA<T> implements ColaTDA<T> {

    Nodo<T> primero;
    Nodo<T> ultimo;

    public void InicializarCola() {
        primero = null;
        ultimo = null;
    }

    public void Acolar(T x) {
        Nodo<T> aux = new Nodo<>();
        aux.info = x;
        aux.sig = null;

        if (ultimo != null)
            ultimo.sig = aux;

        ultimo = aux;

        if (primero == null)
            primero = ultimo;
    }

    public void Desacolar() {
        primero = primero.sig;

        if (primero == null)
            ultimo = null;
    }

    public boolean ColaVacia() {
        return (ultimo == null);
    }

    public T Primero() {
        return primero.info;
    }
}
