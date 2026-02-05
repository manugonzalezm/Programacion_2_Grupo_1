package ar.edu.uade.redsocial.services;

import ar.edu.uade.redsocial.model.SolicitudSeguimiento;
import ar.edu.uade.redsocial.tda.TDASolicitudesSeguimiento;

import java.util.LinkedList;
import java.util.Queue;

// Implementación de la cola de solicitudes de seguimiento
public class ColaSolicitudesSeguimiento implements TDASolicitudesSeguimiento {

    private Queue<SolicitudSeguimiento> solicitudes = new LinkedList<>();

    @Override
    public void agregarSolicitud(SolicitudSeguimiento solicitud) {
        solicitudes.add(solicitud);
    }

    @Override
    public SolicitudSeguimiento procesarSolicitud() {
        return solicitudes.poll();
    }

    @Override
    public boolean haySolicitudes() {
        return !solicitudes.isEmpty();
    }
}
