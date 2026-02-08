package ar.edu.uade.redsocial.implementation;

import ar.edu.uade.redsocial.model.Accion;
import ar.edu.uade.redsocial.tda.HistorialAccionesTDA;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Implementación eficiente del historial de acciones usando ArrayDeque como pila (LIFO).
 * - registrarAccion / deshacerUltimaAccion / hayAcciones: O(1).
 * - listarUltimas: O(n), sin necesidad de desapilar/re-apilar (itera directamente el deque).
 * Capacidad máxima: 1000 acciones.
 */
public class StaticHistorialAccionesTDA implements HistorialAccionesTDA {

    private static final int CAPACIDAD = 1000;

    private final Deque<Accion> pila = new ArrayDeque<>();

    @Override
    public void registrarAccion(Accion accion) { // complejidad O(1)
        if (pila.size() >= CAPACIDAD) {
            throw new IllegalStateException("Historial de acciones lleno");
        }
        pila.push(accion);
    }

    @Override
    public Accion deshacerUltimaAccion() { // complejidad O(1)
        if (pila.isEmpty()) {
            return null;
        }
        return pila.pop();
    }

    @Override
    public boolean hayAcciones() { // complejidad O(1)
        return !pila.isEmpty();
    }

    @Override
    public List<Accion> listarUltimas(int n) { // complejidad O(n), sin modificar la pila
        List<Accion> resultado = new ArrayList<>();
        int count = 0;
        for (Accion a : pila) { // itera desde el tope sin desapilar
            if (count >= n) break;
            resultado.add(a);
            count++;
        }
        return resultado;
    }
}
