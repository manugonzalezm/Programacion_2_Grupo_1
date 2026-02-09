package ar.edu.uade.redsocial.basic_tdas.entities;

/**
 * Nodo para estructuras enlazadas (pila, cola, conjunto dinámicos).
 *
 * Invariante de representación:
 * - info: valor entero almacenado, siempre válido tras la construcción.
 * - sig: referencia al siguiente nodo en la cadena, o null si es el último.
 * - No existen ciclos en la cadena de nodos (sig no apunta a sí mismo ni a un antecesor).
 */
public class Nodo {
    public int info;
    public Nodo sig;
}