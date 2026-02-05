package ar.edu.uade.redsocial.model;

import java.time.LocalDateTime;

// Representa una acción realizada en el sistema
public class Accion {

    private String tipo;
    private String detalle;
    private LocalDateTime fechaHora;

    public Accion(String tipo, String detalle) {
        this.tipo = tipo;
        this.detalle = detalle;
        this.fechaHora = LocalDateTime.now();
    }

    public String getTipo() {
        return tipo;
    }

    public String getDetalle() {
        return detalle;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    @Override
    public String toString() {
        return "Accion{" +
                "tipo='" + tipo + '\'' +
                ", detalle='" + detalle + '\'' +
                ", fechaHora=" + fechaHora +
                '}';
    }
}
