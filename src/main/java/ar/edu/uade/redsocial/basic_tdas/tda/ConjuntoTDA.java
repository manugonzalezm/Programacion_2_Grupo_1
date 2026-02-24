package ar.edu.uade.redsocial.basic_tdas.tda;

public interface ConjuntoTDA<T> {

    void InicializarConjunto(); // O(1)

    boolean ConjuntoVacio(); // O(1)

    void Agregar(T x); // O(n) — verifica pertenencia antes de agregar

    T Elegir(); // O(1)

    void Sacar(T x); // O(n)

    boolean Pertenece(T x); // O(n)
}
