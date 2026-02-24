package ar.edu.uade.redsocial.model;

/**
 * Representa una solicitud de amistad entre dos clientes.
 * Cuando es aceptada, crea un vínculo bidireccional en el grafo de amistades.
 *
 * Invariante de representación:
 * - origen != null y no vacío. Nombre del cliente que envía la solicitud.
 * - destino != null y no vacío. Nombre del cliente que la recibe.
 * - origen != destino. Un cliente no puede enviarse una solicitud a sí mismo.
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
