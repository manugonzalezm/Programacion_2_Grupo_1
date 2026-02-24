package ar.edu.uade.redsocial.basic_tdas.tda;

public interface DiccionarioSimpleTDA<K, V> {

    void InicializarDiccionario(); // O(1)

    void Agregar(K clave, V valor); // O(n) — busca clave existente

    void Eliminar(K clave); // O(n)

    V Recuperar(K clave); // O(n)

    ConjuntoTDA<K> Claves(); // O(n)
}
