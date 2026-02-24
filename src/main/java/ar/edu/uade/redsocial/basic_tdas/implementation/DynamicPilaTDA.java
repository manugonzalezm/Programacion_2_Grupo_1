package ar.edu.uade.redsocial.basic_tdas.implementation;

import ar.edu.uade.redsocial.basic_tdas.entities.Nodo;
import ar.edu.uade.redsocial.basic_tdas.tda.PilaTDA;

public class DynamicPilaTDA<T> implements PilaTDA<T> {

    Nodo<T> primero;

    public void InicializarPila() { // O(1)
        primero = null;
    }

    public void Apilar(T x) { // O(1)
        Nodo<T> aux = new Nodo<>();
        aux.info = x;
        aux.sig = primero;
        primero = aux;
    }

    public void Desapilar() { // O(1)
        primero = primero.sig;
    }

    public boolean PilaVacia() { // O(1)
        return (primero == null);
    }

    public T Tope() { // O(1)
        return primero.info;
    }
}
