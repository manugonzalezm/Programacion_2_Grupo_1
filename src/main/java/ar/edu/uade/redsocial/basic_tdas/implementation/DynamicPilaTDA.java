package ar.edu.uade.redsocial.basic_tdas.implementation;

import ar.edu.uade.redsocial.basic_tdas.entities.Nodo;
import ar.edu.uade.redsocial.basic_tdas.tda.PilaTDA;

public class DynamicPilaTDA implements PilaTDA {

    Nodo primero;

    public void InicializarPila() {
        primero = null;
    }

    public void Apilar(int x) {
        Nodo aux = new Nodo();
        aux.info = x;
        aux.sig = primero;
        primero = aux;
    }

    public void Desapilar() {
        primero = primero.sig;
    }

    public boolean PilaVacia() {
        return (primero == null);
    }

    public int Tope() {
        return primero.info;
    }
}

