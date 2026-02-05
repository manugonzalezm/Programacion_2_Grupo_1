package ar.edu.uade.redsocial.basic_tdas.implementation;

import ar.edu.uade.redsocial.basic_tdas.tda.ColaTDA;

public class StaticColaTDA implements ColaTDA {

    int[] a;      // arreglo en donde se guarda la información
    int indice;   // variable que indica la cantidad de elementos en la cola

    public void InicializarCola() {
        a = new int[100];
        indice = 0;
    }

    public void Acolar(int x) {
        a[indice] = x;
        indice++;
    }

    public void Desacolar() {
        // desplaza todos los elementos una posición a la izquierda
        for (int i = 0; i < indice - 1; i++) {
            a[i] = a[i + 1];
        }
        indice--;
    }

    public boolean ColaVacia() {
        return (indice == 0);
    }

    public int Primero() {
        return a[0];
    }
}
