package ar.edu.uade.redsocial;

import ar.edu.uade.redsocial.model.Accion;
import ar.edu.uade.redsocial.model.SolicitudSeguimiento;
import ar.edu.uade.redsocial.services.CargadorClientesJson;
import ar.edu.uade.redsocial.services.ColaSolicitudesSeguimiento;
import ar.edu.uade.redsocial.services.GestorClientes;
import ar.edu.uade.redsocial.services.HistorialAcciones;

public class Main {

    public static void main(String[] args) {

        // ====== CARGA INICIAL ======
        GestorClientes gestorClientes = new GestorClientes();
        CargadorClientesJson.cargar(gestorClientes);

        // ====== GESTIÓN DE CLIENTES ======
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

        cola.agregarSolicitud(new SolicitudSeguimiento("Alice", "Bob"));
        cola.agregarSolicitud(new SolicitudSeguimiento("Bob", "Charlie"));

        System.out.println("\nProcesar solicitud:");
        System.out.println(cola.procesarSolicitud());
    }
}
