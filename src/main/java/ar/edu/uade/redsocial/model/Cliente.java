package ar.edu.uade.redsocial.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Representa a un cliente de la red social.
 * Las relaciones de seguimiento y amistad NO se almacenan aquí:
 * son gestionadas por GrafoDirigido (seguimiento) y GrafoNoDirigido (amistades)
 * dentro de StaticClientesTDA, evitando duplicación de datos.
 *
 * Invariante de representación:
 * - nombre != null y no vacío.
 * - scoring >= 0.
 * - solicitudesPendientes != null (se expone como inmutable).
 */
public class Cliente {

    private final String nombre;
    private final int scoring;
    private final List<String> solicitudesPendientes;
    private final List<Accion> acciones = new ArrayList<>();
    private Set<String> siguiendo = new HashSet<>();
    private Set<String> conexiones = new HashSet<>();

    public Set<String> getSiguiendo() {
        return siguiendo;
    }

    public Set<String> getConexiones() {
        return conexiones;
    }

    public Cliente(String nombre, int scoring) {
        this(nombre, scoring, new ArrayList<>());
    }

    public Cliente(String nombre, int scoring, List<String> solicitudesPendientes) {
        this.nombre = nombre;
        this.scoring = scoring;
        this.solicitudesPendientes = solicitudesPendientes != null
                ? new ArrayList<>(solicitudesPendientes)
                : new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public int getScoring() {
        return scoring;
    }

    public List<String> getSolicitudesPendientes() {
        return Collections.unmodifiableList(solicitudesPendientes);
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
