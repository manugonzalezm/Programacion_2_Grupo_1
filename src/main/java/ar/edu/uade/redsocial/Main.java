package ar.edu.uade.redsocial;

import ar.edu.uade.redsocial.services.CargadorClientesJson;
import ar.edu.uade.redsocial.services.GestorClientes;
import ar.edu.uade.redsocial.services.HistorialAcciones;
import ar.edu.uade.redsocial.utils.MenuRedSocial;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        GestorClientes gestorClientes = new GestorClientes();
        HistorialAcciones historial = new HistorialAcciones();

        CargadorClientesJson.readFromFile(gestorClientes);

        Scanner scanner = new Scanner(System.in);
        MenuRedSocial menuRedSocial = new MenuRedSocial(scanner, gestorClientes, historial);
        menuRedSocial.ejecutar();

        scanner.close();
    }
}
