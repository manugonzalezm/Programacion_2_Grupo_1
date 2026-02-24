package ar.edu.uade.redsocial;

import ar.edu.uade.redsocial.implementation.StaticClientesTDA;
import ar.edu.uade.redsocial.model.Cliente;
import ar.edu.uade.redsocial.tda.ClientesTDA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        assertNotNull(clientes.buscarPorNombre("Alice"));
        assertFalse(clientes.agregarCliente(new Cliente("Alice", 99))); // duplicado
    }

    @Test
    void eliminarCliente() {
        clientes.agregarCliente(new Cliente("A", 50));
        assertTrue(clientes.eliminarCliente("A"));
        assertNull(clientes.buscarPorNombre("A"));
    }

    @Test
    void buscarPorScoring() {
        clientes.agregarCliente(new Cliente("A", 50));
        clientes.agregarCliente(new Cliente("B", 60));
        clientes.agregarCliente(new Cliente("C", 50));
        assertEquals(2, clientes.buscarPorScoring(50).size());
        assertTrue(clientes.buscarPorScoring(999).isEmpty());
    }

    @Test
    void seguimientoConLimite() {
        clientes.agregarCliente(new Cliente("A", 50));
        clientes.agregarCliente(new Cliente("B", 60));
        clientes.agregarCliente(new Cliente("C", 70));
        clientes.agregarCliente(new Cliente("D", 80));
        assertTrue(clientes.agregarSeguido("A", "B"));
        assertTrue(clientes.agregarSeguido("A", "C"));
        assertFalse(clientes.agregarSeguido("A", "D")); // límite: 2 seguidos
    }

    @Test
    void quitarSeguido() {
        clientes.agregarCliente(new Cliente("A", 50));
        clientes.agregarCliente(new Cliente("B", 60));
        clientes.agregarSeguido("A", "B");
        assertTrue(clientes.quitarSeguido("A", "B"));
        assertFalse(clientes.obtenerSeguidos("A").contains("B"));
    }

    @Test
    void amistadBidireccional() {
        clientes.agregarCliente(new Cliente("A", 50));
        clientes.agregarCliente(new Cliente("B", 60));
        clientes.agregarAmistad("A", "B");
        assertTrue(clientes.obtenerVecinos("A").contains("B"));
        assertTrue(clientes.obtenerVecinos("B").contains("A"));
        clientes.eliminarAmistad("A", "B");
        assertFalse(clientes.obtenerVecinos("A").contains("B"));
    }

    @Test
    void calcularDistanciaSeguimiento() {
        clientes.agregarCliente(new Cliente("A", 10));
        clientes.agregarCliente(new Cliente("B", 20));
        clientes.agregarCliente(new Cliente("C", 30));
        clientes.agregarSeguido("A", "B");
        clientes.agregarSeguido("B", "C");
        assertEquals(2, clientes.calcularDistanciaSeguimiento("A", "C"));
        assertEquals(-1, clientes.calcularDistanciaSeguimiento("C", "A")); // dirigido
    }

    @Test
    void consultarConexionesNivel4() {
        clientes.agregarCliente(new Cliente("A", 10));
        clientes.agregarCliente(new Cliente("B", 20));
        clientes.agregarCliente(new Cliente("C", 30));
        clientes.agregarSeguido("A", "B");
        clientes.agregarSeguido("A", "C");
        assertNotNull(clientes.consultarConexionesNivel4("A"));
        assertTrue(clientes.consultarConexionesNivel4("Ghost").isEmpty());
    }
}
