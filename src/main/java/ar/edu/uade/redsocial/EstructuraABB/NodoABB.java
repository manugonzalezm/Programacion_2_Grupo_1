package ar.edu.uade.redsocial.EstructuraABB;

public class NodoABB<T extends Comparable<T>> {

    T elemento;                 // O(1)
    NodoABB<T> izq;             // O(1)
    NodoABB<T> der;             // O(1)

    public NodoABB(T elemento) { // O(1)
        this.elemento = elemento; // O(1)
    }
}


