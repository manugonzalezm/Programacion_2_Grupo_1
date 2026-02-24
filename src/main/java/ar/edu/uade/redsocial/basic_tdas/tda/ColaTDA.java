package ar.edu.uade.redsocial.basic_tdas.tda;

public interface ColaTDA<T> {

    void InicializarCola(); // O(1)

    void Acolar(T x); // O(1)

    void Desacolar(); // O(1) enlazada / O(n) estática

    boolean ColaVacia(); // O(1)

    T Primero(); // O(1)
}
