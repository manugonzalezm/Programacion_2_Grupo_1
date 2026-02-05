package ar.edu.uade.redsocial.tda;

import ar.edu.uade.redsocial.model.SolicitudSeguimiento;

// TDA para manejar solicitudes de seguimiento
public interface TDASolicitudesSeguimiento {

    void agregarSolicitud(SolicitudSeguimiento solicitud);

    SolicitudSeguimiento procesarSolicitud();

    boolean haySolicitudes();
}