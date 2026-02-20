package ar.edu.uade.redsocial.basic_tdas.tda;

public interface ColaTDA<T> {

    void InicializarCola();

    void Acolar(T x);

    void Desacolar();

    boolean ColaVacia();

    T Primero();
}
