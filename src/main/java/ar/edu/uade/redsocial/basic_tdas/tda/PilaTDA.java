package ar.edu.uade.redsocial.basic_tdas.tda;

public interface PilaTDA<T> {

    void InicializarPila(); // O(1)

    void Apilar(T x); // O(1)

    void Desapilar(); // O(1)

    boolean PilaVacia(); // O(1)

    T Tope(); // O(1)
}
