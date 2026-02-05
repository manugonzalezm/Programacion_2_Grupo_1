package ar.edu.uade.redsocial.implementation;

import ar.edu.uade.redsocial.basic_tdas.implementation.StaticColaTDA;
import ar.edu.uade.redsocial.basic_tdas.tda.ColaTDA;
import ar.edu.uade.redsocial.model.SolicitudSeguimiento;
import ar.edu.uade.redsocial.tda.SolicitudesSeguimientoTDA;

public class StaticSolicitudesSeguimientoTDA implements SolicitudesSeguimientoTDA {

    private ColaTDA cola;

    // Registro de solicitudes
    private SolicitudSeguimiento[] registro;
    private int contador;

    public void InicializarSolicitudes() {
        cola = new StaticColaTDA();
        cola.InicializarCola();

        registro = new SolicitudSeguimiento[1000];
        contador = 1;
    }

    public void AgregarSolicitud(SolicitudSeguimiento s) {

        registro[contador] = s;

        cola.Acolar(contador);

        contador++;
    }

    public void ProcesarSolicitud() {
        cola.Desacolar();
    }

    public SolicitudSeguimiento ProximaSolicitud() {

        int id = cola.Primero();

        return registro[id];
    }

    public boolean SinSolicitudes() {
        return cola.ColaVacia();
    }
}

