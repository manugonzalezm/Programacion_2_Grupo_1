package ar.edu.uade.redsocial;

import ar.edu.uade.redsocial.model.Accion;
import ar.edu.uade.redsocial.model.SolicitudSeguimiento;
import ar.edu.uade.redsocial.services.CargadorClientesJson;
import ar.edu.uade.redsocial.services.ColaSolicitudesSeguimiento;
import ar.edu.uade.redsocial.services.GestorClientes;
import ar.edu.uade.redsocial.services.HistorialAcciones;
import ar.edu.uade.redsocial.utils.MenuRedSocial;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // Una sola instancia de cada servicio (todas respaldadas por TDAs: Cola, Pila, Diccionario)
        GestorClientes gestorClientes = new GestorClientes();
        HistorialAcciones historial = new HistorialAcciones();
        ColaSolicitudesSeguimiento colaSolicitudes = new ColaSolicitudesSeguimiento();

        CargadorClientesJson.readFromFile(gestorClientes);

        Scanner scanner = new Scanner(System.in);
        MenuRedSocial menuRedSocial = new MenuRedSocial(scanner, gestorClientes, historial, colaSolicitudes);
        menuRedSocial.ejecutar();

        scanner.close();
    }
}
