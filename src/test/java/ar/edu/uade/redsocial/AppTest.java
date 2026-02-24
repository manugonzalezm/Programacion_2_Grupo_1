package ar.edu.uade.redsocial;

import ar.edu.uade.redsocial.model.Accion;
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

        assertTrue(gestorClientes.cantidadClientes() > 0, "Deben cargarse clientes desde el JSON");

        HistorialAcciones historial = new HistorialAcciones();
        historial.registrarAccion(new Accion("Agregar cliente", "X"));
        historial.registrarAccion(new Accion("Agregar cliente", "Y"));
        Accion deshecha = historial.deshacerUltimaAccion();
        assertNotNull(deshecha);
        assertEquals("Y", deshecha.getDetalle());

        ColaSolicitudesSeguimiento cola = new ColaSolicitudesSeguimiento();
        cola.agregarSolicitud(new SolicitudSeguimiento("A", "B"));
        cola.agregarSolicitud(new SolicitudSeguimiento("B", "C"));
        SolicitudSeguimiento primera = cola.procesarSolicitud();
        assertNotNull(primera);
        assertEquals("A", primera.getSolicitante());
    }
}
