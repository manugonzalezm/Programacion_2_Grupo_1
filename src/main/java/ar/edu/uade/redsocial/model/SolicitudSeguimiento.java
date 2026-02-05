package ar.edu.uade.redsocial.model;

// Representa una solicitud de seguimiento entre dos clientes
public class SolicitudSeguimiento {

    private String solicitante;
    private String solicitado;

    public SolicitudSeguimiento(String solicitante, String solicitado) {
        this.solicitante = solicitante;
        this.solicitado = solicitado;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public String getSolicitado() {
        return solicitado;
    }

    @Override
    public String toString() {
        return "SolicitudSeguimiento{" +
                "solicitante='" + solicitante + '\'' +
                ", solicitado='" + solicitado + '\'' +
                '}';
    }
}


