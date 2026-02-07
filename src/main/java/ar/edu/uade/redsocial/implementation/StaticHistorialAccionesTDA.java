package ar.edu.uade.redsocial.implementation;

import ar.edu.uade.redsocial.basic_tdas.implementation.StaticPilaTDA;
import ar.edu.uade.redsocial.basic_tdas.tda.PilaTDA;
import ar.edu.uade.redsocial.model.Accion;
import ar.edu.uade.redsocial.tda.HistorialAccionesTDA;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementación estática del historial de acciones usando PilaTDA (LIFO).
 * Apilar/Desapilar/Tope O(1). Se guardan índices en la pila y Accion[] para los objetos.
 */
public class StaticHistorialAccionesTDA implements HistorialAccionesTDA {

    private static final int CAPACIDAD = 1000;

    private PilaTDA pila;
    private Accion[] registro;
    private int nextIndex;

    public StaticHistorialAccionesTDA() { // complejidad O(1)
        pila = new StaticPilaTDA();
        pila.InicializarPila();
        registro = new Accion[CAPACIDAD];
        nextIndex = 0;
    }

    @Override
    public void registrarAccion(Accion accion) { // complejidad O(1)
        if (nextIndex >= CAPACIDAD) {
            throw new IllegalStateException("Historial de acciones lleno");
        }
        registro[nextIndex] = accion;
        pila.Apilar(nextIndex);
        nextIndex++;
    }

    @Override
    public Accion deshacerUltimaAccion() { // complejidad O(1)
        if (pila.PilaVacia()) {
            return null;
        }
        int idx = pila.Tope();
        pila.Desapilar();
        return registro[idx];
    }

    @Override
    public boolean hayAcciones() { // complejidad O(1)
        return !pila.PilaVacia();
    }

    @Override
    public List<Accion> listarUltimas(int n) { // complejidad O(n)
        List<Accion> resultado = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        int count = 0;
        // Desapilar hasta n elementos
        while (!pila.PilaVacia() && count < n) {
            int idx = pila.Tope();
            pila.Desapilar();
            indices.add(idx);
            resultado.add(registro[idx]);
            count++;
        }
        // Re-apilar en orden inverso para restaurar la pila
        for (int i = indices.size() - 1; i >= 0; i--) {
            pila.Apilar(indices.get(i));
        }
        return resultado;
    }
}
