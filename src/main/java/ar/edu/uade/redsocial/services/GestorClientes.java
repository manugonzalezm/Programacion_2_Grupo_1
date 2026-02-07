package ar.edu.uade.redsocial.services;

import ar.edu.uade.redsocial.implementation.StaticClientesTDA;
import ar.edu.uade.redsocial.model.Cliente;
import ar.edu.uade.redsocial.tda.ClientesTDA;

import java.util.List;

/**
 * Servicio de gestión de clientes. Delega en ClientesTDA (StaticClientesTDA)
 * para usar TDAs con complejidad adecuada; mantiene la misma API pública.
 */
public class GestorClientes {

    private final ClientesTDA clientesTDA;

    public GestorClientes() { // complejidad O(1)
        this.clientesTDA = new StaticClientesTDA();
    }

    public boolean agregarCliente(Cliente cliente) { // complejidad O(n), ver StaticClientesTDA
        return clientesTDA.agregarCliente(cliente);
    }

    public boolean modificarSeguidor(Cliente cliente) { // complejidad O(n), ver StaticClientesTDA
        return clientesTDA.modificarSeguidor(cliente);
    }

    /**
     * Aplica una relación de seguimiento (solicitante pasa a seguir a solicitado).
     * Retorna true si ambos existen, son distintos y no se seguía ya; actualiza la lista de clientes.
     */
    public boolean agregarSeguido(String nombreCliente, String nombreSeguido) { // complejidad O(n), ver StaticClientesTDA
        return clientesTDA.agregarSeguido(nombreCliente, nombreSeguido);
    }

    /** Elimina el cliente. Usado al deshacer "Agregar cliente". */
    public boolean eliminarCliente(String nombre) { // complejidad O(n), ver StaticClientesTDA
        return clientesTDA.eliminarCliente(nombre);
    }

    /** Quita un seguido. Usado al deshacer "Procesar solicitud". */
    public boolean quitarSeguido(String nombreCliente, String nombreSeguido) { // complejidad O(n), ver StaticClientesTDA
        return clientesTDA.quitarSeguido(nombreCliente, nombreSeguido);
    }

    public Cliente buscarPorNombre(String nombre) { // complejidad O(n), ver StaticClientesTDA
        return clientesTDA.buscarPorNombre(nombre);
    }

    public List<Cliente> buscarPorScoring(int scoring) { // complejidad O(n*m), ver StaticClientesTDA
        return clientesTDA.buscarPorScoring(scoring);
    }

    public int cantidadClientes() { // complejidad O(1)
        return clientesTDA.cantidadClientes();
    }

    public List<Cliente> listarClientes() { // complejidad O(n*m), ver StaticClientesTDA
        return clientesTDA.listarClientes();
    }
}
