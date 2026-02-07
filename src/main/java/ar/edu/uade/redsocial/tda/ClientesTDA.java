package ar.edu.uade.redsocial.tda;

import ar.edu.uade.redsocial.model.Cliente;

import java.util.List;

/**
 * TDA para la gestión de clientes.
 * Replica la API de TDAClientes con enfoque en uso de TDAs básicos (eficiencia y complejidad adecuada).
 */
public interface ClientesTDA {

    /**
     * Agrega un cliente. Si ya existe uno con el mismo nombre, no se agrega.
     * Incluye siguiendo y conexiones del cliente.
     */
    boolean agregarCliente(Cliente cliente); // complejidad O(n)

    /** Devuelve el cliente con el nombre dado, o null si no existe. */
    Cliente buscarPorNombre(String nombre); // complejidad O(n)

    /** Devuelve todos los clientes con el scoring indicado. */
    List<Cliente> buscarPorScoring(int scoring); // complejidad O(n*m)

    /** Cantidad de clientes almacenados. */
    int cantidadClientes(); // complejidad O(1)

    /** Lista todos los clientes (nombre, scoring, siguiendo, conexiones). */
    List<Cliente> listarClientes(); // complejidad O(n*m)

    /**
     * Actualiza scoring, siguiendo y conexiones del cliente identificado por nombre.
     * Retorna false si el cliente no existe.
     */
    boolean modificarSeguidor(Cliente cliente); // complejidad O(n)

    /**
     * Agrega "nombreSeguido" a la lista de siguiendo de "nombreCliente" (impacto en la lista de clientes).
     * Retorna true solo si ambos clientes existen, el seguido no es el mismo cliente y aún no lo seguía.
     */
    boolean agregarSeguido(String nombreCliente, String nombreSeguido); // complejidad O(n)

    /**
     * Elimina el cliente con el nombre dado. Retorna false si no existe.
     * Usado para deshacer "Agregar cliente".
     */
    boolean eliminarCliente(String nombre); // complejidad O(n)

    /**
     * Quita "nombreSeguido" de la lista de siguiendo de "nombreCliente".
     * Retorna true si existía y se quitó. Usado para deshacer "Procesar solicitud".
     */
    boolean quitarSeguido(String nombreCliente, String nombreSeguido); // complejidad O(n)
}
