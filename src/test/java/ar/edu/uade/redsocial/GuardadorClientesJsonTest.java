package ar.edu.uade.redsocial;

import ar.edu.uade.redsocial.services.CargadorClientesJson;
import ar.edu.uade.redsocial.services.GuardadorClientesJson;
import ar.edu.uade.redsocial.services.GestorClientes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class GuardadorClientesJsonTest {

    @Test
    void guardarNoLanza() {
        GestorClientes gestor = new GestorClientes();
        CargadorClientesJson.readFromFile(gestor);
        assertDoesNotThrow(() -> GuardadorClientesJson.guardar(gestor));
    }
}
