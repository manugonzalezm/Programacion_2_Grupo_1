package ar.edu.uade.redsocial.basic_tdas.tda;

public interface PilaTDA {

    // siempre que la pila esté inicializada
    void InicializarPila();

    // siempre que la pila esté inicializada
    void Apilar(int x);

    // siempre que la pila no esté vacía
    void Desapilar();

    // siempre que la pila esté inicializada
    boolean PilaVacia();

    // siempre que la pila no esté vacía
    int Tope();
}
