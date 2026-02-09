package ar.edu.uade.redsocial.basic_tdas.implementation;

import ar.edu.uade.redsocial.basic_tdas.tda.ConjuntoTDA;

public class StaticConjuntoTDA implements ConjuntoTDA {

    int[] a;
    int cant;

    public void InicializarConjunto() { // complejidad O(1)
        a = new int[100];
        cant = 0;
    }

    public void Agregar(int x) { // complejidad O(n), n = cant (usa Pertenece)
        // agrega x solo si no pertenece al conjunto
        if (!this.Pertenece(x)) {
            a[cant] = x;
            cant++;
        }
    }

    public boolean ConjuntoVacio() { // complejidad O(1)
        return cant == 0;
    }

    public int Elegir() { // complejidad O(1)
        // devuelve un elemento del conjunto
        return a[cant - 1];
    }

    public boolean Pertenece(int x) { // complejidad O(n), n = cant
        int i = 0;
        while (i < cant && a[i] != x) {
            i++;
        }
        return (i < cant);
    }

    public void Sacar(int x) { // complejidad O(n)
        // elimina x si pertenece al conjunto
        int i = 0;
        while (i < cant && a[i] != x) {
            i++;
        }
        if (i < cant) {
            a[i] = a[cant - 1];
            cant--;
        }
    }
}
