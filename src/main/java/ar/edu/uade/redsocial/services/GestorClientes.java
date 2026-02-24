package ar.edu.uade.redsocial.services;

import ar.edu.uade.redsocial.implementation.StaticClientesTDA;
import ar.edu.uade.redsocial.model.Cliente;
import ar.edu.uade.redsocial.model.SolicitudSeguimiento;

import java.util.List;
import java.util.Set;

/**
 * Servicio que expone las operaciones sobre clientes.
 * Internamente delega todo en StaticClientesTDA, que combina
 * un HashMap, un TreeMap y dos grafos (dirigido y no dirigido).
 *
 * Las solicitudes de amistad son per-usuario (List<SolicitudSeguimiento>
 * dentro de cada Cliente), no en una cola global.
 */
public class GestorClientes {

    private final StaticClientesTDA clientesTDA;

    public GestorClientes() {
        this.clientesTDA = new StaticClientesTDA();
    }

    public boolean agregarCliente(Cliente cliente) { // O(1)
        return clientesTDA.agregarCliente(cliente);
    }

    public boolean modificarCliente(Cliente cliente) { // O(log n)
        return clientesTDA.modificarCliente(cliente);
    }

    public boolean eliminarCliente(String nombre) { // O(Vert)
        return clientesTDA.eliminarCliente(nombre);
    }

    public Cliente buscarPorNombre(String nombre) { // O(1)
        return clientesTDA.buscarPorNombre(nombre);
    }

    public List<Cliente> buscarPorScoring(int scoring) { // O(log n)
        return clientesTDA.buscarPorScoring(scoring);
    }

    public int cantidadClientes() { // O(1)
        return clientesTDA.cantidadClientes();
    }

    public List<Cliente> listarClientes() { // O(n)
        return clientesTDA.listarClientes();
    }

    // --- seguimiento (sin aprobación) ---

    public boolean agregarSeguido(String nombreCliente, String nombreSeguido) { // O(grado)
        return clientesTDA.agregarSeguido(nombreCliente, nombreSeguido);
    }

    public boolean quitarSeguido(String nombreCliente, String nombreSeguido) { // O(grado)
        return clientesTDA.quitarSeguido(nombreCliente, nombreSeguido);
    }

    public Set<String> obtenerSeguidos(String nombre) { // O(grado)
        return clientesTDA.obtenerSeguidos(nombre);
    }

    public int calcularDistanciaSeguimiento(String origen, String destino) { // O(Vert+Arist)
        return clientesTDA.calcularDistanciaSeguimiento(origen, destino);
    }

    // --- amistades (requieren solicitud y aceptación) ---

    public void agregarAmistad(String a, String b) { // O(grado)
        clientesTDA.agregarAmistad(a, b);
    }

    public void eliminarAmistad(String a, String b) { // O(grado)
        clientesTDA.eliminarAmistad(a, b);
    }

    public Set<String> obtenerVecinos(String nombre) { // O(grado)
        return clientesTDA.obtenerVecinos(nombre);
    }

    public int calcularDistanciaAmistad(String origen, String destino) { // O(Vert+Arist)
        return clientesTDA.calcularDistanciaAmistad(origen, destino);
    }

    public List<Integer> consultarConexionesNivel4(String nombre) { // O(Vert+Arist+Vert·log Vert)
        return clientesTDA.consultarConexionesNivel4(nombre);
    }

    // --- solicitudes de amistad (por usuario) ---

    /** Envía una solicitud de amistad al receptor. O(n_solicitudes_receptor) */
    public boolean enviarSolicitudAmistad(String emisor, String receptor) {
        return clientesTDA.enviarSolicitudAmistad(emisor, receptor);
    }

    /** Devuelve las solicitudes recibidas por el usuario, ordenadas por llegada. O(1) */
    public List<SolicitudSeguimiento> listarSolicitudesRecibidas(String nombre) {
        return clientesTDA.listarSolicitudesRecibidas(nombre);
    }

    /**
     * Acepta la solicitud en la posición indice (0-based) del receptor,
     * creando la amistad bidireccional. O(grado)
     */
    public boolean aceptarSolicitudAmistad(String receptor, int indice) {
        return clientesTDA.aceptarSolicitudAmistad(receptor, indice);
    }

    /** Rechaza la solicitud en la posición indice (0-based). O(1) */
    public boolean rechazarSolicitudAmistad(String receptor, int indice) {
        return clientesTDA.rechazarSolicitudAmistad(receptor, indice);
    }

    /** Revoca una solicitud enviada. Usado para deshacer "Enviar solicitud amistad". O(n_solicitudes_receptor) */
    public boolean revocarSolicitudAmistad(String emisor, String receptor) {
        return clientesTDA.revocarSolicitudAmistad(emisor, receptor);
    }
}
