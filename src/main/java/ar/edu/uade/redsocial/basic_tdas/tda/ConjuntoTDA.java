package ar.edu.uade.redsocial.basic_tdas.tda;

public interface ConjuntoTDA {

    void InicializarConjunto();

    // siempre que el conjunto esté inicializado
    boolean ConjuntoVacio();

    // siempre que el conjunto esté inicializado
    void Agregar(int x);

    // siempre que el conjunto esté inicializado y no esté vacío
    int Elegir();

    // siempre que el conjunto esté inicializado
    void Sacar(int x);

    // siempre que el conjunto esté inicializado
    boolean Pertenece(int x);
}

