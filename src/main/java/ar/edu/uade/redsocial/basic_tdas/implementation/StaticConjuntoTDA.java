package ar.edu.uade.redsocial.basic_tdas.implementation;

import ar.edu.uade.redsocial.basic_tdas.tda.ConjuntoTDA;

public class StaticConjuntoTDA<T> implements ConjuntoTDA<T> {

    Object[] a;
    int cant;

    public void InicializarConjunto() { // complejidad O(1)
        a = new Object[100];
        cant = 0;
    }

    public void Agregar(T x) { // complejidad O(n)
        if (!this.Pertenece(x)) {
            a[cant] = x;
            cant++;
        }
    }

    public boolean ConjuntoVacio() { // complejidad O(1)
        return cant == 0;
    }

    @SuppressWarnings("unchecked")
    public T Elegir() { // complejidad O(1)
        return (T) a[cant - 1];
    }

    public boolean Pertenece(T x) { // complejidad O(n)
        int i = 0;
        while (i < cant && !a[i].equals(x)) {
            i++;
        }
        return (i < cant);
    }

    public void Sacar(T x) { // complejidad O(n)
        int i = 0;
        while (i < cant && !a[i].equals(x)) {
            i++;
        }
        if (i < cant) {
            a[i] = a[cant - 1];
            cant--;
        }
    }
}
