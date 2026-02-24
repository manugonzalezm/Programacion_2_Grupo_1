package ar.edu.uade.redsocial.basic_tdas.tda;

public interface DiccionarioMultipleTDA<K, V> {

    void InicializarDiccionario(); // O(1)

    void Agregar(K clave, V valor); // O(n+m) — busca clave y valor

    void Eliminar(K clave); // O(n)

    void EliminarValor(K clave, V valor); // O(n+m)

    ConjuntoTDA<V> Recuperar(K clave); // O(n+m)

    ConjuntoTDA<K> Claves(); // O(n)
}
