package ar.edu.uade.redsocial.basic_tdas.tda;

public interface PilaTDA<T> {

    void InicializarPila();

    void Apilar(T x);

    void Desapilar();

    boolean PilaVacia();

    T Tope();
}
