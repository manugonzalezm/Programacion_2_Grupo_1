package ar.edu.uade.redsocial.services;

import ar.edu.uade.redsocial.model.Accion;
import ar.edu.uade.redsocial.tda.TDAHistorialAcciones;

import java.util.Stack;

// Implementación del historial de acciones usando una pila
public class HistorialAcciones implements TDAHistorialAcciones {

    private Stack<Accion> acciones = new Stack<>();

    @Override
    public void registrarAccion(Accion accion) {
        acciones.push(accion);
    }

    @Override
    public Accion deshacerUltimaAccion() {
        if (acciones.isEmpty()) {
            return null;
        }
        return acciones.pop();
    }

    @Override
    public boolean hayAcciones() {
        return !acciones.isEmpty();
    }
}
