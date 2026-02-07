package ar.edu.uade.redsocial.implementation;

import ar.edu.uade.redsocial.basic_tdas.implementation.StaticColaTDA;
import ar.edu.uade.redsocial.basic_tdas.tda.ColaTDA;
import ar.edu.uade.redsocial.model.SolicitudSeguimiento;
import ar.edu.uade.redsocial.tda.SolicitudesSeguimientoTDA;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementación estática de la cola de solicitudes usando ColaTDA (FIFO).
 * Acolar/Desacolar/Primero O(1). Se guardan índices en la cola y registro[] para las solicitudes.
 */
public class StaticSolicitudesSeguimientoTDA implements SolicitudesSeguimientoTDA {

    private static final int CAPACIDAD = 1000;

    private ColaTDA cola;
    private SolicitudSeguimiento[] registro;
    private int nextIndex;

    public StaticSolicitudesSeguimientoTDA() { // complejidad O(1)
        cola = new StaticColaTDA();
        cola.InicializarCola();
        registro = new SolicitudSeguimiento[CAPACIDAD];
        nextIndex = 0;
    }

    @Override
    public void agregarSolicitud(SolicitudSeguimiento solicitud) { // complejidad O(1)
        if (nextIndex >= CAPACIDAD) {
            throw new IllegalStateException("Cola de solicitudes llena");
        }
        registro[nextIndex] = solicitud;
        cola.Acolar(nextIndex);
        nextIndex++;
    }

    @Override
    public SolicitudSeguimiento procesarSolicitud() { // complejidad O(1)
        if (cola.ColaVacia()) {
            return null;
        }
        int id = cola.Primero();
        cola.Desacolar();
        return registro[id];
    }

    @Override
    public boolean haySolicitudes() { // complejidad O(1)
        return !cola.ColaVacia();
    }

    @Override
    public boolean quitarSolicitud(SolicitudSeguimiento solicitud) { // complejidad O(n), n = solicitudes en cola
        List<SolicitudSeguimiento> pendientes = new ArrayList<>();
        while (!cola.ColaVacia()) {
            pendientes.add(registro[cola.Primero()]);
            cola.Desacolar();
        }
        boolean removida = false;
        for (int i = 0; i < pendientes.size(); i++) {
            SolicitudSeguimiento t = pendientes.get(i);
            if (t.getOrigen().equals(solicitud.getOrigen()) && t.getDestino().equals(solicitud.getDestino())) {
                pendientes.remove(i);
                removida = true;
                break;
            }
        }
        cola = new StaticColaTDA();
        cola.InicializarCola();
        registro = new SolicitudSeguimiento[CAPACIDAD];
        nextIndex = 0;
        for (SolicitudSeguimiento s : pendientes) {
            agregarSolicitud(s);
        }
        return removida;
    }

    @Override
    public List<SolicitudSeguimiento> listarPendientes() { // complejidad O(n), n = solicitudes en cola
        List<SolicitudSeguimiento> pendientes = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        while (!cola.ColaVacia()) {
            int idx = cola.Primero();
            indices.add(idx);
            pendientes.add(registro[idx]);
            cola.Desacolar();
        }
        // Reconstruir la cola sin modificarla
        cola = new StaticColaTDA();
        cola.InicializarCola();
        for (int idx : indices) {
            cola.Acolar(idx);
        }
        return pendientes;
    }
}
