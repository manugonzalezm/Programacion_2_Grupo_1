package ar.edu.uade.redsocial.implementation;

import ar.edu.uade.redsocial.basic_tdas.implementation.DynamicColaTDA;
import ar.edu.uade.redsocial.basic_tdas.tda.ColaTDA;
import ar.edu.uade.redsocial.model.SolicitudSeguimiento;
import ar.edu.uade.redsocial.tda.SolicitudesSeguimientoTDA;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de la cola de solicitudes usando ColaTDA como estructura interna (FIFO).
 * - agregarSolicitud / procesarSolicitud / haySolicitudes: O(1).
 * - quitarSolicitud: O(n), desacola y re-acola sin la solicitud encontrada.
 * - listarPendientes: O(n), desacola y re-acola para recorrer.
 * Capacidad máxima: 100 solicitudes.
 */
public class StaticSolicitudesSeguimientoTDA implements SolicitudesSeguimientoTDA {

    private static final int CAPACIDAD = 100;

    private ColaTDA cola;
    private SolicitudSeguimiento[] registro;
    private int nextIndex;

    public StaticSolicitudesSeguimientoTDA() {
        cola = new DynamicColaTDA();
        cola.InicializarCola();
        registro = new SolicitudSeguimiento[CAPACIDAD];
        nextIndex = 0;
    }

    @Override
    public void agregarSolicitud(SolicitudSeguimiento solicitud) { // complejidad O(1)
        if (nextIndex >= CAPACIDAD) {
            throw new IllegalStateException("Cola de solicitudes llena");
        }
        registro[nextIndex] = solicitud;
        cola.Acolar(nextIndex);
        nextIndex++;
    }

    @Override
    public SolicitudSeguimiento procesarSolicitud() { // complejidad O(1)
        if (cola.ColaVacia()) {
            return null;
        }
        int idx = cola.Primero();
        cola.Desacolar();
        return registro[idx];
    }

    @Override
    public boolean haySolicitudes() { // complejidad O(1)
        return !cola.ColaVacia();
    }

    @Override
    public boolean quitarSolicitud(SolicitudSeguimiento solicitud) { // complejidad O(n), desacola y re-acola
        ColaTDA temp = new DynamicColaTDA();
        temp.InicializarCola();
        boolean encontrada = false;

        // Desacolar todo, salteando la primera coincidencia
        while (!cola.ColaVacia()) {
            int idx = cola.Primero();
            cola.Desacolar();
            SolicitudSeguimiento s = registro[idx];
            if (!encontrada && s.getOrigen().equals(solicitud.getOrigen())
                    && s.getDestino().equals(solicitud.getDestino())) {
                encontrada = true;
            } else {
                temp.Acolar(idx);
            }
        }

        // Restaurar la cola sin el elemento eliminado
        while (!temp.ColaVacia()) {
            cola.Acolar(temp.Primero());
            temp.Desacolar();
        }

        return encontrada;
    }

    @Override
    public List<SolicitudSeguimiento> listarPendientes() { // complejidad O(n), desacola y re-acola
        List<SolicitudSeguimiento> resultado = new ArrayList<>();
        ColaTDA temp = new DynamicColaTDA();
        temp.InicializarCola();

        while (!cola.ColaVacia()) {
            int idx = cola.Primero();
            cola.Desacolar();
            resultado.add(registro[idx]);
            temp.Acolar(idx);
        }

        // Restaurar la cola
        while (!temp.ColaVacia()) {
            cola.Acolar(temp.Primero());
            temp.Desacolar();
        }

        return resultado;
    }
}
