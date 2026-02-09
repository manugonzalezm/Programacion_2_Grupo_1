package ar.edu.uade.redsocial.services;

import ar.edu.uade.redsocial.implementation.StaticSolicitudesSeguimientoTDA;
import ar.edu.uade.redsocial.model.SolicitudSeguimiento;
import ar.edu.uade.redsocial.tda.SolicitudesSeguimientoTDA;

import java.util.List;

/**
 * Servicio de cola de solicitudes de seguimiento. Delega en SolicitudesSeguimientoTDA (LinkedList)
 * para FIFO O(1); mantiene la misma API pública.
 */
public class ColaSolicitudesSeguimiento {

    private final SolicitudesSeguimientoTDA solicitudesTDA;

    public ColaSolicitudesSeguimiento() { // complejidad O(1)
        this.solicitudesTDA = new StaticSolicitudesSeguimientoTDA();
    }

    public void agregarSolicitud(SolicitudSeguimiento solicitud) { // complejidad O(1)
        solicitudesTDA.agregarSolicitud(solicitud);
    }

    public SolicitudSeguimiento procesarSolicitud() { // complejidad O(1)
        return solicitudesTDA.procesarSolicitud();
    }

    public boolean haySolicitudes() { // complejidad O(1)
        return solicitudesTDA.haySolicitudes();
    }

    /** Quita una solicitud de la cola. Usado al deshacer "Seguir cliente". */
    public boolean quitarSolicitud(SolicitudSeguimiento solicitud) { // complejidad O(n), n = solicitudes en cola
        return solicitudesTDA.quitarSolicitud(solicitud);
    }

    /** Devuelve lista de solicitudes pendientes sin modificar la cola. */
    public List<SolicitudSeguimiento> listarPendientes() { // complejidad O(n)
        return solicitudesTDA.listarPendientes();
    }
}
