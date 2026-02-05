package ar.edu.uade.redsocial.basic_tdas.implementation;

import ar.edu.uade.redsocial.basic_tdas.entities.Nodo;
import ar.edu.uade.redsocial.basic_tdas.tda.ColaTDA;

public class DynamicColaTDA implements ColaTDA {

    // Primer elemento en la cola
    Nodo primero;

    // Último elemento en la cola, es decir, el último agregado
    Nodo ultimo;

    public void InicializarCola() {
        primero = null;
        ultimo = null;
    }

    public void Acolar(int x) {
        Nodo aux = new Nodo();
        aux.info = x;
        aux.sig = null;

        // Si la cola no está vacía
        if (ultimo != null)
            ultimo.sig = aux;

        ultimo = aux;

        // Si la cola estaba vacía
        if (primero == null)
            primero = ultimo;
    }

    public void Desacolar() {
        primero = primero.sig;

        // Si la cola queda vacía
        if (primero == null)
            ultimo = null;
    }

    public boolean ColaVacia() {
        return (ultimo == null);
    }

    public int Primero() {
        return primero.info;
    }
}

