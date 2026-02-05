package ar.edu.uade.redsocial.basic_tdas.tda;

public interface DiccionarioMultipleTDA {

    // siempre que el diccionario esté inicializado
    void InicializarDiccionario();

    // siempre que el diccionario esté inicializado
    void Agregar(int clave, int valor);

    // siempre que el diccionario esté inicializado
    void Eliminar(int clave);

    // siempre que el diccionario esté inicializado
    void EliminarValor(int clave, int valor);

    // siempre que el diccionario esté inicializado
    ConjuntoTDA Recuperar(int clave);

    // siempre que el diccionario esté inicializado
    ConjuntoTDA Claves();
}

