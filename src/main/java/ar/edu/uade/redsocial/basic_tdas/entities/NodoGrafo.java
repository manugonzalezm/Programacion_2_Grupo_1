package ar.edu.uade.redsocial.basic_tdas.entities;

/**
 * Representa un vértice dentro de GrafoLA.
 *
 * Invariante de representación:
 * - nodo: identificador entero único del vértice.
 * - arista: primera arista de la lista de adyacencia, o null si no tiene vecinos.
 * - sigNodo: siguiente nodo en la lista de vértices, o null si es el último.
 */
public class NodoGrafo {
    public int nodo;
    public NodoArista arista;
    public NodoGrafo sigNodo;
}
