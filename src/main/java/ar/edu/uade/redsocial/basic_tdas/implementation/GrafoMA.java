package ar.edu.uade.redsocial.basic_tdas.implementation;

import ar.edu.uade.redsocial.basic_tdas.tda.ConjuntoTDA;
import ar.edu.uade.redsocial.basic_tdas.tda.GrafoTDA;

/**
 * GrafoTDA implementado con matriz de adyacencia.
 * Útil para grafos pequeños o cuando se consultan aristas con mucha frecuencia.
 * La capacidad máxima está fijada en n vértices.
 *
 * Complejidades:
 * - InicializarGrafo: O(n²) — aloca la matriz n×n
 * - AgregarVertice / EliminarVertice: O(Vert)
 * - AgregarArista / EliminarArista / ExisteArista / PesoArista: O(Vert) — por Vert2Indice
 * - Vertices: O(Vert)
 *
 * Invariante de representación:
 * - cantNodos indica cuántas entradas de Etiqs y filas/columnas de MAdy están en uso.
 * - MAdy[i][j] == 0 indica ausencia de arista entre los vértices en índices i y j.
 * - Etiqs[0..cantNodos-1] contiene las etiquetas activas sin duplicados.
 */
public class GrafoMA implements GrafoTDA {

    static int n = 1_100_000; // capacidad máxima de vértices

    int[][] MAdy;
    int[] Etiqs;
    int cantNodos;

    @Override
    public void InicializarGrafo() { // O(n²)
        MAdy = new int[n][n];
        Etiqs = new int[n];
        cantNodos = 0;
    }

    @Override
    public void AgregarVertice(int v) { // O(Vert)
        Etiqs[cantNodos] = v;
        for (int i = 0; i <= cantNodos; i++) {
            MAdy[cantNodos][i] = 0;
            MAdy[i][cantNodos] = 0;
        }
        cantNodos++;
    }

    @Override
    public void EliminarVertice(int v) { // O(Vert)
        int ind = Vert2Indice(v);
        if (ind == -1) return;

        for (int k = 0; k < cantNodos; k++)
            MAdy[k][ind] = MAdy[k][cantNodos - 1];

        for (int k = 0; k < cantNodos; k++)
            MAdy[ind][k] = MAdy[cantNodos - 1][k];

        Etiqs[ind] = Etiqs[cantNodos - 1];
        cantNodos--;
    }

    private int Vert2Indice(int v) { // O(Vert)
        int i = cantNodos - 1;
        while (i >= 0 && Etiqs[i] != v)
            i--;
        return i;
    }

    @Override
    public ConjuntoTDA<Integer> Vertices() { // O(Vert)
        ConjuntoTDA<Integer> vert = new DynamicConjuntoTDA<>();
        vert.InicializarConjunto();
        for (int i = 0; i < cantNodos; i++)
            vert.Agregar(Etiqs[i]);
        return vert;
    }

    @Override
    public void AgregarArista(int v1, int v2, int peso) { // O(Vert)
        int o = Vert2Indice(v1);
        int d = Vert2Indice(v2);
        if (o != -1 && d != -1)
            MAdy[o][d] = peso;
    }

    @Override
    public void EliminarArista(int v1, int v2) { // O(Vert)
        int o = Vert2Indice(v1);
        int d = Vert2Indice(v2);
        if (o != -1 && d != -1)
            MAdy[o][d] = 0;
    }

    @Override
    public boolean ExisteArista(int v1, int v2) { // O(Vert)
        int o = Vert2Indice(v1);
        int d = Vert2Indice(v2);
        return o != -1 && d != -1 && MAdy[o][d] != 0;
    }

    @Override
    public int PesoArista(int v1, int v2) { // O(Vert)
        int o = Vert2Indice(v1);
        int d = Vert2Indice(v2);
        if (o == -1 || d == -1) return 0;
        return MAdy[o][d];
    }
}
