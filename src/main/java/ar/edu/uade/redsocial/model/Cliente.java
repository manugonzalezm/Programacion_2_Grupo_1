package ar.edu.uade.redsocial.model;

// Representa a un cliente de la red social
public class Cliente {

    private String nombre;
    private int scoring;

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

    @Override
    public String toString() {
        return "Cliente{" +
                "nombre='" + nombre + '\'' +
                ", scoring=" + scoring +
                '}';
    }
}
