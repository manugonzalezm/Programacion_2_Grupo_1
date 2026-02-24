package ar.edu.uade.redsocial.basic_tdas.entities;

/**
 * Representa una arista dentro de GrafoLA.
 *
 * Invariante de representación:
 * - etiqueta: peso de la arista (> 0).
 * - nodoDestino: vértice destino, nunca null.
 * - sigArista: siguiente arista en la lista, o null si es la última.
 */
public class NodoArista {
    public int etiqueta;
    public NodoGrafo nodoDestino;
    public NodoArista sigArista;
}
