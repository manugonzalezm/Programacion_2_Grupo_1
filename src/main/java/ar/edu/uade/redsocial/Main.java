package ar.edu.uade.redsocial; // O(1)

import ar.edu.uade.redsocial.model.Accion; // O(1)
import ar.edu.uade.redsocial.model.SolicitudSeguimiento; // O(1)
import ar.edu.uade.redsocial.services.CargadorClientesJson; // O(1)
import ar.edu.uade.redsocial.services.ColaSolicitudesSeguimiento; // O(1)
import ar.edu.uade.redsocial.services.GestorClientes; // O(1)
import ar.edu.uade.redsocial.services.HistorialAcciones; // O(1)
import ar.edu.uade.redsocial.utils.MenuRedSocial; // O(1)

import java.util.Scanner; // O(1)

public class Main {

    public static void main(String[] args) {

        // Crear servicios principales
        GestorClientes gestorClientes = new GestorClientes(); // O(1)
        HistorialAcciones historial = new HistorialAcciones(); // O(1)
        ColaSolicitudesSeguimiento colaSolicitudes = new ColaSolicitudesSeguimiento(); // O(1)

        // Cargar clientes desde JSON
        CargadorClientesJson.readFromFile(gestorClientes); // O(n log n)

        // Crear scanner
        Scanner scanner = new Scanner(System.in); // O(1)

        // Crear menú principal
        MenuRedSocial menuRedSocial =
                new MenuRedSocial(scanner, gestorClientes, historial, colaSolicitudes); // O(1)

        // Ejecutar sistema (bucle principal)
        menuRedSocial.ejecutar(); // O(t*(p + A)) + O(n)

        // Cerrar recursos
        scanner.close(); // O(1)
    }
}