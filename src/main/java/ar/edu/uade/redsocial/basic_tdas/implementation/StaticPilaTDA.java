package ar.edu.uade.redsocial.basic_tdas.implementation;

import ar.edu.uade.redsocial.basic_tdas.tda.PilaTDA;

public class StaticPilaTDA implements PilaTDA {

    int[] a;     // arreglo en donde se guarda la información
    int indice;  // variable entera en donde se guarda la cantidad
    // de elementos que se tienen guardados

    public void InicializarPila() { // complejidad O(1)
        a = new int[100];
        indice = 0;
    }

    public void Apilar(int x) { // complejidad O(1)
        a[indice] = x;
        indice++;
    }

    public void Desapilar() { // complejidad O(1)
        indice--;
    }

    public boolean PilaVacia() { // complejidad O(1)
        return (indice == 0);
    }

    public int Tope() { // complejidad O(1)
        return a[indice - 1];
    }
}
