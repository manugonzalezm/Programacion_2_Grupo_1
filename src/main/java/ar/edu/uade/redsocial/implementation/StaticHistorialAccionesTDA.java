package ar.edu.uade.redsocial.implementation;

import ar.edu.uade.redsocial.basic_tdas.implementation.StaticPilaTDA;
import ar.edu.uade.redsocial.basic_tdas.tda.PilaTDA;
import ar.edu.uade.redsocial.tda.HistorialAccionesTDA;

public class StaticHistorialAccionesTDA implements HistorialAccionesTDA {

    private PilaTDA pila;

    public void Inicializar() {
        pila = new StaticPilaTDA();
        pila.InicializarPila();
    }

    public void RegistrarAccion(int accion) {
        pila.Apilar(accion);
    }

    public void DeshacerUltimaAccion() {
        pila.Desapilar();
    }

    public int UltimaAccion() {
        return pila.Tope();
    }

    public boolean Vacio() {
        return pila.PilaVacia();
    }

}
