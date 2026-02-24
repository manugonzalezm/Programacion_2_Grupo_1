package ar.edu.uade.redsocial;

import ar.edu.uade.redsocial.services.CargadorClientesJson;
import ar.edu.uade.redsocial.services.GuardadorClientesJson;
import ar.edu.uade.redsocial.services.GestorClientes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GuardadorClientesJsonTest {

    @Test
    void guardarNoLanza() {
        GestorClientes gestor = new GestorClientes();
        CargadorClientesJson.readFromFile(gestor);
        assertDoesNotThrow(() -> GuardadorClientesJson.guardar(gestor));
    }

    @Test
    void jsonCargaClientesConRelaciones() {
        GestorClientes sistema = new GestorClientes();
        CargadorClientesJson.readFromFile(sistema);
        assertTrue(sistema.cantidadClientes() > 0, "El JSON debe tener al menos un cliente");
    }
}
