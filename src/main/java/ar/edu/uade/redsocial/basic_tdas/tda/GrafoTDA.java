package ar.edu.uade.redsocial.basic_tdas.tda;

/**
 * TDA Grafo con vértices enteros y aristas ponderadas.
 * Contamos con dos implementaciones: GrafoMA (matriz) y GrafoLA (lista).
 * Cada una tiene sus ventajas según el caso de uso.
 *
 * Invariante de representación:
 * - Cada vértice existe exactamente una vez.
 * - Una arista solo existe entre vértices previamente agregados.
 * - PesoArista(v1, v2) == 0 indica ausencia de arista.
 *
 * Las complejidades varían según la implementación:
 *   GrafoMA (Matriz de Adyacencia): AgregarArista/ExisteArista/PesoArista en O(Vert)
 *   GrafoLA (Lista de Adyacencia):  AgregarVertice en O(1), AgregarArista en O(1) con índice
 */
public interface GrafoTDA {

    void InicializarGrafo(); // O(1) LA / O(n²) MA — MA aloca la matriz completa

    void AgregarVertice(int v); // O(1) LA / O(Vert) MA

    void EliminarVertice(int v); // O(Vert + Arist_v) donde Arist_v = aristas de v

    ConjuntoTDA<Integer> Vertices(); // O(Vert)

    void AgregarArista(int v1, int v2, int peso); // O(1) LA / O(Vert) MA

    void EliminarArista(int v1, int v2); // O(grado) LA / O(Vert) MA

    boolean ExisteArista(int v1, int v2); // O(grado) LA / O(Vert) MA

    int PesoArista(int v1, int v2); // O(grado) LA / O(Vert) MA
}
