package ar.edu.uade.redsocial.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Representa a un cliente de la red social (campos alineados con clientes.json)
public class Cliente {

    private String nombre;
    private int scoring;
    private List<String> siguiendo;
    private List<String> conexiones;

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

    @Override
    public String toString() {
        return "Nombre='" + nombre + '\'' +
                ", Scoring=" + scoring +
                ", Siguiendo=" + siguiendo +
                ", Conexiones=" + conexiones;
    }
}
