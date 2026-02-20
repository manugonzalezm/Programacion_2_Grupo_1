package ar.edu.uade.redsocial.basic_tdas.tda;

public interface ConjuntoTDA<T> {

    void InicializarConjunto();

    boolean ConjuntoVacio();

    void Agregar(T x);

    T Elegir();

    void Sacar(T x);

    boolean Pertenece(T x);
}
