package ar.edu.uade.redsocial.implementation;

import ar.edu.uade.redsocial.basic_tdas.implementation.DynamicColaTDA;
import ar.edu.uade.redsocial.basic_tdas.tda.ColaTDA;
import ar.edu.uade.redsocial.model.SolicitudSeguimiento;
import ar.edu.uade.redsocial.tda.SolicitudesSeguimientoTDA;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de la cola de solicitudes usando ColaTDA genérico (FIFO).
 * Gracias a los genéricos, almacena SolicitudSeguimiento directamente sin mapeo de índices.
 */
public class StaticSolicitudesSeguimientoTDA implements SolicitudesSeguimientoTDA {

    private ColaTDA<SolicitudSeguimiento> cola;

    public StaticSolicitudesSeguimientoTDA() {
        cola = new DynamicColaTDA<>();
        cola.InicializarCola();
    }

    @Override
    public void agregarSolicitud(SolicitudSeguimiento solicitud) { // complejidad O(1)
        cola.Acolar(solicitud);
    }

    @Override
    public SolicitudSeguimiento procesarSolicitud() { // complejidad O(1)
        if (cola.ColaVacia()) {
            return null;
        }
        SolicitudSeguimiento s = cola.Primero();
        cola.Desacolar();
        return s;
    }

    @Override
    public boolean haySolicitudes() { // complejidad O(1)
        return !cola.ColaVacia();
    }

    @Override
    public boolean quitarSolicitud(SolicitudSeguimiento solicitud) { // complejidad O(n)
        ColaTDA<SolicitudSeguimiento> temp = new DynamicColaTDA<>();
        temp.InicializarCola();
        boolean encontrada = false;

        while (!cola.ColaVacia()) {
            SolicitudSeguimiento s = cola.Primero();
            cola.Desacolar();
            if (!encontrada && s.getOrigen().equals(solicitud.getOrigen())
                    && s.getDestino().equals(solicitud.getDestino())) {
                encontrada = true;
            } else {
                temp.Acolar(s);
            }
        }

        while (!temp.ColaVacia()) {
            cola.Acolar(temp.Primero());
            temp.Desacolar();
        }

        return encontrada;
    }

    @Override
    public List<SolicitudSeguimiento> listarPendientes() { // complejidad O(n)
        List<SolicitudSeguimiento> resultado = new ArrayList<>();
        ColaTDA<SolicitudSeguimiento> temp = new DynamicColaTDA<>();
        temp.InicializarCola();

        while (!cola.ColaVacia()) {
            SolicitudSeguimiento s = cola.Primero();
            cola.Desacolar();
            resultado.add(s);
            temp.Acolar(s);
        }

        while (!temp.ColaVacia()) {
            cola.Acolar(temp.Primero());
            temp.Desacolar();
        }

        return resultado;
    }

    @Override
    public SolicitudSeguimiento procesarSolicitudParaUsuario(String nombreUsuario) { // complejidad O(n)
        ColaTDA<SolicitudSeguimiento> temp = new DynamicColaTDA<>();
        temp.InicializarCola();
        SolicitudSeguimiento encontrada = null;

        while (!cola.ColaVacia()) {
            SolicitudSeguimiento s = cola.Primero();
            cola.Desacolar();
            if (encontrada == null && s.getDestino().equals(nombreUsuario)) {
                encontrada = s;
            } else {
                temp.Acolar(s);
            }
        }

        while (!temp.ColaVacia()) {
            cola.Acolar(temp.Primero());
            temp.Desacolar();
        }

        return encontrada;
    }

    @Override
    public List<SolicitudSeguimiento> listarPendientesParaUsuario(String nombreUsuario) { // complejidad O(n)
        List<SolicitudSeguimiento> resultado = new ArrayList<>();
        ColaTDA<SolicitudSeguimiento> temp = new DynamicColaTDA<>();
        temp.InicializarCola();

        while (!cola.ColaVacia()) {
            SolicitudSeguimiento s = cola.Primero();
            cola.Desacolar();
            if (s.getOrigen().equals(nombreUsuario) || s.getDestino().equals(nombreUsuario)) {
                resultado.add(s);
            }
            temp.Acolar(s);
        }

        while (!temp.ColaVacia()) {
            cola.Acolar(temp.Primero());
            temp.Desacolar();
        }

        return resultado;
    }
}
