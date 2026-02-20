package ar.edu.uade.redsocial.basic_tdas.tda;

public interface DiccionarioMultipleTDA<K, V> {

    void InicializarDiccionario();

    void Agregar(K clave, V valor);

    void Eliminar(K clave);

    void EliminarValor(K clave, V valor);

    ConjuntoTDA<V> Recuperar(K clave);

    ConjuntoTDA<K> Claves();
}
