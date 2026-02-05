package ar.edu.uade.redsocial;

import ar.edu.uade.redsocial.model.Cliente;
import ar.edu.uade.redsocial.services.CargadorClientesJson;
import ar.edu.uade.redsocial.services.GestorClientes;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GestorClientesTest {

    @Test
    void testCargaClientesDesdeJson() {
        GestorClientes gestor = new GestorClientes();
        CargadorClientesJson.readFromFile(gestor);
        Cliente alice = gestor.buscarPorNombre("Alice");
        assertNotNull(alice);
        assertEquals(95, alice.getScoring());
    }

    @Test
    void buscarPorNombreNoExistente() {
        GestorClientes gestor = new GestorClientes();
        CargadorClientesJson.readFromFile(gestor);
        assertNull(gestor.buscarPorNombre("NoExiste"));
    }

    @Test
    void buscarPorScoring() {
        GestorClientes gestor = new GestorClientes();
        gestor.agregarCliente(new Cliente("A", 50));
        gestor.agregarCliente(new Cliente("B", 50));
        List<Cliente> lista = gestor.buscarPorScoring(50);
        assertEquals(2, lista.size());
    }

    @Test
    void agregarClienteNuevo() {
        GestorClientes gestor = new GestorClientes();
        boolean ok = gestor.agregarCliente(new Cliente("Juan", 70));
        assertTrue(ok);
        assertEquals(1, gestor.cantidadClientes());
    }

    @Test
    void noAgregarClienteDuplicado() {
        GestorClientes gestor = new GestorClientes();
        gestor.agregarCliente(new Cliente("Juan", 70));
        boolean ok = gestor.agregarCliente(new Cliente("Juan", 80));
        assertFalse(ok);
        assertEquals(1, gestor.cantidadClientes());
    }

    @Test
    void cantidadClientes() {
        GestorClientes gestor = new GestorClientes();
        CargadorClientesJson.readFromFile(gestor);
        assertEquals(5, gestor.cantidadClientes());
    }
}
