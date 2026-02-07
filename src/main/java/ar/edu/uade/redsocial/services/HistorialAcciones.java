package ar.edu.uade.redsocial.services;

import ar.edu.uade.redsocial.implementation.StaticHistorialAccionesTDA;
import ar.edu.uade.redsocial.model.Accion;
import ar.edu.uade.redsocial.tda.HistorialAccionesTDA;

import java.util.List;

/**
 * Servicio de historial de acciones (deshacer). Delega en HistorialAccionesTDA (PilaTDA)
 * para LIFO O(1); mantiene la misma API pública.
 */
public class HistorialAcciones {

    private final HistorialAccionesTDA historialTDA;

    public HistorialAcciones() { // complejidad O(1)
        this.historialTDA = new StaticHistorialAccionesTDA();
    }

    public void registrarAccion(Accion accion) { // complejidad O(1)
        historialTDA.registrarAccion(accion);
    }

    public Accion deshacerUltimaAccion() { // complejidad O(1)
        return historialTDA.deshacerUltimaAccion();
    }

    public boolean hayAcciones() { // complejidad O(1)
        return historialTDA.hayAcciones();
    }

    /** Devuelve las ultimas n acciones (la mas reciente primero) sin modificar el historial. */
    public List<Accion> listarUltimas(int n) { // complejidad O(n)
        return historialTDA.listarUltimas(n);
    }
}
