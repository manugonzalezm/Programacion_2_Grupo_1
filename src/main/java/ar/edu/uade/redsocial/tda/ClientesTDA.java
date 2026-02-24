package ar.edu.uade.redsocial.tda;

import ar.edu.uade.redsocial.model.Cliente;
import ar.edu.uade.redsocial.model.SolicitudSeguimiento;

import java.util.List;
import java.util.Set;

/**
 * Define las operaciones disponibles sobre el conjunto de clientes.
 * Maneja datos básicos (nombre, scoring) y dos tipos de relaciones:
 * seguimiento directo y amistades (estas últimas con solicitud previa).
 *
 * Las solicitudes de amistad se almacenan por usuario (no en una cola global),
 * lo que permite acceso por índice en O(1) y operaciones acotadas al volumen
 * del usuario receptor en lugar del volumen global del sistema.
 */
public interface ClientesTDA {

    boolean agregarCliente(Cliente cliente); // O(1)

    Cliente buscarPorNombre(String nombre); // O(1)

    List<Cliente> buscarPorScoring(int scoring); // O(log n)

    int cantidadClientes(); // O(1)

    List<Cliente> listarClientes(); // O(n)

    /** Actualiza el scoring del cliente. Retorna false si no existe. */
    boolean modificarCliente(Cliente cliente); // O(log n)

    boolean eliminarCliente(String nombre); // O(V)

    // --- seguimiento ---

    /**
     * Agrega el seguimiento de nombreCliente hacia nombreSeguido.
     * Cada cliente puede seguir como máximo 2 usuarios.
     */
    boolean agregarSeguido(String nombreCliente, String nombreSeguido); // O(grado)

    boolean quitarSeguido(String nombreCliente, String nombreSeguido); // O(grado)

    Set<String> obtenerSeguidos(String nombreCliente); // O(grado)

    /** Distancia en saltos entre dos clientes por seguimiento (BFS). */
    int calcularDistanciaSeguimiento(String origen, String destino); // O(Vert + Arist)

    // --- amistades ---

    /** Crea la amistad entre a y b en ambas direcciones. */
    void agregarAmistad(String a, String b); // O(grado)

    void eliminarAmistad(String a, String b); // O(grado)

    Set<String> obtenerVecinos(String nombreCliente); // O(grado)

    /** Distancia en saltos entre dos clientes por amistad (BFS). */
    int calcularDistanciaAmistad(String origen, String destino); // O(Vert + Arist)

    // --- solicitudes de amistad (por usuario) ---

    /**
     * Encola una solicitud de amistad en la lista del receptor.
     * Retorna false si alguno no existe, son el mismo usuario, o ya existe la solicitud.
     */
    boolean enviarSolicitudAmistad(String emisor, String receptor); // O(n_solicitudes_receptor)

    /**
     * Devuelve las solicitudes de amistad recibidas por el usuario, ordenadas por llegada.
     * Lista vacía si el usuario no existe.
     */
    List<SolicitudSeguimiento> listarSolicitudesRecibidas(String nombre); // O(1)

    /**
     * Acepta la solicitud en la posición indice (0-based) de la lista del receptor,
     * crea la amistad bidireccional y la elimina de la lista.
     * Retorna false si el índice es inválido o el usuario no existe.
     */
    boolean aceptarSolicitudAmistad(String receptor, int indice); // O(grado)

    /**
     * Rechaza (elimina sin crear amistad) la solicitud en la posición indice (0-based).
     * Retorna false si el índice es inválido o el usuario no existe.
     */
    boolean rechazarSolicitudAmistad(String receptor, int indice); // O(1)

    /**
     * Revoca (cancela) una solicitud enviada por emisor a receptor.
     * Usado para deshacer "Enviar solicitud amistad".
     * Retorna false si no existía esa solicitud.
     */
    boolean revocarSolicitudAmistad(String emisor, String receptor); // O(n_solicitudes_receptor)

    // --- ABB de conexiones ---

    /**
     * Recorre la red de seguimiento con BFS, inserta los scorings en un ABB
     * y devuelve los del nivel 4.
     */
    List<Integer> consultarConexionesNivel4(String nombre); // O(Vert + Arist + Vert·log Vert)
}
