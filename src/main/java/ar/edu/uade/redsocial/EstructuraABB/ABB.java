package ar.edu.uade.redsocial.EstructuraABB;

import java.util.ArrayList;
import java.util.List;

public class ABB<T extends Comparable<T>> {

    private NodoABB<T> raiz;

    public void agregar(T elemento) { // O(log n) promedio, O(n) peor caso
        raiz = agregarRec(raiz, elemento);
    }

    private NodoABB<T> agregarRec(NodoABB<T> nodo, T elemento) {
        if (nodo == null) {
            return new NodoABB<>(elemento);
        }

        if (elemento.compareTo(nodo.elemento) < 0) {
            nodo.izq = agregarRec(nodo.izq, elemento);
        } else {
            nodo.der = agregarRec(nodo.der, elemento);
        }

        return nodo;
    }

    public void imprimirNivel(int nivel) { // O(n)
        imprimirNivelRec(raiz, nivel);
    }

    private void imprimirNivelRec(NodoABB<T> nodo, int nivel) {
        if (nodo == null) return;

        if (nivel == 1) {
            System.out.println(nodo.elemento);
        } else {
            imprimirNivelRec(nodo.izq, nivel - 1);
            imprimirNivelRec(nodo.der, nivel - 1);
        }
    }

    /** Retorna los elementos en el nivel indicado (raíz = nivel 1). */
    public List<T> obtenerNivel(int nivel) { // O(n)
        List<T> resultado = new ArrayList<>();
        obtenerNivelRec(raiz, nivel, resultado);
        return resultado;
    }

    private void obtenerNivelRec(NodoABB<T> nodo, int nivel, List<T> resultado) {
        if (nodo == null) return;

        if (nivel == 1) {
            resultado.add(nodo.elemento);
        } else {
            obtenerNivelRec(nodo.izq, nivel - 1, resultado);
            obtenerNivelRec(nodo.der, nivel - 1, resultado);
        }
    }

    public boolean estaVacio() {
        return raiz == null;
    }
}
