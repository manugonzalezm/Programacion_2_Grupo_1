package ar.edu.uade.redsocial.tda;

import ar.edu.uade.redsocial.model.Cliente;

import java.util.List;
import java.util.Set;

/**
 * Define las operaciones disponibles sobre el conjunto de clientes.
 * Maneja datos básicos (nombre, scoring) y dos tipos de relaciones:
 * seguimiento directo y amistades (estas últimas con solicitud previa).
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

    // --- ABB de conexiones ---

    /**
     * Recorre la red de seguimiento con BFS, inserta los scorings en un ABB
     * y devuelve los del nivel 4.
     */
    List<Integer> consultarConexionesNivel4(String nombre); // O(Vert + Arist + Vert·log Vert)
}
