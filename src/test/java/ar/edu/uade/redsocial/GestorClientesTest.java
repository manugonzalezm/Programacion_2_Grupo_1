package ar.edu.uade.redsocial;

import ar.edu.uade.redsocial.model.Cliente;
import ar.edu.uade.redsocial.services.CargadorClientesJson;
import ar.edu.uade.redsocial.services.GestorClientes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
//Este test verifica : “Si cargo el sistema, ¿Alice existe y tiene scoring 95?”

class GestorClientesTest {

    @Test
    void testCargaClientesDesdeJson() {

        GestorClientes gestor = new GestorClientes();

        CargadorClientesJson.cargar(gestor);

        Cliente alice = gestor.buscarPorNombre("Alice");

        assertNotNull(alice);
        assertEquals(95, alice.getScoring());
    }
}


