package ar.edu.uade.redsocial.basic_tdas.implementation;

import ar.edu.uade.redsocial.basic_tdas.tda.ConjuntoTDA;
import ar.edu.uade.redsocial.basic_tdas.tda.DiccionarioSimpleTDA;

public class StaticDiccionarioSimpleTDA implements DiccionarioSimpleTDA {

    class Elemento {
        int clave;
        int valor;
    }

    Elemento[] elementos;
    int cant;

    public void InicializarDiccionario() { // complejidad O(1)
        cant = 0;
        elementos = new Elemento[100];
    }

    public void Agregar(int clave, int valor) { // complejidad O(n), n = cant (usa Clave2Indice)
        int pos = Clave2Indice(clave);

        if (pos == -1) {
            pos = cant;
            elementos[pos] = new Elemento();
            elementos[pos].clave = clave;
            cant++;
        }

        elementos[pos].valor = valor;
    }

    private int Clave2Indice(int clave) { // complejidad O(n), n = cant
        int i = cant - 1;

        while (i >= 0 && elementos[i].clave != clave) {
            i--;
        }

        return i;
    }

    public void Eliminar(int clave) { // complejidad O(n)
        int pos = Clave2Indice(clave);

        if (pos != -1) {
            elementos[pos] = elementos[cant - 1];
            cant--;
        }
    }

    public int Recuperar(int clave) { // complejidad O(n)
        int pos = Clave2Indice(clave);
        return elementos[pos].valor;
    }

    public ConjuntoTDA Claves() { // complejidad O(n), n = cant
        ConjuntoTDA c = new DynamicConjuntoTDA();
        c.InicializarConjunto();

        for (int i = 0; i < cant; i++) {
            c.Agregar(elementos[i].clave);
        }

        return c;
    }
}
