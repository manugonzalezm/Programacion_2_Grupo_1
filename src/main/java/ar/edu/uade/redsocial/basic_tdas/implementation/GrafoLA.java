package ar.edu.uade.redsocial.basic_tdas.implementation;

import ar.edu.uade.redsocial.basic_tdas.entities.NodoArista;
import ar.edu.uade.redsocial.basic_tdas.entities.NodoGrafo;
import ar.edu.uade.redsocial.basic_tdas.tda.ConjuntoTDA;
import ar.edu.uade.redsocial.basic_tdas.tda.GrafoTDA;

import java.util.HashMap;
import java.util.Map;

/**
 * GrafoTDA implementado con lista de adyacencia.
 * Se eligió esta estructura porque es más eficiente en memoria cuando
 * el grafo tiene muchos vértices y pocas aristas por nodo.
 *
 * Para no perder tiempo buscando nodos en la lista cada vez que
 * se agrega o elimina una arista, se mantiene un HashMap interno
 * que permite encontrar cualquier nodo directamente.
 *
 * Complejidades:
 * - AgregarVertice: O(1)
 * - EliminarVertice: O(Arist_v) donde Arist_v = aristas que involucran a v
 * - AgregarArista: O(1)  (lookup O(1) vía índice + inserción al frente O(1))
 * - EliminarArista: O(grado)
 * - ExisteArista / PesoArista: O(grado)
 * - Vertices: O(Vert)
 * - ObtenerAdyacentes: O(grado)
 * - GradoSalida: O(grado)
 *
 * Invariante de representación:
 * - origen apunta al primer NodoGrafo de la lista, o null si está vacío.
 * - indice contiene exactamente los mismos nodos que la lista enlazada.
 * - No existen vértices duplicados.
 * - Las aristas apuntan siempre a NodoGrafo existentes en la lista.
 */
public class GrafoLA implements GrafoTDA {

    NodoGrafo origen;
    private final Map<Integer, NodoGrafo> indice = new HashMap<>();

    @Override
    public void InicializarGrafo() { // O(1)
        origen = null;
        indice.clear();
    }

    @Override
    public void AgregarVertice(int v) { // O(1)
        NodoGrafo aux = new NodoGrafo();
        aux.nodo = v;
        aux.arista = null;
        aux.sigNodo = origen;
        origen = aux;
        indice.put(v, aux);
    }

    @Override
    public void AgregarArista(int v1, int v2, int peso) { // O(1)
        NodoGrafo n1 = Vert2Nodo(v1);
        NodoGrafo n2 = Vert2Nodo(v2);
        if (n1 == null || n2 == null) return;

        NodoArista aux = new NodoArista();
        aux.etiqueta = peso;
        aux.nodoDestino = n2;
        aux.sigArista = n1.arista;
        n1.arista = aux;
    }

    private NodoGrafo Vert2Nodo(int v) { // O(1) con índice
        return indice.get(v);
    }

    @Override
    public void EliminarVertice(int v) { // O(Arist_v)
        NodoGrafo nodoV = indice.remove(v);
        if (nodoV == null) return;

        // sacar el nodo de la lista enlazada
        if (origen == nodoV) {
            origen = origen.sigNodo;
        } else {
            NodoGrafo aux = origen;
            while (aux != null && aux.sigNodo != nodoV)
                aux = aux.sigNodo;
            if (aux != null) aux.sigNodo = nodoV.sigNodo;
        }

        // sacar las aristas hacia v que tengan otros nodos
        for (NodoGrafo nodo : indice.values()) {
            EliminarAristaNodo(nodo, v);
        }
    }

    private void EliminarAristaNodo(NodoGrafo nodo, int v) { // O(grado)
        NodoArista aux = nodo.arista;
        if (aux == null) return;

        if (aux.nodoDestino.nodo == v) {
            nodo.arista = aux.sigArista;
        } else {
            while (aux.sigArista != null && aux.sigArista.nodoDestino.nodo != v)
                aux = aux.sigArista;
            if (aux.sigArista != null)
                aux.sigArista = aux.sigArista.sigArista;
        }
    }

    @Override
    public ConjuntoTDA<Integer> Vertices() { // O(Vert)
        ConjuntoTDA<Integer> c = new DynamicConjuntoTDA<>();
        c.InicializarConjunto();
        NodoGrafo aux = origen;
        while (aux != null) {
            c.Agregar(aux.nodo);
            aux = aux.sigNodo;
        }
        return c;
    }

    @Override
    public void EliminarArista(int v1, int v2) { // O(grado)
        NodoGrafo n1 = Vert2Nodo(v1);
        if (n1 != null) EliminarAristaNodo(n1, v2);
    }

    @Override
    public boolean ExisteArista(int v1, int v2) { // O(grado)
        NodoGrafo n1 = Vert2Nodo(v1);
        if (n1 == null) return false;
        NodoArista aux = n1.arista;
        while (aux != null && aux.nodoDestino.nodo != v2)
            aux = aux.sigArista;
        return aux != null;
    }

    @Override
    public int PesoArista(int v1, int v2) { // O(grado)
        NodoGrafo n1 = Vert2Nodo(v1);
        if (n1 == null) return 0;
        NodoArista aux = n1.arista;
        while (aux != null && aux.nodoDestino.nodo != v2)
            aux = aux.sigArista;
        return (aux != null) ? aux.etiqueta : 0;
    }

    /** Devuelve todos los vecinos del vértice v. O(grado) */
    public ConjuntoTDA<Integer> ObtenerAdyacentes(int v) { // O(grado)
        ConjuntoTDA<Integer> resultado = new DynamicConjuntoTDA<>();
        resultado.InicializarConjunto();
        NodoGrafo n = Vert2Nodo(v);
        if (n == null) return resultado;
        NodoArista a = n.arista;
        while (a != null) {
            resultado.Agregar(a.nodoDestino.nodo);
            a = a.sigArista;
        }
        return resultado;
    }

    /** Cuenta cuántos vecinos de salida tiene v. O(grado) */
    public int GradoSalida(int v) { // O(grado)
        NodoGrafo n = Vert2Nodo(v);
        if (n == null) return 0;
        int count = 0;
        NodoArista a = n.arista;
        while (a != null) { count++; a = a.sigArista; }
        return count;
    }
}
