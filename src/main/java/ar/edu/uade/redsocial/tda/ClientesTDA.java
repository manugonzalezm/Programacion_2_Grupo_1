package ar.edu.uade.redsocial.tda;

import ar.edu.uade.redsocial.model.Cliente;

import java.util.List;

/**
 * TDA para la gestión de clientes.
 * Implementación eficiente con HashMap (nombre) y TreeMap (scoring).
 */
public interface ClientesTDA {

    /**
     * Agrega un cliente. Si ya existe uno con el mismo nombre, no se agrega.
     * Incluye siguiendo y conexiones del cliente.
     */
    boolean agregarCliente(Cliente cliente); // complejidad O(s + c)

    /** Devuelve el cliente con el nombre dado, o null si no existe. */
    Cliente buscarPorNombre(String nombre); // complejidad O(1)

    /** Devuelve todos los clientes con el scoring indicado. */
    List<Cliente> buscarPorScoring(int scoring); // complejidad O(log n)

    /** Cantidad de clientes almacenados. */
    int cantidadClientes(); // complejidad O(1)

    /** Lista todos los clientes (nombre, scoring, siguiendo, conexiones). */
    List<Cliente> listarClientes(); // complejidad O(n)

    /**
     * Actualiza scoring, siguiendo y conexiones del cliente identificado por nombre.
     * Retorna false si el cliente no existe.
     */
    boolean modificarSeguidor(Cliente cliente); // complejidad O(k)

    /**
     * Agrega "nombreSeguido" a la lista de siguiendo de "nombreCliente".
     * Retorna true solo si ambos clientes existen, el seguido no es el mismo cliente y aún no lo seguía.
     */
    boolean agregarSeguido(String nombreCliente, String nombreSeguido); // complejidad O(s)

    /**
     * Elimina el cliente con el nombre dado. Retorna false si no existe.
     * Usado para deshacer "Agregar cliente".
     */
    boolean eliminarCliente(String nombre); // complejidad O(n)

    /**
     * Quita "nombreSeguido" de la lista de siguiendo de "nombreCliente".
     * Retorna true si existía y se quitó. Usado para deshacer "Procesar solicitud".
     */
    boolean quitarSeguido(String nombreCliente, String nombreSeguido); // complejidad O(s)
}
