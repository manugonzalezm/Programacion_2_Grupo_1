package ar.edu.uade.redsocial.implementation;

import ar.edu.uade.redsocial.basic_tdas.implementation.DynamicPilaTDA;
import ar.edu.uade.redsocial.basic_tdas.tda.PilaTDA;
import ar.edu.uade.redsocial.model.Accion;
import ar.edu.uade.redsocial.tda.HistorialAccionesTDA;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del historial de acciones usando PilaTDA como estructura interna (LIFO).
 * - registrarAccion / deshacerUltimaAccion / hayAcciones: O(1).
 * - listarUltimas: O(n), desapila y re-apila para recorrer.
 * Capacidad máxima: 1000 acciones.
 */
public class StaticHistorialAccionesTDA implements HistorialAccionesTDA {

    private static final int CAPACIDAD = 1000;

    private PilaTDA pila;
    private Accion[] registro;
    private int nextIndex;

    public StaticHistorialAccionesTDA() {
        pila = new DynamicPilaTDA();
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
    public List<Accion> listarUltimas(int n) { // complejidad O(n), desapila y re-apila
        List<Accion> resultado = new ArrayList<>();
        PilaTDA temp = new DynamicPilaTDA();
        temp.InicializarPila();

        int count = 0;
        while (!pila.PilaVacia() && count < n) {
            int idx = pila.Tope();
            pila.Desapilar();
            resultado.add(registro[idx]);
            temp.Apilar(idx);
            count++;
        }

        // Restaurar la pila original
        while (!temp.PilaVacia()) {
            pila.Apilar(temp.Tope());
            temp.Desapilar();
        }

        return resultado;
    }
}
