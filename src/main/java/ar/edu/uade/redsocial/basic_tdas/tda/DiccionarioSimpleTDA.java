package ar.edu.uade.redsocial.basic_tdas.tda;

public interface DiccionarioSimpleTDA<K, V> {

    void InicializarDiccionario();

    void Agregar(K clave, V valor);

    void Eliminar(K clave);

    V Recuperar(K clave);

    ConjuntoTDA<K> Claves();
}
