package ar.edu.uade.redsocial.model;

import java.time.LocalDateTime;

/**
 * Representa una acción realizada en el sistema.
 *
 * Invariante de representación:
 * - tipo != null. Identifica la categoría de la acción (ej: "Agregar cliente", "Buscar por nombre").
 * - detalle != null. Información adicional sobre la acción realizada.
 * - fechaHora != null. Se asigna automáticamente al momento de la creación (LocalDateTime.now()).
 * - Una vez creada, la acción es inmutable (no existen setters).
 */
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
