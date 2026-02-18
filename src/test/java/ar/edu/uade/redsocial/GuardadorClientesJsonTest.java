package ar.edu.uade.redsocial;

import ar.edu.uade.redsocial.implementation.StaticClientesTDA;
import ar.edu.uade.redsocial.model.Cliente;
import ar.edu.uade.redsocial.services.CargadorClientesJson;
import ar.edu.uade.redsocial.services.GuardadorClientesJson;
import ar.edu.uade.redsocial.services.GestorClientes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class GuardadorClientesJsonTest {

    @Test
    void guardarNoLanza() {
        GestorClientes gestor = new GestorClientes();
        CargadorClientesJson.readFromFile(gestor);
        assertDoesNotThrow(() -> GuardadorClientesJson.guardar(gestor));
    }


    @Test
    public void jsonDebeCargarClientesConSeguidos() {

        GestorClientes sistema = new GestorClientes();


        CargadorClientesJson.readFromFile(sistema);

        Cliente chen = sistema.buscarPorNombre("Chen");

        assertNotNull(chen);
        assertTrue(chen.getSiguiendo().contains("Flor"));
        assertTrue(chen.getSiguiendo().contains("Manuel"));
    }

}
