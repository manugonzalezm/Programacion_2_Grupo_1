package ar.edu.uade.redsocial;

import ar.edu.uade.redsocial.model.Cliente;
import ar.edu.uade.redsocial.services.CargadorClientesJson;
import java.util.List;
import ar.edu.uade.redsocial.services.GestorClientes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        assertTrue(gestor.cantidadClientes() > 0, "Se deben cargar clientes desde el JSON");
        assertFalse(gestor.listarClientes().isEmpty());
    }

    @Test
    void agregarYBuscarCliente() {
        gestor.agregarCliente(new Cliente("Juan", 70));
        assertNotNull(gestor.buscarPorNombre("Juan"));
        assertFalse(gestor.agregarCliente(new Cliente("Juan", 80))); // duplicado
    }

    @Test
    void eliminarCliente() {
        gestor.agregarCliente(new Cliente("Test", 50));
        assertTrue(gestor.eliminarCliente("Test"));
        assertNull(gestor.buscarPorNombre("Test"));
    }

    @Test
    void seguimientoDirecto() {
        gestor.agregarCliente(new Cliente("A", 50));
        gestor.agregarCliente(new Cliente("B", 60));
        gestor.agregarCliente(new Cliente("C", 70));
        gestor.agregarCliente(new Cliente("D", 80));
        assertTrue(gestor.agregarSeguido("A", "B"));
        assertTrue(gestor.agregarSeguido("A", "C"));
        assertFalse(gestor.agregarSeguido("A", "D")); // límite de 2
        assertTrue(gestor.obtenerSeguidos("A").contains("B"));
    }

    @Test
    void quitarSeguido() {
        gestor.agregarCliente(new Cliente("A", 50));
        gestor.agregarCliente(new Cliente("B", 60));
        gestor.agregarSeguido("A", "B");
        assertTrue(gestor.quitarSeguido("A", "B"));
        assertFalse(gestor.obtenerSeguidos("A").contains("B"));
    }

    @Test
    void amistadBidireccional() {
        gestor.agregarCliente(new Cliente("A", 50));
        gestor.agregarCliente(new Cliente("B", 60));
        gestor.agregarAmistad("A", "B");
        assertTrue(gestor.obtenerVecinos("A").contains("B"));
        assertTrue(gestor.obtenerVecinos("B").contains("A"));
    }

    @Test
    void calcularDistanciaSeguimiento() {
        gestor.agregarCliente(new Cliente("A", 10));
        gestor.agregarCliente(new Cliente("B", 20));
        gestor.agregarCliente(new Cliente("C", 30));
        gestor.agregarSeguido("A", "B");
        gestor.agregarSeguido("B", "C");
        assertEquals(2, gestor.calcularDistanciaSeguimiento("A", "C"));
        assertEquals(-1, gestor.calcularDistanciaSeguimiento("C", "A")); // grafo dirigido
    }

    @Test
    void aceptarSolicitudAmistadDebeCrearAmistad() {
        gestor.agregarCliente(new Cliente("A", 10));
        gestor.agregarCliente(new Cliente("B", 20));
        gestor.agregarAmistad("A", "B");
        assertTrue(gestor.obtenerVecinos("A").contains("B"));
        assertFalse(gestor.obtenerSeguidos("A").contains("B")); // amistad ≠ seguimiento
    }
}
