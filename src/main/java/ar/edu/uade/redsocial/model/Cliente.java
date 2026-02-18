package ar.edu.uade.redsocial.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa a un cliente de la red social.
 *
 * Invariante de representación:
 * - nombre != null y no vacío.
 * - scoring >= 0.
 * - siguiendo != null (sin duplicados).
 * - conexiones != null (sin duplicados).
 * - solicitudesPendientes != null.
 * - Las listas se exponen como inmutables.
 */
public class Cliente {

    private String nombre;
    private int scoring;
    private List<String> siguiendo;
    private List<String> conexiones;
    private List<String> solicitudesPendientes;
    private List<Accion> acciones = new ArrayList<>();

    // 🔹 Constructor simple
    public Cliente(String nombre, int scoring) {
        this(nombre, scoring, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    // 🔹 Constructor intermedio (compatibilidad con tests viejos)
    public Cliente(String nombre,
                   int scoring,
                   List<String> siguiendo,
                   List<String> conexiones) {
        this(nombre, scoring, siguiendo, conexiones, new ArrayList<>());
    }

    // 🔹 Constructor completo
    public Cliente(String nombre,
                   int scoring,
                   List<String> siguiendo,
                   List<String> conexiones,
                   List<String> solicitudesPendientes) {

        this.nombre = nombre;
        this.scoring = scoring;

        this.siguiendo = siguiendo != null
                ? new ArrayList<>(siguiendo)
                : new ArrayList<>();

        this.conexiones = conexiones != null
                ? new ArrayList<>(conexiones)
                : new ArrayList<>();

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

    public List<String> getSiguiendo() {
        return Collections.unmodifiableList(siguiendo);
    }

    public List<String> getConexiones() {
        return Collections.unmodifiableList(conexiones);
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
        return "Cliente{" +
                "nombre='" + nombre + '\'' +
                ", scoring=" + scoring +
                ", siguiendo=" + siguiendo +
                ", conexiones=" + conexiones +
                ", solicitudesPendientes=" + solicitudesPendientes +
                '}';
    }
}
