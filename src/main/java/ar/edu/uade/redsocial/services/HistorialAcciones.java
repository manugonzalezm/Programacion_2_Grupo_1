package ar.edu.uade.redsocial.services;

import ar.edu.uade.redsocial.model.Accion;

import java.util.Stack;

public class HistorialAcciones {

    private Stack<Accion> acciones = new Stack<>();

    public void registrarAccion(Accion accion) {
        acciones.push(accion);
    }

    public Accion deshacerUltimaAccion() {
        if (acciones.isEmpty()) {
            return null;
        }
        return acciones.pop();
    }

    public boolean hayAcciones() {
        return !acciones.isEmpty();
    }
}
