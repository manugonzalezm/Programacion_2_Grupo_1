package ar.edu.uade.redsocial.implementation;

import ar.edu.uade.redsocial.model.SolicitudSeguimiento;
import ar.edu.uade.redsocial.tda.SolicitudesSeguimientoTDA;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementación eficiente de la cola de solicitudes usando LinkedList como cola (FIFO).
 * - agregarSolicitud / procesarSolicitud / haySolicitudes: O(1).
 * - quitarSolicitud: O(n), usa iterator.remove() sin reconstruir la cola.
 * - listarPendientes: O(n), itera directamente sin desacolar/re-acolar.
 * Capacidad máxima: 100 solicitudes.
 */
public class StaticSolicitudesSeguimientoTDA implements SolicitudesSeguimientoTDA {

    private static final int CAPACIDAD = 100;

    private final LinkedList<SolicitudSeguimiento> cola = new LinkedList<>();

    @Override
    public void agregarSolicitud(SolicitudSeguimiento solicitud) { // complejidad O(1)
        if (cola.size() >= CAPACIDAD) {
            throw new IllegalStateException("Cola de solicitudes llena");
        }
        cola.addLast(solicitud);
    }

    @Override
    public SolicitudSeguimiento procesarSolicitud() { // complejidad O(1)
        if (cola.isEmpty()) {
            return null;
        }
        return cola.removeFirst();
    }

    @Override
    public boolean haySolicitudes() { // complejidad O(1)
        return !cola.isEmpty();
    }

    @Override
    public boolean quitarSolicitud(SolicitudSeguimiento solicitud) { // complejidad O(n), sin reconstruir la cola
        Iterator<SolicitudSeguimiento> it = cola.iterator();
        while (it.hasNext()) {
            SolicitudSeguimiento s = it.next();
            if (s.getOrigen().equals(solicitud.getOrigen()) && s.getDestino().equals(solicitud.getDestino())) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    @Override
    public List<SolicitudSeguimiento> listarPendientes() { // complejidad O(n), sin modificar la cola
        return new ArrayList<>(cola);
    }
}
