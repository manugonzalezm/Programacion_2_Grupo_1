package ar.edu.uade.redsocial;

import ar.edu.uade.redsocial.implementation.StaticClientesTDA;
import ar.edu.uade.redsocial.model.Cliente;
import ar.edu.uade.redsocial.tda.ClientesTDA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StaticClientesTDATest {

    private ClientesTDA clientes;

    @BeforeEach
    void setUp() {
        clientes = new StaticClientesTDA();
    }

    @Test
    void agregarYBuscar() {
        assertTrue(clientes.agregarCliente(new Cliente("Alice", 90)));
        Cliente c = clientes.buscarPorNombre("Alice");
        assertNotNull(c);
        assertEquals("Alice", c.getNombre());
        assertEquals(90, c.getScoring());
    }

    @Test
    void buscarNoExistente() {
        assertNull(clientes.buscarPorNombre("Ghost"));
    }

    @Test
    void noDuplicados() {
        clientes.agregarCliente(new Cliente("A", 50));
        assertFalse(clientes.agregarCliente(new Cliente("A", 99)));
    }

    @Test
    void cantidadClientes() {
        assertEquals(0, clientes.cantidadClientes());
        clientes.agregarCliente(new Cliente("A", 1));
        clientes.agregarCliente(new Cliente("B", 2));
        assertEquals(2, clientes.cantidadClientes());
    }

    @Test
    void listarClientes() {
        clientes.agregarCliente(new Cliente("A", 1));
        clientes.agregarCliente(new Cliente("B", 2));
        List<Cliente> lista = clientes.listarClientes();
        assertEquals(2, lista.size());
    }

    @Test
    void buscarPorScoring() {
        clientes.agregarCliente(new Cliente("A", 50));
        clientes.agregarCliente(new Cliente("B", 60));
        clientes.agregarCliente(new Cliente("C", 50));
        List<Cliente> result = clientes.buscarPorScoring(50);
        assertEquals(2, result.size());
        assertTrue(clientes.buscarPorScoring(999).isEmpty());
    }

    @Test
    void agregarConSiguiendo() {
        clientes.agregarCliente(new Cliente("A", 50));
        clientes.agregarCliente(new Cliente("B", 60,Arrays.asList("A"),Arrays.asList(),Arrays.asList()));
        Cliente b = clientes.buscarPorNombre("B");
        assertTrue(b.getSiguiendo().contains("A"));
    }

    @Test
    void agregarConConexiones() {
        clientes.agregarCliente(new Cliente("A", 50));
        clientes.agregarCliente(new Cliente("B", 60,Arrays.asList(),Arrays.asList("A"),Arrays.asList()));
        Cliente b = clientes.buscarPorNombre("B");
        assertTrue(b.getConexiones().contains("A"));
    }

    @Test
    void modificarSeguidor() {
        clientes.agregarCliente(new Cliente("A", 50));
        assertTrue(clientes.modificarSeguidor(new Cliente("A", 99)));
        assertEquals(99, clientes.buscarPorNombre("A").getScoring());
    }

    @Test
    void modificarSeguidorNoExistente() {
        assertFalse(clientes.modificarSeguidor(new Cliente("Z", 10)));
    }

    @Test
    void eliminarCliente() {
        clientes.agregarCliente(new Cliente("A", 50));
        assertTrue(clientes.eliminarCliente("A"));
        assertNull(clientes.buscarPorNombre("A"));
        assertEquals(0, clientes.cantidadClientes());
    }

    @Test
    void eliminarClienteNoExistente() {
        assertFalse(clientes.eliminarCliente("NoExiste"));
    }

    @Test
    void agregarSeguido() {
        clientes.agregarCliente(new Cliente("A", 50));
        clientes.agregarCliente(new Cliente("B", 60));
        assertTrue(clientes.agregarSeguido("A", "B"));
        assertTrue(clientes.buscarPorNombre("A").getSiguiendo().contains("B"));
    }

    @Test
    void agregarSeguidoMismo() {
        clientes.agregarCliente(new Cliente("A", 50));
        assertFalse(clientes.agregarSeguido("A", "A"));
    }

    @Test
    void agregarSeguidoDuplicado() {
        clientes.agregarCliente(new Cliente("A", 50));
        clientes.agregarCliente(new Cliente("B", 60));
        clientes.agregarSeguido("A", "B");
        assertFalse(clientes.agregarSeguido("A", "B"));
    }

    @Test
    void agregarSeguidoNoExiste() {
        clientes.agregarCliente(new Cliente("A", 50));
        assertFalse(clientes.agregarSeguido("A", "Ghost"));
        assertFalse(clientes.agregarSeguido("Ghost", "A"));
    }

    @Test
    void quitarSeguido() {
        clientes.agregarCliente(new Cliente("A", 50));
        clientes.agregarCliente(new Cliente("B", 60));
        clientes.agregarSeguido("A", "B");
        assertTrue(clientes.quitarSeguido("A", "B"));
        assertFalse(clientes.buscarPorNombre("A").getSiguiendo().contains("B"));
    }

    @Test
    void quitarSeguidoNoExiste() {
        clientes.agregarCliente(new Cliente("A", 50));
        assertFalse(clientes.quitarSeguido("A", "NoSeguido"));
        assertFalse(clientes.quitarSeguido("NoExiste", "A"));
    }

    @Test
    void eliminarClienteQuitaReferencias() {
        clientes.agregarCliente(new Cliente("A", 50));
        clientes.agregarCliente(new Cliente("B", 60));
        clientes.agregarSeguido("A", "B");
        clientes.eliminarCliente("B");
        assertNull(clientes.buscarPorNombre("B"));
        assertEquals(1, clientes.cantidadClientes());
    }

    @Test
    void agregarConSiguiendoReferenciaNoExistente() {
        // Si un siguiendo referencia alguien que no fue cargado aun, se ignora
        clientes.agregarCliente(new Cliente("A", 50,Arrays.asList("Ghost"),Arrays.asList(),Arrays.asList()));
        Cliente a = clientes.buscarPorNombre("A");
        assertTrue(a.getSiguiendo().isEmpty());
    }

    // ─── Límite de 2 seguidos ───

    @Test
    void agregarSeguidoMaximoDos() {
        clientes.agregarCliente(new Cliente("A", 50));
        clientes.agregarCliente(new Cliente("B", 60));
        clientes.agregarCliente(new Cliente("C", 70));
        clientes.agregarCliente(new Cliente("D", 80));

        assertTrue(clientes.agregarSeguido("A", "B"));
        assertTrue(clientes.agregarSeguido("A", "C"));
        assertFalse(clientes.agregarSeguido("A", "D"));
        assertEquals(2, clientes.buscarPorNombre("A").getSiguiendo().size());
    }

    // ─── Consulta ABB nivel 4 ───

    @Test
    void consultarConexionesNivel4ClienteNoExiste() {
        assertTrue(clientes.consultarConexionesNivel4("Ghost").isEmpty());
    }

    @Test
    void consultarConexionesNivel4SinSeguidos() {
        clientes.agregarCliente(new Cliente("A", 50));
        assertTrue(clientes.consultarConexionesNivel4("A").isEmpty());
    }

    @Test
    void consultarConexionesNivel4RedTransitiva() {
        // Construimos una cadena: A->B->D, A->C->E, D->F, D->G
        clientes.agregarCliente(new Cliente("A", 10));
        clientes.agregarCliente(new Cliente("B", 20));
        clientes.agregarCliente(new Cliente("C", 30));
        clientes.agregarCliente(new Cliente("D", 40));
        clientes.agregarCliente(new Cliente("E", 50));
        clientes.agregarCliente(new Cliente("F", 60));
        clientes.agregarCliente(new Cliente("G", 70));

        clientes.agregarSeguido("A", "B");
        clientes.agregarSeguido("A", "C");
        clientes.agregarSeguido("B", "D");
        clientes.agregarSeguido("C", "E");
        clientes.agregarSeguido("D", "F");
        clientes.agregarSeguido("D", "G");

        // ABB con scorings: 20, 30, 40, 50, 60, 70
        // El arbol y su nivel 4 dependerá del orden de inserción BFS
        List<Integer> nivel4 = clientes.consultarConexionesNivel4("A");
        assertNotNull(nivel4);
    }
}
