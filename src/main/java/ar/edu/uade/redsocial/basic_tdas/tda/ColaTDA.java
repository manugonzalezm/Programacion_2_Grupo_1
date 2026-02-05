package ar.edu.uade.redsocial.basic_tdas.tda;

public interface ColaTDA {

    // siempre que la cola esté inicializada
    void InicializarCola();

    // siempre que la cola esté inicializada
    void Acolar(int x);

    // siempre que la cola no esté vacía
    void Desacolar();

    // siempre que la cola esté inicializada
    boolean ColaVacia();

    // siempre que la cola no esté vacía
    int Primero();
}

