package ar.edu.uade.redsocial.EstructuraABB;

public class ABB<T extends Comparable<T>> {

    private NodoABB<T> raiz;

    public void agregar(T elemento) {
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

    public void imprimirNivel(int nivel) {     // O(n)
        imprimirNivelRec(raiz, nivel);         // O(n)
    }


    private void imprimirNivelRec(NodoABB<T> nodo, int nivel) {

        if (nodo == null) return;              // O(1)

        if (nivel == 1) {                      // O(1)
            System.out.println(nodo.elemento); // O(1)
        } else {
            imprimirNivelRec(nodo.izq, nivel - 1); // O(n)
            imprimirNivelRec(nodo.der, nivel - 1); // O(n)
        }
    }

}
