package ar.edu.uade.redsocial.basic_tdas.entities;

/**
 * Nodo genérico para estructuras enlazadas (pila, cola, conjunto dinámicos).
 *
 * Invariante de representación:
 * - info: valor almacenado, siempre válido tras la construcción.
 * - sig: referencia al siguiente nodo en la cadena, o null si es el último.
 * - No existen ciclos en la cadena de nodos.
 */
public class Nodo<T> {
    public T info;
    public Nodo<T> sig;
}
