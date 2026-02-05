package ar.edu.uade.redsocial.utils;

import ar.edu.uade.redsocial.model.Accion;
import ar.edu.uade.redsocial.model.Cliente;
import ar.edu.uade.redsocial.model.SolicitudSeguimiento;
import ar.edu.uade.redsocial.services.CargadorClientesJson;
import ar.edu.uade.redsocial.services.ColaSolicitudesSeguimiento;
import ar.edu.uade.redsocial.services.GestorClientes;
import ar.edu.uade.redsocial.services.GuardadorClientesJson;
import ar.edu.uade.redsocial.services.HistorialAcciones;

import java.util.List;
import java.util.Scanner;

public class MenuRedSocial {
    
    private GestorClientes gestorClientes;
    private HistorialAcciones historial;
    private ColaSolicitudesSeguimiento colaSolicitudes;
    private Scanner scanner;
    
    public MenuRedSocial(Scanner scanner) {
        this.scanner = scanner;
        this.gestorClientes = new GestorClientes();
        this.historial = new HistorialAcciones();
        this.colaSolicitudes = new ColaSolicitudesSeguimiento();
    }
    
    public void cargarDatosIniciales() {
        CargadorClientesJson.readFromFile(gestorClientes);
        System.out.println("Datos iniciales cargados correctamente.");
    }
    
    public Menu crearMenu() {
        return new MenuBuilder("MENU - RED SOCIAL EMPRESARIAL")
            .agregarOpcion("1", "Buscar Cliente por nombre", scanner -> buscarClientePorNombre())
            .agregarOpcion("2", "Buscar Cliente por Scoring", scanner -> buscarClientePorScoring())
            .agregarOpcion("3", "Agregar Cliente", scanner -> agregarCliente())
            .agregarOpcion("4", "Seguir Cliente", scanner -> seguirCliente())
            .agregarOpcion("5", "Deshacer la última acción", () -> deshacerUltimaAccion())
            .agregarOpcion("6", "Procesar solicitudes de seguimiento", () -> procesarSolicitudesSeguimiento())
            .setMensajeSalida("Saliendo del sistema...")
            .setLimpiarConsola(false)
            .build();
    }
    
    public void ejecutar() {
        Menu menu = crearMenu();
        menu.ejecutar(scanner);
        GuardadorClientesJson.guardar(gestorClientes);
    }
    
    private void buscarClientePorNombre() {
        System.out.println("\n=== Buscar Cliente por nombre ===");
        String nombre = InputUtils.leerTexto(scanner, "Ingrese el nombre del cliente: ");
        
        Cliente cliente = gestorClientes.buscarPorNombre(nombre);
        
        if (cliente != null) {
            System.out.println("\nCliente encontrado: " + cliente);
            historial.registrarAccion(new Accion("Buscar por nombre", nombre));
        } else {
            System.out.println("\nNo se encontró ningún cliente con el nombre: " + nombre);
        }
    }
    
    private void buscarClientePorScoring() {
        System.out.println("\n=== Buscar Cliente por Scoring ===");
        int scoring = InputUtils.leerEnteroConReintentos(scanner, "Ingrese el scoring: ");
        
        List<Cliente> clientes = gestorClientes.buscarPorScoring(scoring);
        
        if (clientes.isEmpty()) {
            System.out.println("\nNo se encontraron clientes con scoring: " + scoring);
        } else {
            System.out.println("\nClientes encontrados con scoring " + scoring + ":");
            for (Cliente cliente : clientes) {
                System.out.println("  - " + cliente);
            }
            historial.registrarAccion(new Accion("Buscar por scoring", String.valueOf(scoring)));
        }
    }
    
    private void agregarCliente() {
        System.out.println("\n=== Agregar Cliente ===");
        String nombre = InputUtils.leerTexto(scanner, "Ingrese el nombre del cliente: ");
        
        if (gestorClientes.buscarPorNombre(nombre) != null) {
            System.out.println("\nError: Ya existe un cliente con el nombre: " + nombre);
            return;
        }
        
        int scoring = InputUtils.leerEnteroConReintentos(scanner, "Ingrese el scoring: ");
        
        Cliente nuevoCliente = new Cliente(nombre, scoring);
        boolean agregado = gestorClientes.agregarCliente(nuevoCliente);
        
        if (agregado) {
            System.out.println("\nCliente agregado exitosamente: " + nuevoCliente);
            historial.registrarAccion(new Accion("Agregar cliente", nombre));
        } else {
            System.out.println("\nError: No se pudo agregar el cliente.");
        }
    }
    
    private void seguirCliente() {
        System.out.println("\n=== Seguir Cliente ===");
        String seguidor = InputUtils.leerTexto(scanner, "Ingrese el nombre del seguidor: ");
        String seguido = InputUtils.leerTexto(scanner, "Ingrese el nombre del cliente a seguir: ");
        
        if (gestorClientes.buscarPorNombre(seguidor) == null) {
            System.out.println("\nError: No existe un cliente con el nombre: " + seguidor);
            return;
        }
        
        if (gestorClientes.buscarPorNombre(seguido) == null) {
            System.out.println("\nError: No existe un cliente con el nombre: " + seguido);
            return;
        }
        
        SolicitudSeguimiento solicitud = new SolicitudSeguimiento(seguidor, seguido);
        colaSolicitudes.agregarSolicitud(solicitud);
        
        System.out.println("\nSolicitud de seguimiento agregada a la cola.");
        historial.registrarAccion(new Accion("Seguir cliente", seguidor + " -> " + seguido));
    }
    
    private void deshacerUltimaAccion() {
        System.out.println("\n=== Deshacer Última Acción ===");
        
        Accion accionDeshecha = historial.deshacerUltimaAccion();
        
        if (accionDeshecha != null) {
            System.out.println("\nAcción deshecha: " + accionDeshecha);
        } else {
            System.out.println("\nNo hay acciones para deshacer.");
        }
    }
    
    private void procesarSolicitudesSeguimiento() {
        System.out.println("\n=== Procesar Solicitudes de Seguimiento ===");
        
        SolicitudSeguimiento solicitud = colaSolicitudes.procesarSolicitud();
        
        if (solicitud != null) {
            System.out.println("\nSolicitud procesada: " + solicitud);
            historial.registrarAccion(new Accion("Procesar solicitud", 
                solicitud.getSolicitante() + " -> " + solicitud.getSolicitado()));
        } else {
            System.out.println("\nNo hay solicitudes pendientes para procesar.");
        }
    }
}
