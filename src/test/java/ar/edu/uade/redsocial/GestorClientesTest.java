package ar.edu.uade.redsocial;

import ar.edu.uade.redsocial.implementation.StaticClientesTDA;
import ar.edu.uade.redsocial.model.Cliente;
import ar.edu.uade.redsocial.services.CargadorClientesJson;
import ar.edu.uade.redsocial.services.GestorClientes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GestorClientesTest {

    private GestorClientes gestor;

    @BeforeEach
    void setUp() {
        gestor = new GestorClientes();
    }

    @Test
    void testCargaClientesDesdeJson() {
        CargadorClientesJson.readFromFile(gestor);
        Cliente alice = gestor.buscarPorNombre("Alice");
        assertNotNull(alice);
        assertEquals(95, alice.getScoring());
    }

    @Test
    void buscarPorNombreNoExistente() {
        CargadorClientesJson.readFromFile(gestor);
        assertNull(gestor.buscarPorNombre("NoExiste"));
    }

    @Test
    void buscarPorScoring() {
        gestor.agregarCliente(new Cliente("A", 50));
        gestor.agregarCliente(new Cliente("B", 50));
        List<Cliente> lista = gestor.buscarPorScoring(50);
        assertEquals(2, lista.size());
    }

    @Test
    void buscarPorScoringNoExistente() {
        gestor.agregarCliente(new Cliente("A", 50));
        List<Cliente> lista = gestor.buscarPorScoring(999);
        assertTrue(lista.isEmpty());
    }

    @Test
    void agregarClienteNuevo() {
        boolean ok = gestor.agregarCliente(new Cliente("Juan", 70));
        assertTrue(ok);
        assertEquals(1, gestor.cantidadClientes());
    }

    @Test
    void noAgregarClienteDuplicado() {
        gestor.agregarCliente(new Cliente("Juan", 70));
        boolean ok = gestor.agregarCliente(new Cliente("Juan", 80));
        assertFalse(ok);
        assertEquals(1, gestor.cantidadClientes());
    }


    @Test
    void listarClientesVacio() {
        List<Cliente> lista = gestor.listarClientes();
        assertTrue(lista.isEmpty());
    }

    @Test
    void listarClientesConDatos() {
        gestor.agregarCliente(new Cliente("X", 10));
        gestor.agregarCliente(new Cliente("Y", 20));
        List<Cliente> lista = gestor.listarClientes();
        assertEquals(2, lista.size());
    }

    @Test
    void eliminarCliente() {
        gestor.agregarCliente(new Cliente("Test", 50));
        assertEquals(1, gestor.cantidadClientes());
        boolean ok = gestor.eliminarCliente("Test");
        assertTrue(ok);
        assertEquals(0, gestor.cantidadClientes());
        assertNull(gestor.buscarPorNombre("Test"));
    }

    @Test
    void eliminarClienteNoExistente() {
        assertFalse(gestor.eliminarCliente("NoExiste"));
    }

    @Test
    void agregarSeguido() {
        gestor.agregarCliente(new Cliente("A", 50));
        gestor.agregarCliente(new Cliente("B", 60));
        boolean ok = gestor.agregarSeguido("A", "B");
        assertTrue(ok);
        Cliente a = gestor.buscarPorNombre("A");
        assertTrue(a.getSiguiendo().contains("B"));
    }

    @Test
    void agregarSeguidoMismoUsuario() {
        gestor.agregarCliente(new Cliente("A", 50));
        assertFalse(gestor.agregarSeguido("A", "A"));
    }

    @Test
    void agregarSeguidoDuplicado() {
        gestor.agregarCliente(new Cliente("A", 50));
        gestor.agregarCliente(new Cliente("B", 60));
        gestor.agregarSeguido("A", "B");
        assertFalse(gestor.agregarSeguido("A", "B"));
    }

    @Test
    void agregarSeguidoClienteNoExiste() {
        gestor.agregarCliente(new Cliente("A", 50));
        assertFalse(gestor.agregarSeguido("A", "NoExiste"));
        assertFalse(gestor.agregarSeguido("NoExiste", "A"));
    }

    @Test
    void quitarSeguido() {
        gestor.agregarCliente(new Cliente("A", 50));
        gestor.agregarCliente(new Cliente("B", 60));
        gestor.agregarSeguido("A", "B");
        boolean ok = gestor.quitarSeguido("A", "B");
        assertTrue(ok);
        Cliente a = gestor.buscarPorNombre("A");
        assertFalse(a.getSiguiendo().contains("B"));
    }

    @Test
    void quitarSeguidoNoExistente() {
        gestor.agregarCliente(new Cliente("A", 50));
        assertFalse(gestor.quitarSeguido("A", "B"));
    }

    @Test
    void quitarSeguidoClienteNoExiste() {
        assertFalse(gestor.quitarSeguido("NoExiste", "A"));
    }

    @Test
    void modificarSeguidor() {
        gestor.agregarCliente(new Cliente("A", 50));
        Cliente actualizado = new Cliente("A", 99);
        boolean ok = gestor.modificarSeguidor(actualizado);
        assertTrue(ok);
        assertEquals(99, gestor.buscarPorNombre("A").getScoring());
    }

    @Test
    void modificarSeguidorNoExistente() {
        assertFalse(gestor.modificarSeguidor(new Cliente("NoExiste", 10)));
    }

    @Test
    void eliminarClienteConReferenciasEnOtros() {
        gestor.agregarCliente(new Cliente("A", 50));
        gestor.agregarCliente(new Cliente("B", 60));
        gestor.agregarSeguido("A", "B");
        gestor.eliminarCliente("B");
        Cliente a = gestor.buscarPorNombre("A");
        assertNotNull(a);
    }


    @Test
    public void noDebePermitirMasDeDosSeguidos() {

        StaticClientesTDA sistema = new StaticClientesTDA();

        Cliente a = new Cliente("A", 10);
        Cliente b = new Cliente("B", 20);
        Cliente c = new Cliente("C", 30);
        Cliente d = new Cliente("D", 40);

        sistema.agregarCliente(a);
        sistema.agregarCliente(b);
        sistema.agregarCliente(c);
        sistema.agregarCliente(d);

        sistema.enviarSolicitud("A", "B");
        sistema.aceptarSolicitud("B", "A");

        sistema.enviarSolicitud("A", "C");
        sistema.aceptarSolicitud("C", "A");

        // Tercera solicitud
        sistema.enviarSolicitud("A", "D");
        boolean resultado = sistema.aceptarSolicitud("D", "A");

        assertFalse(resultado);
    }


    @Test
    public void aceptarSolicitudDebeAgregarSeguido() {

        StaticClientesTDA sistema = new StaticClientesTDA();

        Cliente a = new Cliente("A", 10);
        Cliente b = new Cliente("B", 20);

        sistema.agregarCliente(a);
        sistema.agregarCliente(b);

        sistema.enviarSolicitud("A", "B");
        sistema.aceptarSolicitud("B", "A");

        Cliente actualizado = sistema.buscarPorNombre("A");

        assertTrue(actualizado.getSiguiendo().contains("B"));
    }

    @Test
    void agregarSeguidoMaximoDos() {
        gestor.agregarCliente(new Cliente("A", 50));
        gestor.agregarCliente(new Cliente("B", 60));
        gestor.agregarCliente(new Cliente("C", 70));
        gestor.agregarCliente(new Cliente("D", 80));

        assertTrue(gestor.agregarSeguido("A", "B"));
        assertTrue(gestor.agregarSeguido("A", "C"));
        assertFalse(gestor.agregarSeguido("A", "D"));
    }

    @Test
    void consultarConexionesNivel4() {
        gestor.agregarCliente(new Cliente("A", 10));
        gestor.agregarCliente(new Cliente("B", 20));
        gestor.agregarCliente(new Cliente("C", 30));

        gestor.agregarSeguido("A", "B");
        gestor.agregarSeguido("A", "C");

        List<Integer> nivel4 = gestor.consultarConexionesNivel4("A");
        assertNotNull(nivel4);
    }
}
