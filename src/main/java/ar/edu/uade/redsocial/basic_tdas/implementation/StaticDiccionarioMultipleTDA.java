package ar.edu.uade.redsocial.basic_tdas.implementation;

import ar.edu.uade.redsocial.basic_tdas.tda.ConjuntoTDA;
import ar.edu.uade.redsocial.basic_tdas.tda.DiccionarioMultipleTDA;

public class StaticDiccionarioMultipleTDA implements DiccionarioMultipleTDA {

    class Elemento {
        int clave;
        int[] valores;
        int cantValores;
    }

    Elemento[] elementos;
    int cantClaves;

    public void InicializarDiccionario() { // complejidad O(1)
        elementos = new Elemento[100];
        cantClaves = 0;
    }

    public void Agregar(int clave, int valor) { // complejidad O(n+m), n = cantClaves, m = valores de la clave
        int posC = Clave2Indice(clave);

        if (posC == -1) {
            posC = cantClaves;
            elementos[posC] = new Elemento();
            elementos[posC].clave = clave;
            elementos[posC].cantValores = 0;
            elementos[posC].valores = new int[100];
            cantClaves++;
        }

        Elemento e = elementos[posC];
        int posV = Valor2Indice(e, valor);

        if (posV == -1) {
            e.valores[e.cantValores] = valor;
            e.cantValores++;
        }
    }

    private int Clave2Indice(int clave) { // complejidad O(n), n = cantClaves
        int i = cantClaves - 1;

        while (i >= 0 && elementos[i].clave != clave) {
            i--;
        }

        return i;
    }

    public void Eliminar(int clave) { // complejidad O(n)
        int pos = Clave2Indice(clave);

        if (pos != -1) {
            elementos[pos] = elementos[cantClaves - 1];
            cantClaves--;
        }
    }

    public void EliminarValor(int clave, int valor) { // complejidad O(n+m)
        int posC = Clave2Indice(clave);

        if (posC != -1) {
            Elemento e = elementos[posC];
            int posV = Valor2Indice(e, valor);

            if (posV != -1) {
                e.valores[posV] = e.valores[e.cantValores - 1];
                e.cantValores--;

                if (e.cantValores == 0) {
                    Eliminar(clave);
                }
            }
        }
    }

    private int Valor2Indice(Elemento e, int valor) { // complejidad O(m), m = cantValores
        int i = e.cantValores - 1;

        while (i >= 0 && e.valores[i] != valor) {
            i--;
        }

        return i;
    }

    public ConjuntoTDA Recuperar(int clave) { // complejidad O(n+m)
        ConjuntoTDA c = new DynamicConjuntoTDA();
        c.InicializarConjunto();

        int pos = Clave2Indice(clave);

        if (pos != -1) {
            Elemento e = elementos[pos];

            for (int i = 0; i < e.cantValores; i++) {
                c.Agregar(e.valores[i]);
            }
        }

        return c;
    }

    public ConjuntoTDA Claves() { // complejidad O(n), n = cantClaves
        ConjuntoTDA c = new DynamicConjuntoTDA();
        c.InicializarConjunto();

        for (int i = 0; i < cantClaves; i++) {
            c.Agregar(elementos[i].clave);
        }

        return c;
    }
}
