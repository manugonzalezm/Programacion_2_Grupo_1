package ar.edu.uade.redsocial.services;

import ar.edu.uade.redsocial.implementation.StaticSolicitudesSeguimientoTDA;
import ar.edu.uade.redsocial.model.SolicitudSeguimiento;
import ar.edu.uade.redsocial.tda.SolicitudesSeguimientoTDA;

import java.util.List;

/**
 * Maneja la cola de solicitudes de amistad pendientes.
 * Las solicitudes se procesan en orden de llegada (FIFO).
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

    /** Quita una solicitud de la cola. Usado al deshacer "Enviar solicitud amistad". */
    public boolean quitarSolicitud(SolicitudSeguimiento solicitud) { // complejidad O(n), n = solicitudes en cola
        return solicitudesTDA.quitarSolicitud(solicitud);
    }

    /** Devuelve lista de solicitudes pendientes sin modificar la cola. */
    public List<SolicitudSeguimiento> listarPendientes() { // complejidad O(n)
        return solicitudesTDA.listarPendientes();
    }

    /**
     * Desacola y devuelve la primera solicitud cuyo destino sea el usuario indicado,
     * o null si no existe ninguna.
     */
    public SolicitudSeguimiento procesarSolicitudParaUsuario(String nombreUsuario) { // complejidad O(n)
        return solicitudesTDA.procesarSolicitudParaUsuario(nombreUsuario);
    }

    /** Devuelve solicitudes pendientes donde el usuario es origen o destino, sin modificar la cola. */
    public List<SolicitudSeguimiento> listarPendientesParaUsuario(String nombreUsuario) { // complejidad O(n)
        return solicitudesTDA.listarPendientesParaUsuario(nombreUsuario);
    }
}
