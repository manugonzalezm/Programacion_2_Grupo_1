package ar.edu.uade.redsocial.basic_tdas.tda;

public interface DiccionarioSimpleTDA {

    // siempre que el diccionario esté inicializado
    void InicializarDiccionario();

    // siempre que el diccionario esté inicializado
    void Agregar(int clave, int valor);

    // siempre que el diccionario esté inicializado
    void Eliminar(int clave);

    // siempre que el diccionario esté inicializado y la clave exista
    int Recuperar(int clave);

    // siempre que el diccionario esté inicializado
    ConjuntoTDA Claves();
}

