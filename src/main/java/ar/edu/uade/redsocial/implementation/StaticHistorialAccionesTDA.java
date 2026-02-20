package ar.edu.uade.redsocial.implementation;

import ar.edu.uade.redsocial.basic_tdas.implementation.DynamicPilaTDA;
import ar.edu.uade.redsocial.basic_tdas.tda.PilaTDA;
import ar.edu.uade.redsocial.model.Accion;
import ar.edu.uade.redsocial.tda.HistorialAccionesTDA;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del historial de acciones usando PilaTDA genérico (LIFO).
 * Gracias a los genéricos, almacena Accion directamente sin mapeo de índices.
 */
public class StaticHistorialAccionesTDA implements HistorialAccionesTDA {

    private PilaTDA<Accion> pila;

    public StaticHistorialAccionesTDA() {
        pila = new DynamicPilaTDA<>();
        pila.InicializarPila();
    }

    @Override
    public void registrarAccion(Accion accion) { // complejidad O(1)
        pila.Apilar(accion);
    }

    @Override
    public Accion deshacerUltimaAccion() { // complejidad O(1)
        if (pila.PilaVacia()) {
            return null;
        }
        Accion accion = pila.Tope();
        pila.Desapilar();
        return accion;
    }

    @Override
    public boolean hayAcciones() { // complejidad O(1)
        return !pila.PilaVacia();
    }

    @Override
    public List<Accion> listarUltimas(int n) { // complejidad O(n)
        List<Accion> resultado = new ArrayList<>();
        PilaTDA<Accion> temp = new DynamicPilaTDA<>();
        temp.InicializarPila();

        int count = 0;
        while (!pila.PilaVacia() && count < n) {
            Accion accion = pila.Tope();
            pila.Desapilar();
            resultado.add(accion);
            temp.Apilar(accion);
            count++;
        }

        while (!temp.PilaVacia()) {
            pila.Apilar(temp.Tope());
            temp.Desapilar();
        }

        return resultado;
    }
}
