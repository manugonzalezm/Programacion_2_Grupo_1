package ar.edu.uade.redsocial.basic_tdas.implementation;

import ar.edu.uade.redsocial.basic_tdas.tda.PilaTDA;

public class StaticPilaTDA implements PilaTDA {

    int[] a;     // arreglo en donde se guarda la información
    int indice;  // variable entera en donde se guarda la cantidad
    // de elementos que se tienen guardados

    public void InicializarPila() {
        a = new int[100];
        indice = 0;
    }

    public void Apilar(int x) {
        a[indice] = x;
        indice++;
    }

    public void Desapilar() {
        indice--;
    }

    public boolean PilaVacia() {
        return (indice == 0);
    }

    public int Tope() {
        return a[indice - 1];
    }
}
