package ar.edu.uade.redsocial.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa a un cliente de la red social.
 * Las relaciones de seguimiento y amistad NO se almacenan aquí:
 * son gestionadas por GrafoDirigido (seguimiento) y GrafoNoDirigido (amistades)
 * dentro de StaticClientesTDA, evitando duplicación de datos.
 *
 * Las solicitudes de amistad recibidas SÍ se almacenan aquí (por usuario),
 * usando un ArrayList ordenado por llegada para acceso por índice en O(1).
 *
 * Invariante de representación:
 * - nombre != null y no vacío.
 * - scoring >= 0.
 * - solicitudesRecibidas != null (se expone como inmutable).
 */
public class Cliente {

    private final String nombre;
    private final int scoring;
    private final List<Accion> acciones = new ArrayList<>();
    private final List<SolicitudSeguimiento> solicitudesRecibidas = new ArrayList<>();

    public Cliente(String nombre, int scoring) {
        this.nombre = nombre;
        this.scoring = scoring;
    }

    public String getNombre() {
        return nombre;
    }

    public int getScoring() {
        return scoring;
    }

    /** Vista inmutable de las solicitudes de amistad pendientes para este usuario. */
    public List<SolicitudSeguimiento> getSolicitudesRecibidas() {
        return Collections.unmodifiableList(solicitudesRecibidas);
    }

    /** Agrega una solicitud recibida al final de la lista. O(1) */
    public void agregarSolicitudRecibida(SolicitudSeguimiento s) {
        solicitudesRecibidas.add(s);
    }

    /**
     * Elimina y devuelve la solicitud en la posición indicada (0-based).
     * Devuelve null si el índice está fuera de rango. O(n) — ArrayList.remove(int)
     */
    public SolicitudSeguimiento eliminarSolicitudRecibidaPorIndice(int indice) {
        if (indice < 0 || indice >= solicitudesRecibidas.size()) return null;
        return solicitudesRecibidas.remove(indice);
    }

    /**
     * Elimina la primera solicitud que coincide por origen y destino.
     * Devuelve true si se encontró y eliminó. O(n)
     */
    public boolean eliminarSolicitudRecibida(SolicitudSeguimiento s) {
        return solicitudesRecibidas.removeIf(
                r -> r.getOrigen().equals(s.getOrigen()) && r.getDestino().equals(s.getDestino()));
    }

    public void agregarAccion(Accion accion) {
        acciones.add(accion);
    }

    public List<Accion> getAcciones() {
        return Collections.unmodifiableList(acciones);
    }

    @Override
    public String toString() {
        return "Cliente{nombre='" + nombre + "', scoring=" + scoring + '}';
    }
}
