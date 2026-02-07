package ar.edu.uade.redsocial.tda;

import ar.edu.uade.redsocial.model.SolicitudSeguimiento;

/**
 * TDA para manejar solicitudes de seguimiento en orden FIFO.
 * Replica la API de TDASolicitudesSeguimiento; implementaciones usan ColaTDA para FIFO O(1).
 */
public interface SolicitudesSeguimientoTDA {

    void agregarSolicitud(SolicitudSeguimiento solicitud); // complejidad O(1)

    /** Desacola y devuelve la próxima solicitud, o null si no hay solicitudes. */
    SolicitudSeguimiento procesarSolicitud(); // complejidad O(1)

    boolean haySolicitudes(); // complejidad O(1)

    /**
     * Quita una solicitud de la cola (una ocurrencia que coincida por origen y destino).
     * Retorna true si se encontró y eliminó. Usado para deshacer "Seguir cliente".
     */
    boolean quitarSolicitud(SolicitudSeguimiento solicitud); // complejidad O(n)

    /** Devuelve una lista con todas las solicitudes pendientes sin modificar la cola. */
    java.util.List<SolicitudSeguimiento> listarPendientes(); // complejidad O(n)
}
