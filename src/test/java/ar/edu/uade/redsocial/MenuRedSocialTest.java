package ar.edu.uade.redsocial;

import ar.edu.uade.redsocial.model.Cliente;
import ar.edu.uade.redsocial.model.SolicitudSeguimiento;
import ar.edu.uade.redsocial.services.ColaSolicitudesSeguimiento;
import ar.edu.uade.redsocial.services.GestorClientes;
import ar.edu.uade.redsocial.services.HistorialAcciones;
import ar.edu.uade.redsocial.utils.MenuRedSocial;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * MENÚ SIN SESIÓN (opciones 1-10, salir=0):
 *   1-Iniciar Sesion | 2-Buscar por nombre | 3-Buscar por puntuacion
 *   4-Registrarse | 5-Solicitudes de amistad | 6-Últimas acciones
 *   7-Ver todos los usuarios | 8-Explorar red | 9-Ver seguidos/amigos | 10-Distancia
 *
 * MENÚ CON SESIÓN (opciones 1-16, salir=0):
 *   1-Buscar nombre | 2-Buscar puntuacion | 3-Seguir | 4-Dejar de seguir
 *   5-Enviar solicitud amistad | 6-Aceptar solicitud amistad | 7-Rechazar solicitud amistad
 *   8-Deshacer | 9-Mis solicitudes | 10-Últimas acciones
 *   11-Ver todos | 12-Mis datos | 13-Explorar red | 14-Ver seguidos/amigos
 *   15-Distancia | 16-Cerrar Sesion | 0-SALIR
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

    private void ejecutarMenuSinLogin(String input) {
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        MenuRedSocial menuRedSocial = new MenuRedSocial(scanner, gestor, historial, cola);
        menuRedSocial.crearMenuSinLogin().ejecutar(scanner);
        scanner.close();
    }

    private void ejecutarMenuConLogin(String input, String nombreUsuario) {
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        MenuRedSocial menuRedSocial = new MenuRedSocial(scanner, gestor, historial, cola);
        menuRedSocial.setUsuarioLogueado(gestor.buscarPorNombre(nombreUsuario));
        menuRedSocial.crearMenuConLogin().ejecutar(scanner);
        scanner.close();
    }

    @Test
    void registrarNuevoUsuario() {
        ejecutarMenuSinLogin("4\nDiana\n60\n\n0\n");
        assertNotNull(gestor.buscarPorNombre("Diana"));
        assertEquals(60, gestor.buscarPorNombre("Diana").getScoring());
    }

    @Test
    void buscarClientePorNombreExistente() {
        ejecutarMenuSinLogin("2\nAlice\n\n0\n");
        assertTrue(historial.hayAcciones());
        assertEquals("Buscar por nombre", historial.deshacerUltimaAccion().getTipo());
    }

    @Test
    void seguirClienteDirecto() {
        ejecutarMenuConLogin("3\nBob\n\n0\n", "Alice");
        assertFalse(cola.haySolicitudes()); // no usa cola, es directo
        assertTrue(gestor.obtenerSeguidos("Alice").contains("Bob"));
    }

    @Test
    void deshacerAccionSeguir() {
        ejecutarMenuConLogin("3\nBob\n\n8\n\n0\n", "Alice");
        assertFalse(gestor.obtenerSeguidos("Alice").contains("Bob"));
    }

    @Test
    void enviarSolicitudAmistad() {
        ejecutarMenuConLogin("5\nBob\n\n0\n", "Alice");
        assertTrue(cola.haySolicitudes());
        assertEquals("Enviar solicitud amistad", historial.deshacerUltimaAccion().getTipo());
    }

    @Test
    void aceptarSolicitudAmistadCreaAmistad() {
        cola.agregarSolicitud(new SolicitudSeguimiento("Alice", "Bob"));
        ejecutarMenuConLogin("6\n1\n\n0\n", "Bob");
        assertTrue(gestor.obtenerVecinos("Alice").contains("Bob"));
        assertTrue(gestor.obtenerVecinos("Bob").contains("Alice"));
        assertFalse(gestor.obtenerSeguidos("Alice").contains("Bob")); // amistad ≠ seguimiento
    }

    @Test
    void rechazarSolicitudAmistad() {
        cola.agregarSolicitud(new SolicitudSeguimiento("Alice", "Bob"));
        ejecutarMenuConLogin("7\n1\n\n0\n", "Bob");
        assertFalse(cola.haySolicitudes());
        assertFalse(gestor.obtenerVecinos("Alice").contains("Bob"));
    }

    @Test
    void deshacerAceptarSolicitudAmistad() {
        cola.agregarSolicitud(new SolicitudSeguimiento("Alice", "Bob"));
        ejecutarMenuConLogin("6\n1\n\n8\n\n0\n", "Bob");
        assertFalse(gestor.obtenerVecinos("Alice").contains("Bob"));
        assertFalse(gestor.obtenerVecinos("Bob").contains("Alice"));
    }

    @Test
    void opcionInvalidaNoRompeMenu() {
        ejecutarMenuSinLogin("99\n\n0\n");
    }

    @Test
    void flujoCompletoRegistroSeguimientoAmistad() {
        ejecutarMenuSinLogin("4\nDiana\n60\n\n0\n");
        ejecutarMenuConLogin("3\nBob\n\n0\n", "Alice");
        ejecutarMenuConLogin("5\nBob\n\n0\n", "Alice");
        ejecutarMenuConLogin("6\n1\n\n0\n", "Bob");

        assertNotNull(gestor.buscarPorNombre("Diana"));
        assertTrue(gestor.obtenerSeguidos("Alice").contains("Bob"));
        assertTrue(gestor.obtenerVecinos("Alice").contains("Bob"));
    }
}
