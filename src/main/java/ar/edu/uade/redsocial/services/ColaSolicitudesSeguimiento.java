package ar.edu.uade.redsocial.services;

import ar.edu.uade.redsocial.model.SolicitudSeguimiento;

import java.util.LinkedList;
import java.util.Queue;

public class ColaSolicitudesSeguimiento {

    private Queue<SolicitudSeguimiento> solicitudes = new LinkedList<>();

    public void agregarSolicitud(SolicitudSeguimiento solicitud) {
        solicitudes.add(solicitud);
    }

    public SolicitudSeguimiento procesarSolicitud() {
        return solicitudes.poll();
    }

    public boolean haySolicitudes() {
        return !solicitudes.isEmpty();
    }
}
