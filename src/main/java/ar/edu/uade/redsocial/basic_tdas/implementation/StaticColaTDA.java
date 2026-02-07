package ar.edu.uade.redsocial.basic_tdas.implementation;

import ar.edu.uade.redsocial.basic_tdas.tda.ColaTDA;

public class StaticColaTDA implements ColaTDA {

    int[] a;      // arreglo en donde se guarda la información
    int indice;   // variable que indica la cantidad de elementos en la cola

    public void InicializarCola() { // complejidad O(1)
        a = new int[100];
        indice = 0;
    }

    public void Acolar(int x) { // complejidad O(1)
        a[indice] = x;
        indice++;
    }

    public void Desacolar() { // complejidad O(n), n = elementos en cola (desplaza)
        // desplaza todos los elementos una posición a la izquierda
        for (int i = 0; i < indice - 1; i++) {
            a[i] = a[i + 1];
        }
        indice--;
    }

    public boolean ColaVacia() { // complejidad O(1)
        return (indice == 0);
    }

    public int Primero() { // complejidad O(1)
        return a[0];
    }
}
