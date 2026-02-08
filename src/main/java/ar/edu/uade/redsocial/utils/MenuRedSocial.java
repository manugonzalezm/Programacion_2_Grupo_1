package ar.edu.uade.redsocial.utils;

import ar.edu.uade.redsocial.model.Accion;
import ar.edu.uade.redsocial.model.Cliente;
import ar.edu.uade.redsocial.model.SolicitudSeguimiento;
import ar.edu.uade.redsocial.services.CargadorClientesJson;
import ar.edu.uade.redsocial.services.ColaSolicitudesSeguimiento;
import ar.edu.uade.redsocial.services.ExportadorAccionesCsv;
import ar.edu.uade.redsocial.services.GestorClientes;
import ar.edu.uade.redsocial.services.GuardadorClientesJson;
import ar.edu.uade.redsocial.services.HistorialAcciones;

import java.util.List;
import java.util.Scanner;

public class MenuRedSocial {

    private final GestorClientes gestorClientes;
    private final HistorialAcciones historial;
    private final ColaSolicitudesSeguimiento colaSolicitudes;
    private final Scanner scanner;

    /**
     * Constructor que recibe el scanner y los servicios ya creados (respaldados por TDAs:
     * ClientesTDA, HistorialAccionesTDA, SolicitudesSeguimientoTDA -> Diccionario, Pila, Cola).
     * Todo el flujo usa estas mismas instancias.
     */
    public MenuRedSocial(Scanner scanner, GestorClientes gestorClientes, HistorialAcciones historial, ColaSolicitudesSeguimiento colaSolicitudes) { // complejidad O(1)
        this.scanner = scanner;
        this.gestorClientes = gestorClientes;
        this.historial = historial;
        this.colaSolicitudes = colaSolicitudes;
    }

    /** Carga el JSON en el gestor recibido por constructor (misma instancia TDA). */
    public void cargarDatosIniciales() { // complejidad O(n), ver CargadorClientesJson
        CargadorClientesJson.readFromFile(gestorClientes);
        System.out.println("Datos iniciales cargados correctamente.");
    }

    public Menu crearMenu() { // complejidad O(1)
        return new MenuBuilder("MENU - RED SOCIAL EMPRESARIAL")
            .agregarOpcion("1", "Buscar Cliente por nombre", scanner -> buscarClientePorNombre())
            .agregarOpcion("2", "Buscar Cliente por Scoring", scanner -> buscarClientePorScoring())
            .agregarOpcion("3", "Agregar Cliente", scanner -> agregarCliente())
            .agregarOpcion("4", "Seguir Cliente", scanner -> seguirCliente())
            .agregarOpcion("5", "Deshacer la ultima accion", () -> deshacerUltimaAccion())
            .agregarOpcion("6", "Procesar solicitudes de seguimiento", () -> procesarSolicitudesSeguimiento())
            .agregarOpcion("7", "Listar solicitudes pendientes", () -> listarSolicitudesPendientes())
            .agregarOpcion("8", "Listar ultimas 10 acciones", () -> listarUltimasAcciones())
            .agregarOpcion("9", "Listar todos los clientes", () -> listarTodosLosClientes())
            .setMensajeSalida("Saliendo del sistema...")
            .setLimpiarConsola(false)
            .build();
    }

    public void ejecutar() { // complejidad segun uso del menu; al salir O(n*m) por guardar
        Menu menu = crearMenu();
        menu.ejecutar(scanner);
        GuardadorClientesJson.guardar(gestorClientes);
        exportarAccionesCsv();
    }

    private void buscarClientePorNombre() { // complejidad O(n), ver GestorClientes.buscarPorNombre
        System.out.println("\n=== Buscar Cliente por nombre ===");
        String nombre = InputUtils.leerTexto(scanner, "Ingrese el nombre del cliente: ");

        Cliente cliente = gestorClientes.buscarPorNombre(nombre);

        if (cliente != null) {
            System.out.println("\nCliente encontrado: " + cliente.toString());
            historial.registrarAccion(new Accion("Buscar por nombre", nombre));
        } else {
            System.out.println("\nNo se encontro ningun cliente con el nombre: " + nombre);
        }
    }

    private void buscarClientePorScoring() { // complejidad O(n*m), ver GestorClientes.buscarPorScoring
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

    private void agregarCliente() { // complejidad O(n), ver GestorClientes
        System.out.println("\n=== Agregar Cliente ===");
        String nombre = InputUtils.leerTextoNoVacio(scanner, "Ingrese el nombre del cliente: ");

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

    private void seguirCliente() { // complejidad O(n), buscarPorNombre + agregarSolicitud O(1)
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

    private void deshacerUltimaAccion() { // complejidad O(1) o O(n) segun tipo
        System.out.println("\n=== Deshacer Ultima Accion ===");

        Accion accionDeshecha = historial.deshacerUltimaAccion();

        if (accionDeshecha != null) {
            boolean impactoEnClientes = false;
            String tipo = accionDeshecha.getTipo();
            String detalle = accionDeshecha.getDetalle();

            if ("Agregar cliente".equals(tipo)) {
                impactoEnClientes = gestorClientes.eliminarCliente(detalle);
                if (impactoEnClientes) {
                    System.out.println("\nAccion deshecha: cliente \"" + detalle + "\" eliminado de los datos.");
                }
            } else if ("Procesar solicitud".equals(tipo) && detalle != null && detalle.contains(" -> ")) {
                String[] partes = detalle.split(" -> ", 2);
                if (partes.length == 2) {
                    impactoEnClientes = gestorClientes.quitarSeguido(partes[0].trim(), partes[1].trim());
                    if (impactoEnClientes) {
                        System.out.println("\nAccion deshecha: se revirtio el seguimiento " + detalle + ".");
                    }
                }
            } else if ("Seguir cliente".equals(tipo) && detalle != null && detalle.contains(" -> ")) {
                String[] partes = detalle.split(" -> ", 2);
                if (partes.length == 2) {
                    boolean quitada = colaSolicitudes.quitarSolicitud(new SolicitudSeguimiento(partes[0].trim(), partes[1].trim()));
                    if (quitada) {
                        System.out.println("\nAccion deshecha: solicitud " + detalle + " quitada de la cola.");
                    }
                }
            }

            if (!impactoEnClientes && !"Seguir cliente".equals(tipo)) {
                if (!"Agregar cliente".equals(tipo) && !"Procesar solicitud".equals(tipo)) {
                    System.out.println("\nAccion deshecha: " + accionDeshecha);
                } else {
                    System.out.println("\nAccion deshecha (sin cambios en datos): " + accionDeshecha);
                }
            }
        } else {
            System.out.println("\nNo hay acciones para deshacer.");
        }
    }

    private void procesarSolicitudesSeguimiento() { // complejidad O(1) procesar + O(n) agregarSeguido
        System.out.println("\n=== Procesar Solicitudes de Seguimiento ===");

        SolicitudSeguimiento solicitud = colaSolicitudes.procesarSolicitud();

        if (solicitud != null) {
            String solicitante = solicitud.getSolicitante();
            String solicitado = solicitud.getSolicitado();
            boolean aplicada = gestorClientes.agregarSeguido(solicitante, solicitado);
            if (aplicada) {
                System.out.println("\nSolicitud procesada y aplicada: " + solicitante + " ahora sigue a " + solicitado + ".");
            } else {
                System.out.println("\nSolicitud procesada pero no pudo aplicarse (cliente/s no existen, ya lo seguia o es el mismo usuario): " + solicitud);
            }
            historial.registrarAccion(new Accion("Procesar solicitud", solicitante + " -> " + solicitado));
        } else {
            System.out.println("\nNo hay solicitudes pendientes para procesar.");
        }
    }

    private void listarSolicitudesPendientes() { // complejidad O(n)
        System.out.println("\n=== Solicitudes Pendientes ===");
        List<SolicitudSeguimiento> pendientes = colaSolicitudes.listarPendientes();
        if (pendientes.isEmpty()) {
            System.out.println("No hay solicitudes pendientes.");
        } else {
            System.out.println("Total: " + pendientes.size() + " solicitud(es) pendiente(s):");
            int i = 1;
            for (SolicitudSeguimiento s : pendientes) {
                System.out.println("  " + i + ". " + s.getOrigen() + " -> " + s.getDestino());
                i++;
            }
        }
    }

    private void listarUltimasAcciones() { // complejidad O(n)
        System.out.println("\n=== Ultimas 10 Acciones ===");
        List<Accion> ultimas = historial.listarUltimas(10);
        if (ultimas.isEmpty()) {
            System.out.println("No hay acciones registradas.");
        } else {
            System.out.println("Mostrando " + ultimas.size() + " accion(es) (mas reciente primero):");
            int i = 1;
            for (Accion a : ultimas) {
                System.out.println("  " + i + ". [" + a.getTipo() + "] " + a.getDetalle() + " (" + a.getFechaHora() + ")");
                i++;
            }
        }
    }

    private void listarTodosLosClientes() { // complejidad O(n*m)
        System.out.println("\n=== Lista de Clientes ===");
        List<Cliente> clientes = gestorClientes.listarClientes();
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
        } else {
            System.out.println("Total: " + clientes.size() + " cliente(s):");
            for (Cliente c : clientes) {
                System.out.println("  - " + c);
            }
        }
        historial.registrarAccion(new Accion("Listar clientes", "total=" + clientes.size()));
    }

    private void exportarAccionesCsv() { // complejidad O(n)
        List<Accion> todas = historial.listarUltimas(1000);
        if (!todas.isEmpty()) {
            try {
                ExportadorAccionesCsv.exportar(todas, "acciones.csv");
                System.out.println("Historial de acciones exportado a acciones.csv");
            } catch (Exception e) {
                System.out.println("No se pudo exportar acciones: " + e.getMessage());
            }
        }
    }
}
