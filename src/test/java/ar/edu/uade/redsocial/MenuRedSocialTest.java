package ar.edu.uade.redsocial;

import ar.edu.uade.redsocial.model.Accion;
import ar.edu.uade.redsocial.model.Cliente;
import ar.edu.uade.redsocial.model.SolicitudSeguimiento;
import ar.edu.uade.redsocial.services.ColaSolicitudesSeguimiento;
import ar.edu.uade.redsocial.services.GestorClientes;
import ar.edu.uade.redsocial.services.HistorialAcciones;
import ar.edu.uade.redsocial.utils.Menu;
import ar.edu.uade.redsocial.utils.MenuRedSocial;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class MenuRedSocialTest {

    private GestorClientes gestor;
    private HistorialAcciones historial;
    private ColaSolicitudesSeguimiento cola;

    @BeforeEach
    void setUp() {
        gestor = new GestorClientes();
        historial = new HistorialAcciones();
        cola = new ColaSolicitudesSeguimiento();
        // Cargamos datos de prueba directamente
        gestor.agregarCliente(new Cliente("Alice", 95));
        gestor.agregarCliente(new Cliente("Bob", 88));
        gestor.agregarCliente(new Cliente("Charlie", 72));
    }

    private MenuRedSocial crearMenuRedSocial(String input) {
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        return new MenuRedSocial(scanner, gestor, historial, cola);
    }

    /** Ejecuta el menu con el input dado usando crearMenu().ejecutar(scanner)
     *  para evitar llamar a GuardadorClientesJson.guardar() que requiere filesystem. */
    private void ejecutarMenu(String input) {
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        MenuRedSocial menuRedSocial = new MenuRedSocial(scanner, gestor, historial, cola);
        Menu menu = menuRedSocial.crearMenu();
        menu.ejecutar(scanner);
        scanner.close();
    }

    // --- Opcion 1: Buscar por nombre ---

    @Test
    void buscarClientePorNombreExistente() {
        // 1 = buscar por nombre, "Alice" = nombre, \n = enter continuar, 0 = salir
        ejecutarMenu("1\nAlice\n\n0\n");
        assertTrue(historial.hayAcciones());
        assertEquals("Buscar por nombre", historial.deshacerUltimaAccion().getTipo());
    }

    @Test
    void buscarClientePorNombreNoExistente() {
        ejecutarMenu("1\nNoExiste\n\n0\n");
        assertFalse(historial.hayAcciones());
    }

    // --- Opcion 2: Buscar por scoring ---

    @Test
    void buscarClientePorScoringExistente() {
        // 2 = buscar por scoring, 95 = scoring, \n = enter, 0 = salir
        ejecutarMenu("2\n95\n\n0\n");
        assertTrue(historial.hayAcciones());
        Accion a = historial.deshacerUltimaAccion();
        assertEquals("Buscar por scoring", a.getTipo());
        assertEquals("95", a.getDetalle());
    }

    @Test
    void buscarClientePorScoringNoExistente() {
        ejecutarMenu("2\n999\n\n0\n");
        assertFalse(historial.hayAcciones());
    }

    @Test
    void buscarClientePorScoringEntradaInvalida() {
        // "abc" falla, luego 88 funciona
        ejecutarMenu("2\nabc\n88\n\n0\n");
        assertTrue(historial.hayAcciones());
    }

    // --- Opcion 3: Agregar cliente ---

    @Test
    void agregarClienteNuevo() {
        // 3 = agregar, "Diana" = nombre, 60 = scoring, \n = enter, 0 = salir
        ejecutarMenu("3\nDiana\n60\n\n0\n");
        assertNotNull(gestor.buscarPorNombre("Diana"));
        assertEquals(60, gestor.buscarPorNombre("Diana").getScoring());
        assertTrue(historial.hayAcciones());
    }

    @Test
    void agregarClienteDuplicado() {
        // Intentar agregar "Alice" que ya existe
        ejecutarMenu("3\nAlice\n\n0\n");
        assertEquals(3, gestor.cantidadClientes()); // no se agrego otro
    }

    @Test
    void agregarClienteNombreVacioReintenta() {
        // Primer intento vacio, segundo "Eva" valido
        ejecutarMenu("3\n\nEva\n75\n\n0\n");
        assertNotNull(gestor.buscarPorNombre("Eva"));
    }

    // --- Opcion 4: Seguir cliente ---

    @Test
    void seguirClienteExitoso() {
        // 4 = seguir, "Alice" = seguidor, "Bob" = seguido, \n = enter, 0 = salir
        ejecutarMenu("4\nAlice\nBob\n\n0\n");
        assertTrue(cola.haySolicitudes());
        assertTrue(historial.hayAcciones());
        assertEquals("Seguir cliente", historial.deshacerUltimaAccion().getTipo());
    }

    @Test
    void seguirClienteSeguidorNoExiste() {
        ejecutarMenu("4\nNoExiste\nBob\n\n0\n");
        assertFalse(cola.haySolicitudes());
    }

    @Test
    void seguirClienteSeguidoNoExiste() {
        ejecutarMenu("4\nAlice\nNoExiste\n\n0\n");
        assertFalse(cola.haySolicitudes());
    }

    // --- Opcion 5: Deshacer accion ---

    @Test
    void deshacerSinAcciones() {
        ejecutarMenu("5\n\n0\n");
        assertFalse(historial.hayAcciones());
    }

    @Test
    void deshacerAccionBuscar() {
        // Primero buscar (1), luego deshacer (5)
        ejecutarMenu("1\nAlice\n\n5\n\n0\n");
        assertFalse(historial.hayAcciones());
    }

    @Test
    void deshacerAccionAgregarCliente() {
        // Agregar Diana (3), luego deshacer (5) -> debe eliminar Diana
        ejecutarMenu("3\nDiana\n60\n\n5\n\n0\n");
        assertNull(gestor.buscarPorNombre("Diana"));
        assertEquals(3, gestor.cantidadClientes());
    }

    @Test
    void deshacerAccionSeguirCliente() {
        // Seguir (4), luego deshacer (5) -> debe quitar solicitud de la cola
        ejecutarMenu("4\nAlice\nBob\n\n5\n\n0\n");
        assertFalse(cola.haySolicitudes());
    }

    @Test
    void deshacerAccionProcesarSolicitud() {
        // Agregar solicitud a la cola, procesarla (6), luego deshacer (5)
        cola.agregarSolicitud(new SolicitudSeguimiento("Alice", "Bob"));
        ejecutarMenu("6\n\n5\n\n0\n");
        // Deshacer "Procesar solicitud" quita el seguido
        assertFalse(gestor.buscarPorNombre("Alice").getSiguiendo().contains("Bob"));
    }

    // --- Opcion 6: Procesar solicitudes ---

    @Test
    void procesarSolicitudExitosa() {
        cola.agregarSolicitud(new SolicitudSeguimiento("Alice", "Bob"));
        ejecutarMenu("6\n\n0\n");
        assertTrue(gestor.buscarPorNombre("Alice").getSiguiendo().contains("Bob"));
        assertTrue(historial.hayAcciones());
    }

    @Test
    void procesarSolicitudSinPendientes() {
        ejecutarMenu("6\n\n0\n");
        assertFalse(historial.hayAcciones());
    }

    @Test
    void procesarSolicitudNoAplicable() {
        // Solicitud donde el seguido no existe
        cola.agregarSolicitud(new SolicitudSeguimiento("Alice", "NoExiste"));
        ejecutarMenu("6\n\n0\n");
        assertTrue(historial.hayAcciones()); // se registra igualmente
    }

    // --- Opcion 7: Listar solicitudes pendientes ---

    @Test
    void listarSolicitudesPendientesVacia() {
        ejecutarMenu("7\n\n0\n");
        // Solo verificamos que no rompa
    }

    @Test
    void listarSolicitudesPendientesConDatos() {
        cola.agregarSolicitud(new SolicitudSeguimiento("Alice", "Bob"));
        cola.agregarSolicitud(new SolicitudSeguimiento("Bob", "Charlie"));
        ejecutarMenu("7\n\n0\n");
        // Verificamos que la cola no fue modificada
        assertTrue(cola.haySolicitudes());
        assertEquals(2, cola.listarPendientes().size());
    }

    // --- Opcion 8: Listar ultimas acciones ---

    @Test
    void listarUltimasAccionesVacia() {
        ejecutarMenu("8\n\n0\n");
        // Solo verificamos que no rompa
    }

    @Test
    void listarUltimasAccionesConDatos() {
        historial.registrarAccion(new Accion("test1", "detalle1"));
        historial.registrarAccion(new Accion("test2", "detalle2"));
        ejecutarMenu("8\n\n0\n");
        // Las acciones no se modifican al listar
        assertTrue(historial.hayAcciones());
    }

    // --- Opcion 9: Listar todos los clientes ---

    @Test
    void listarTodosLosClientes() {
        ejecutarMenu("9\n\n0\n");
        assertTrue(historial.hayAcciones());
        assertEquals("Listar clientes", historial.deshacerUltimaAccion().getTipo());
    }

    @Test
    void listarTodosLosClientesVacio() {
        gestor = new GestorClientes(); // vacio
        ejecutarMenu("9\n\n0\n");
    }

    // --- crearMenu ---

    @Test
    void crearMenuDevuelveMenu() {
        Scanner sc = new Scanner(new ByteArrayInputStream("0\n".getBytes()));
        MenuRedSocial menuRedSocial = new MenuRedSocial(sc, gestor, historial, cola);
        Menu menu = menuRedSocial.crearMenu();
        assertNotNull(menu);
        sc.close();
    }

    // --- cargarDatosIniciales ---

    @Test
    void cargarDatosIniciales() {
        GestorClientes gestorVacio = new GestorClientes();
        Scanner sc = new Scanner(new ByteArrayInputStream("0\n".getBytes()));
        MenuRedSocial menuRedSocial = new MenuRedSocial(sc, gestorVacio, historial, cola);
        menuRedSocial.cargarDatosIniciales();
        assertNotNull(gestorVacio.buscarPorNombre("Alice"));
        sc.close();
    }

    // --- Flujo completo: multiples acciones ---

    @Test
    void flujoCompletoVariasAcciones() {
        // Agregar "Diana", seguir Alice->Bob, procesar solicitud, listar clientes, salir
        String input = "3\nDiana\n60\n\n4\nAlice\nBob\n\n6\n\n9\n\n0\n";
        ejecutarMenu(input);
        assertNotNull(gestor.buscarPorNombre("Diana"));
        assertTrue(gestor.buscarPorNombre("Alice").getSiguiendo().contains("Bob"));
        assertEquals(4, gestor.cantidadClientes());
    }

    @Test
    void opcionInvalidaNoRompeMenu() {
        ejecutarMenu("99\n\n0\n");
    }
}
