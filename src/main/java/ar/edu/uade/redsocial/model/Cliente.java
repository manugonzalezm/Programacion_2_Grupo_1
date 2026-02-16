package ar.edu.uade.redsocial.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa a un cliente de la red social (campos alineados con clientes.json).
 *
 * Invariante de representación:
 * - nombre != null y no vacío. Identifica de forma única al cliente.
 * - scoring >= 0. Representa la puntuación del cliente.
 * - siguiendo != null. Lista de nombres de clientes a los que sigue (sin duplicados).
 * - conexiones != null. Lista de nombres de clientes conectados (sin duplicados).
 * - Las listas se exponen como inmutables (Collections.unmodifiableList) para evitar modificación externa.
 * - Un cliente no puede seguirse a sí mismo (su nombre no aparece en siguiendo).
 */
public class Cliente {

    private String nombre;
    private int scoring;
    private List<String> siguiendo;
    private List<String> conexiones;
    private List<Accion> acciones = new ArrayList<>();
    /*cada cliente tiene:
        Historial global (pila)
        Historial propio   */

    public Cliente(String nombre, int scoring) {
        this(nombre, scoring, new ArrayList<>(), new ArrayList<>());
    }

    public Cliente(String nombre, int scoring, List<String> siguiendo, List<String> conexiones) {
        this.nombre = nombre;
        this.scoring = scoring;
        this.siguiendo = siguiendo != null ? new ArrayList<>(siguiendo) : new ArrayList<>();
        this.conexiones = conexiones != null ? new ArrayList<>(conexiones) : new ArrayList<>();
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

    public void agregarAccion(Accion accion) {
        acciones.add(accion);
    }

    public List<Accion> getAcciones() {
        return Collections.unmodifiableList(acciones);
    }


    @Override
    public String toString() {
        return "Nombre='" + nombre + '\'' +
                ", Scoring=" + scoring +
                ", Siguiendo=" + siguiendo +
                ", Conexiones=" + conexiones;
    }
}
