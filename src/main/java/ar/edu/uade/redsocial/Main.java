package ar.edu.uade.redsocial.app;

import ar.edu.uade.redsocial.model.Cliente;
import ar.edu.uade.redsocial.model.Accion;
import ar.edu.uade.redsocial.model.SolicitudSeguimiento;
import ar.edu.uade.redsocial.services.GestorClientes;
import ar.edu.uade.redsocial.services.HistorialAcciones;
import ar.edu.uade.redsocial.services.ColaSolicitudesSeguimiento;

public class Main {

    public static void main(String[] args) {

        // ====== GESTIÓN DE CLIENTES ======
        GestorClientes gestorClientes = new GestorClientes();

        Cliente c1 = new Cliente("Alice", 95);
        Cliente c2 = new Cliente("Bob", 88);
        Cliente c3 = new Cliente("Charlie", 80);

        gestorClientes.agregarCliente(c1);
        gestorClientes.agregarCliente(c2);
        gestorClientes.agregarCliente(c3);

        System.out.println("Buscar por nombre Alice:");
        System.out.println(gestorClientes.buscarPorNombre("Alice"));

        System.out.println("\nBuscar por scoring 88:");
        System.out.println(gestorClientes.buscarPorScoring(88));

        // ====== HISTORIAL DE ACCIONES ======
        HistorialAcciones historial = new HistorialAcciones();

        historial.registrarAccion(new Accion("Agregar cliente", "Alice"));
        historial.registrarAccion(new Accion("Agregar cliente", "Bob"));

        System.out.println("\nDeshacer última acción:");
        System.out.println(historial.deshacerUltimaAccion());

        // ====== SOLICITUDES DE SEGUIMIENTO ======
        ColaSolicitudesSeguimiento cola = new ColaSolicitudesSeguimiento();

        // ✅ Constructor correcto: (solicitante, solicitado)
        cola.agregarSolicitud(new SolicitudSeguimiento("Alice", "Bob"));
        cola.agregarSolicitud(new SolicitudSeguimiento("Bob", "Charlie"));

        System.out.println("\nProcesar solicitud:");
        System.out.println(cola.procesarSolicitud());
    }
}
