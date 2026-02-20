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

/**
 * Tests adaptados a la estructura de dos menús:
 *
 * MENÚ SIN SESIÓN (opciones 1-8, salir=0):
 *   1- Iniciar Sesion  |  2- Buscar por nombre  |  3- Buscar por Scoring
 *   4- Registrarse  |  5- Ver solicitudes pendientes
 *   6- Ultimas 10 acciones  |  7- Ver todos los clientes
 *   8- Consultar red de conexiones (ABB)  |  0- SALIR
 *
 * MENÚ CON SESIÓN (opciones 1-12, salir=0):
 *   1- Buscar por nombre  |  2- Buscar por Scoring  |  3- Seguir cliente
 *   4- Aceptar solicitud  |  5- Rechazar solicitud  |  6- Deshacer
 *   7- Mis solicitudes pendientes  |  8- Ultimas 10 acciones
 *   9- Ver todos los clientes  |  10- Ver mis datos
 *   11- Consultar red de conexiones (ABB)  |  12- Cerrar Sesion  |  0- SALIR
 */
class MenuRedSocialTest {

    private GestorClientes gestor;
    private HistorialAcciones historial;
    private ColaSolicitudesSeguimiento cola;

    @BeforeEach
    void setUp() {
        gestor = new GestorClientes();
        historial = new HistorialAcciones();
        cola = new ColaSolicitudesSeguimiento();
        gestor.agregarCliente(new Cliente("Alice", 95));
        gestor.agregarCliente(new Cliente("Bob", 88));
        gestor.agregarCliente(new Cliente("Charlie", 72));
    }

    /** Ejecuta el menú SIN sesión con el input dado. */
    private void ejecutarMenuSinLogin(String input) {
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        MenuRedSocial menuRedSocial = new MenuRedSocial(scanner, gestor, historial, cola);
        Menu menu = menuRedSocial.crearMenuSinLogin();
        menu.ejecutar(scanner);
        scanner.close();
    }

    /** Ejecuta el menú CON sesión activa como el usuario indicado. */
    private void ejecutarMenuConLogin(String input, String nombreUsuario) {
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        MenuRedSocial menuRedSocial = new MenuRedSocial(scanner, gestor, historial, cola);
        menuRedSocial.setUsuarioLogueado(gestor.buscarPorNombre(nombreUsuario));
        Menu menu = menuRedSocial.crearMenuConLogin();
        menu.ejecutar(scanner);
        scanner.close();
    }

    // ─── Sin sesión: Opcion 2 - Buscar por nombre ───

    @Test
    void buscarClientePorNombreExistente() {
        ejecutarMenuSinLogin("2\nAlice\n\n0\n");
        assertTrue(historial.hayAcciones());
        assertEquals("Buscar por nombre", historial.deshacerUltimaAccion().getTipo());
    }

    @Test
    void buscarClientePorNombreNoExistente() {
        ejecutarMenuSinLogin("2\nNoExiste\n\n0\n");
        assertFalse(historial.hayAcciones());
    }

    // ─── Sin sesión: Opcion 3 - Buscar por scoring ───

    @Test
    void buscarClientePorScoringExistente() {
        ejecutarMenuSinLogin("3\n95\n\n0\n");
        assertTrue(historial.hayAcciones());
        Accion a = historial.deshacerUltimaAccion();
        assertEquals("Buscar por scoring", a.getTipo());
        assertEquals("95", a.getDetalle());
    }

    @Test
    void buscarClientePorScoringNoExistente() {
        ejecutarMenuSinLogin("3\n999\n\n0\n");
        assertFalse(historial.hayAcciones());
    }

    @Test
    void buscarClientePorScoringEntradaInvalida() {
        ejecutarMenuSinLogin("3\nabc\n88\n\n0\n");
        assertTrue(historial.hayAcciones());
    }

    // ─── Sin sesión: Opcion 4 - Registrarse ───

    @Test
    void agregarClienteNuevo() {
        ejecutarMenuSinLogin("4\nDiana\n60\n\n0\n");
        assertNotNull(gestor.buscarPorNombre("Diana"));
        assertEquals(60, gestor.buscarPorNombre("Diana").getScoring());
        assertTrue(historial.hayAcciones());
    }

    @Test
    void agregarClienteDuplicado() {
        ejecutarMenuSinLogin("4\nAlice\n\n0\n");
        assertEquals(3, gestor.cantidadClientes());
    }

    @Test
    void agregarClienteNombreVacioReintenta() {
        ejecutarMenuSinLogin("4\n\nEva\n75\n\n0\n");
        assertNotNull(gestor.buscarPorNombre("Eva"));
    }

    // ─── Con sesión: Opcion 3 - Seguir cliente ───

    @Test
    void seguirClienteExitoso() {
        ejecutarMenuConLogin("3\nBob\n\n0\n", "Alice");
        assertTrue(cola.haySolicitudes());
        assertTrue(historial.hayAcciones());
        assertEquals("Seguir cliente", historial.deshacerUltimaAccion().getTipo());
    }

    @Test
    void seguirClienteSeguidoNoExiste() {
        ejecutarMenuConLogin("3\nNoExiste\n\n0\n", "Alice");
        assertFalse(cola.haySolicitudes());
    }

    // ─── Con sesión: Opcion 6 - Deshacer accion ───

    @Test
    void deshacerSinAcciones() {
        ejecutarMenuConLogin("6\n\n0\n", "Alice");
        assertFalse(historial.hayAcciones());
    }

    @Test
    void deshacerAccionBuscar() {
        ejecutarMenuConLogin("1\nAlice\n\n6\n\n0\n", "Alice");
        assertFalse(historial.hayAcciones());
    }

    @Test
    void deshacerAccionAgregarCliente() {
        ejecutarMenuSinLogin("4\nDiana\n60\n\n0\n");
        ejecutarMenuConLogin("6\n\n0\n", "Alice");
        assertNull(gestor.buscarPorNombre("Diana"));
        assertEquals(3, gestor.cantidadClientes());
    }

    @Test
    void deshacerAccionSeguirCliente() {
        ejecutarMenuConLogin("3\nBob\n\n6\n\n0\n", "Alice");
        assertFalse(cola.haySolicitudes());
    }

    @Test
    void deshacerAccionAceptarSolicitud() {
        cola.agregarSolicitud(new SolicitudSeguimiento("Alice", "Bob"));
        ejecutarMenuConLogin("4\n1\n\n6\n\n0\n", "Bob");
        assertFalse(gestor.buscarPorNombre("Alice").getSiguiendo().contains("Bob"));
    }

    // ─── Con sesión: Opcion 4 - Aceptar solicitud ───

    @Test
    void aceptarSolicitudExitosa() {
        cola.agregarSolicitud(new SolicitudSeguimiento("Alice", "Bob"));
        ejecutarMenuConLogin("4\n1\n\n0\n", "Bob");
        assertTrue(gestor.buscarPorNombre("Alice").getSiguiendo().contains("Bob"));
        assertTrue(historial.hayAcciones());
    }

    @Test
    void aceptarSolicitudSinPendientes() {
        ejecutarMenuConLogin("4\n\n0\n", "Alice");
        assertFalse(historial.hayAcciones());
    }

    @Test
    void aceptarSolicitudNoAplicable() {
        cola.agregarSolicitud(new SolicitudSeguimiento("NoExiste", "Alice"));
        ejecutarMenuConLogin("4\n1\n\n0\n", "Alice");
        assertTrue(historial.hayAcciones());
    }

    // ─── Con sesión: Opcion 5 - Rechazar solicitud ───

    @Test
    void rechazarSolicitudExitosa() {
        cola.agregarSolicitud(new SolicitudSeguimiento("Alice", "Bob"));
        ejecutarMenuConLogin("5\n1\n\n0\n", "Bob");
        assertFalse(cola.haySolicitudes());
        assertFalse(gestor.buscarPorNombre("Alice").getSiguiendo().contains("Bob"));
    }

    // ─── Sin sesión: Opcion 5 - Listar solicitudes pendientes ───

    @Test
    void listarSolicitudesPendientesVacia() {
        ejecutarMenuSinLogin("5\n\n0\n");
    }

    @Test
    void listarSolicitudesPendientesConDatos() {
        cola.agregarSolicitud(new SolicitudSeguimiento("Alice", "Bob"));
        cola.agregarSolicitud(new SolicitudSeguimiento("Bob", "Charlie"));
        ejecutarMenuSinLogin("5\n\n0\n");
        assertTrue(cola.haySolicitudes());
        assertEquals(2, cola.listarPendientes().size());
    }

    // ─── Sin sesión: Opcion 6 - Listar ultimas acciones ───

    @Test
    void listarUltimasAccionesVacia() {
        ejecutarMenuSinLogin("6\n\n0\n");
    }

    @Test
    void listarUltimasAccionesConDatos() {
        historial.registrarAccion(new Accion("test1", "detalle1"));
        historial.registrarAccion(new Accion("test2", "detalle2"));
        ejecutarMenuSinLogin("6\n\n0\n");
        assertTrue(historial.hayAcciones());
    }

    // ─── Sin sesión: Opcion 7 - Listar todos los clientes ───

    @Test
    void listarTodosLosClientes() {
        ejecutarMenuSinLogin("7\n\n0\n");
        assertTrue(historial.hayAcciones());
        assertEquals("Listar clientes", historial.deshacerUltimaAccion().getTipo());
    }

    @Test
    void listarTodosLosClientesVacio() {
        gestor = new GestorClientes();
        ejecutarMenuSinLogin("7\n\n0\n");
    }

    // ─── crearMenuSinLogin devuelve Menu ───

    @Test
    void crearMenuDevuelveMenu() {
        Scanner sc = new Scanner(new ByteArrayInputStream("0\n".getBytes()));
        MenuRedSocial menuRedSocial = new MenuRedSocial(sc, gestor, historial, cola);
        Menu menu = menuRedSocial.crearMenuSinLogin();
        assertNotNull(menu);
        sc.close();
    }

    // ─── cargarDatosIniciales ───

    @Test
    void cargarDatosIniciales() {
        GestorClientes gestorVacio = new GestorClientes();
        Scanner sc = new Scanner(new ByteArrayInputStream("0\n".getBytes()));
        MenuRedSocial menuRedSocial = new MenuRedSocial(sc, gestorVacio, historial, cola);
        menuRedSocial.cargarDatosIniciales();
        assertNotNull(gestorVacio.buscarPorNombre("Alice"));
        sc.close();
    }

    // ─── Flujo completo ───

    @Test
    void flujoCompletoVariasAcciones() {
        ejecutarMenuSinLogin("4\nDiana\n60\n\n0\n");
        ejecutarMenuConLogin("3\nBob\n\n0\n", "Alice");
        ejecutarMenuConLogin("4\n1\n\n9\n\n0\n", "Bob");

        assertNotNull(gestor.buscarPorNombre("Diana"));
        assertTrue(gestor.buscarPorNombre("Alice").getSiguiendo().contains("Bob"));
        assertEquals(4, gestor.cantidadClientes());
    }

    @Test
    void opcionInvalidaNoRompeMenu() {
        ejecutarMenuSinLogin("99\n\n0\n");
    }
}
