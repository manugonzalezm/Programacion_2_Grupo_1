package ar.edu.uade.redsocial;

import ar.edu.uade.redsocial.model.Accion;
import ar.edu.uade.redsocial.model.Cliente;
import ar.edu.uade.redsocial.model.SolicitudSeguimiento;
import ar.edu.uade.redsocial.services.CargadorClientesJson;
import ar.edu.uade.redsocial.services.ColaSolicitudesSeguimiento;
import ar.edu.uade.redsocial.services.GestorClientes;
import ar.edu.uade.redsocial.services.HistorialAcciones;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    @Test
    void flujoCompletoComoEnMain() {
        GestorClientes gestorClientes = new GestorClientes();
        CargadorClientesJson.readFromFile(gestorClientes);

        Cliente alice = gestorClientes.buscarPorNombre("Alice");
        assertNotNull(alice);
        assertEquals(95, alice.getScoring());

        assertFalse(gestorClientes.buscarPorScoring(88).isEmpty());

        HistorialAcciones historial = new HistorialAcciones();
        historial.registrarAccion(new Accion("Agregar cliente", "Alice"));
        historial.registrarAccion(new Accion("Agregar cliente", "Bob"));
        Accion deshecha = historial.deshacerUltimaAccion();
        assertNotNull(deshecha);
        assertEquals("Bob", deshecha.getDetalle());

        ColaSolicitudesSeguimiento cola = new ColaSolicitudesSeguimiento();
        cola.agregarSolicitud(new SolicitudSeguimiento("Alice", "Bob"));
        cola.agregarSolicitud(new SolicitudSeguimiento("Bob", "Charlie"));
        SolicitudSeguimiento primera = cola.procesarSolicitud();
        assertNotNull(primera);
        assertEquals("Alice", primera.getSolicitante());
        assertEquals("Bob", primera.getSolicitado());
    }
}
