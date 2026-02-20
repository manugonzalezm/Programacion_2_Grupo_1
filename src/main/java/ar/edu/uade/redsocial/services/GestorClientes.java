package ar.edu.uade.redsocial.services;

import ar.edu.uade.redsocial.implementation.StaticClientesTDA;
import ar.edu.uade.redsocial.model.Cliente;
import ar.edu.uade.redsocial.tda.ClientesTDA;

import java.util.List;

/**
 * Servicio de gestión de clientes. Delega en ClientesTDA (StaticClientesTDA).
 * Usa HashMap para búsqueda por nombre O(1) y TreeMap para búsqueda por scoring O(log n).
 */
public class GestorClientes {

    private final ClientesTDA clientesTDA;

    public GestorClientes() { // complejidad O(1)
        this.clientesTDA = new StaticClientesTDA();
    }

    public boolean agregarCliente(Cliente cliente) { // complejidad O(s + c), s = siguiendo, c = conexiones
        return clientesTDA.agregarCliente(cliente);
    }

    public boolean modificarSeguidor(Cliente cliente) { // complejidad O(k), k = clientes con mismo scoring
        return clientesTDA.modificarSeguidor(cliente);
    }

    /**
     * Aplica una relación de seguimiento (solicitante pasa a seguir a solicitado).
     * Retorna true si ambos existen, son distintos y no se seguía ya; actualiza la lista de clientes.
     */
    public boolean agregarSeguido(String nombreCliente, String nombreSeguido) { // complejidad O(s), s = siguiendo
        return clientesTDA.agregarSeguido(nombreCliente, nombreSeguido);
    }

    /** Elimina el cliente. Usado al deshacer "Agregar cliente". */
    public boolean eliminarCliente(String nombre) { // complejidad O(n), n = total clientes
        return clientesTDA.eliminarCliente(nombre);
    }

    /** Quita un seguido. Usado al deshacer "Procesar solicitud". */
    public boolean quitarSeguido(String nombreCliente, String nombreSeguido) { // complejidad O(s), s = siguiendo
        return clientesTDA.quitarSeguido(nombreCliente, nombreSeguido);
    }

    public Cliente buscarPorNombre(String nombre) { // complejidad O(1)
        return clientesTDA.buscarPorNombre(nombre);
    }

    public List<Cliente> buscarPorScoring(int scoring) { // complejidad O(log n)
        return clientesTDA.buscarPorScoring(scoring);
    }

    public int cantidadClientes() { // complejidad O(1)
        return clientesTDA.cantidadClientes();
    }

    public List<Cliente> listarClientes() { // complejidad O(n)
        return clientesTDA.listarClientes();
    }

    /**
     * Recorre la red transitiva de "siguiendo" del cliente, construye un ABB
     * con los scorings alcanzables y retorna los que caen en el nivel 4 del árbol.
     */
    public List<Integer> consultarConexionesNivel4(String nombre) { // complejidad O(v + e + v log v)
        return clientesTDA.consultarConexionesNivel4(nombre);
    }
}
