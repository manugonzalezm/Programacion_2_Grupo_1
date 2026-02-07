package ar.edu.uade.redsocial.tda;

import ar.edu.uade.redsocial.model.Accion;

/**
 * TDA para el historial de acciones (deshacer).
 * Replica la API de TDAHistorialAcciones; implementaciones usan PilaTDA para LIFO O(1).
 */
public interface HistorialAccionesTDA {

    void registrarAccion(Accion accion); // complejidad O(1)

    /** Desapila y devuelve la última acción, o null si no hay acciones. */
    Accion deshacerUltimaAccion(); // complejidad O(1)

    boolean hayAcciones(); // complejidad O(1)

    /** Devuelve las ultimas N acciones (la mas reciente primero) sin modificar la pila. */
    java.util.List<Accion> listarUltimas(int n); // complejidad O(n)
}
