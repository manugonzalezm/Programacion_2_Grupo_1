package ar.edu.uade.redsocial.basic_tdas.implementation;

import ar.edu.uade.redsocial.basic_tdas.entities.Nodo;
import ar.edu.uade.redsocial.basic_tdas.tda.ConjuntoTDA;

public class DynamicConjuntoTDA<T> implements ConjuntoTDA<T> {

    Nodo<T> c;

    public void InicializarConjunto() {
        c = null;
    }

    public boolean ConjuntoVacio() {
        return (c == null);
    }

    public void Agregar(T x) {
        if (!this.Pertenece(x)) {
            Nodo<T> aux = new Nodo<>();
            aux.info = x;
            aux.sig = c;
            c = aux;
        }
    }

    public T Elegir() {
        return c.info;
    }

    public void Sacar(T x) {
        if (c != null) {
            if (c.info.equals(x)) {
                c = c.sig;
            } else {
                Nodo<T> aux = c;

                while (aux.sig != null && !aux.sig.info.equals(x))
                    aux = aux.sig;

                if (aux.sig != null)
                    aux.sig = aux.sig.sig;
            }
        }
    }

    public boolean Pertenece(T x) {
        Nodo<T> aux = c;

        while ((aux != null) && (!aux.info.equals(x))) {
            aux = aux.sig;
        }

        return (aux != null);
    }
}
