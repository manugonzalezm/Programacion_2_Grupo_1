package ar.edu.uade.redsocial.tda;

import ar.edu.uade.redsocial.model.SolicitudSeguimiento;

public interface SolicitudesSeguimientoTDA {

    void InicializarSolicitudes();

    void AgregarSolicitud(SolicitudSeguimiento s);

    void ProcesarSolicitud();

    SolicitudSeguimiento ProximaSolicitud();

    boolean SinSolicitudes();
}

