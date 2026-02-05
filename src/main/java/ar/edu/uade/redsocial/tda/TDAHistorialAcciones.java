package ar.edu.uade.redsocial.tda;

import ar.edu.uade.redsocial.model.Accion;

// TDA para el historial de acciones
public interface TDAHistorialAcciones {

    void registrarAccion(Accion accion);

    Accion deshacerUltimaAccion();

    boolean hayAcciones();
}
