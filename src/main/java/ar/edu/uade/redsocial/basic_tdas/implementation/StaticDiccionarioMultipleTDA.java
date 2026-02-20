package ar.edu.uade.redsocial.basic_tdas.implementation;

import ar.edu.uade.redsocial.basic_tdas.tda.ConjuntoTDA;
import ar.edu.uade.redsocial.basic_tdas.tda.DiccionarioMultipleTDA;

public class StaticDiccionarioMultipleTDA<K, V> implements DiccionarioMultipleTDA<K, V> {

    Object[] claves;
    Object[][] valores;
    int[] cantValores;
    int cantClaves;

    public void InicializarDiccionario() { // complejidad O(1)
        claves = new Object[100];
        valores = new Object[100][100];
        cantValores = new int[100];
        cantClaves = 0;
    }

    public void Agregar(K clave, V valor) { // complejidad O(n+m)
        int posC = Clave2Indice(clave);

        if (posC == -1) {
            posC = cantClaves;
            claves[posC] = clave;
            cantValores[posC] = 0;
            cantClaves++;
        }

        int posV = Valor2Indice(posC, valor);

        if (posV == -1) {
            valores[posC][cantValores[posC]] = valor;
            cantValores[posC]++;
        }
    }

    private int Clave2Indice(K clave) { // complejidad O(n)
        int i = cantClaves - 1;

        while (i >= 0 && !claves[i].equals(clave)) {
            i--;
        }

        return i;
    }

    public void Eliminar(K clave) { // complejidad O(n)
        int pos = Clave2Indice(clave);

        if (pos != -1) {
            claves[pos] = claves[cantClaves - 1];
            valores[pos] = valores[cantClaves - 1];
            cantValores[pos] = cantValores[cantClaves - 1];
            cantClaves--;
        }
    }

    public void EliminarValor(K clave, V valor) { // complejidad O(n+m)
        int posC = Clave2Indice(clave);

        if (posC != -1) {
            int posV = Valor2Indice(posC, valor);

            if (posV != -1) {
                valores[posC][posV] = valores[posC][cantValores[posC] - 1];
                cantValores[posC]--;

                if (cantValores[posC] == 0) {
                    Eliminar(clave);
                }
            }
        }
    }

    private int Valor2Indice(int posC, V valor) { // complejidad O(m)
        int i = cantValores[posC] - 1;

        while (i >= 0 && !valores[posC][i].equals(valor)) {
            i--;
        }

        return i;
    }

    @SuppressWarnings("unchecked")
    public ConjuntoTDA<V> Recuperar(K clave) { // complejidad O(n+m)
        ConjuntoTDA<V> c = new DynamicConjuntoTDA<>();
        c.InicializarConjunto();

        int pos = Clave2Indice(clave);

        if (pos != -1) {
            for (int i = 0; i < cantValores[pos]; i++) {
                c.Agregar((V) valores[pos][i]);
            }
        }

        return c;
    }

    @SuppressWarnings("unchecked")
    public ConjuntoTDA<K> Claves() { // complejidad O(n)
        ConjuntoTDA<K> c = new DynamicConjuntoTDA<>();
        c.InicializarConjunto();

        for (int i = 0; i < cantClaves; i++) {
            c.Agregar((K) claves[i]);
        }

        return c;
    }
}
