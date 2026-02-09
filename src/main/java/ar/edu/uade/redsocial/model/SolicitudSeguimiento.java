package ar.edu.uade.redsocial.model;

/**
 * Representa una solicitud de seguimiento entre dos clientes.
 *
 * Invariante de representación:
 * - origen != null y no vacío. Nombre del cliente que quiere seguir a otro.
 * - destino != null y no vacío. Nombre del cliente al que se quiere seguir.
 * - origen != destino. Un cliente no puede solicitar seguirse a sí mismo.
 * - Una vez creada, la solicitud es inmutable (no existen setters).
 */
public class SolicitudSeguimiento {

    private String origen;
    private String destino;

    public SolicitudSeguimiento(String origen, String destino) {
        this.origen = origen;
        this.destino = destino;
    }

    public String getOrigen() {
        return origen;
    }

    public String getDestino() {
        return destino;
    }

    /** Alias para uso en menú/historial: quien solicita seguir. */
    public String getSolicitante() {
        return origen;
    }

    /** Alias para uso en menú/historial: a quién se solicita seguir. */
    public String getSolicitado() {
        return destino;
    }

    @Override
    public String toString() {
        return origen + " -> " + destino;
    }
}
