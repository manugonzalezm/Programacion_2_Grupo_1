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
        GestorClientes gestorClientes = new GestorClientes();
        CargadorClientesJson.readFromFile(gestorClientes);

        System.out.println("Buscar por nombre Alice:");
        System.out.println(gestorClientes.buscarPorNombre("Alice"));

        System.out.println("\nBuscar por scoring 88:");
        System.out.println(gestorClientes.buscarPorScoring(88));

        HistorialAcciones historial = new HistorialAcciones();
        historial.registrarAccion(new Accion("Agregar cliente", "Alice"));
        historial.registrarAccion(new Accion("Agregar cliente", "Bob"));

        System.out.println("\nDeshacer última acción:");
        System.out.println(historial.deshacerUltimaAccion());

        ColaSolicitudesSeguimiento cola = new ColaSolicitudesSeguimiento();
        cola.agregarSolicitud(new SolicitudSeguimiento("Alice", "Bob"));
        cola.agregarSolicitud(new SolicitudSeguimiento("Bob", "Charlie"));

        System.out.println("\nProcesar solicitud:");
        System.out.println(cola.procesarSolicitud());

        Scanner scanner = new Scanner(System.in);
        MenuRedSocial menuRedSocial = new MenuRedSocial(scanner);
        menuRedSocial.cargarDatosIniciales();
        menuRedSocial.ejecutar();

        scanner.close();
    }
}
