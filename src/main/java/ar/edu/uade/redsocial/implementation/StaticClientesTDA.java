package ar.edu.uade.redsocial.implementation;

import ar.edu.uade.redsocial.model.Cliente;
import ar.edu.uade.redsocial.tda.ClientesTDA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Implementación eficiente de ClientesTDA usando HashMap y TreeMap.
 * - HashMap nombre -> Cliente: búsqueda por nombre O(1).
 * - TreeMap scoring -> List(Cliente): búsqueda por scoring O(log n) acceso + O(1) lista.
 */
public class StaticClientesTDA implements ClientesTDA {

    private final Map<String, Cliente> clientesPorNombre = new HashMap<>();
    private final Map<Integer, List<Cliente>> clientesPorScoring = new TreeMap<>();

    @Override
    public boolean agregarCliente(Cliente cliente) { // complejidad O(s + c), s = siguiendo, c = conexiones
        if (clientesPorNombre.containsKey(cliente.getNombre())) {
            return false;
        }
        // Filtrar siguiendo/conexiones a solo clientes existentes
        List<String> sigValidos = new ArrayList<>();
        for (String s : cliente.getSiguiendo()) {
            if (clientesPorNombre.containsKey(s)) {
                sigValidos.add(s);
            }
        }
        List<String> connValidos = new ArrayList<>();
        for (String c : cliente.getConexiones()) {
            if (clientesPorNombre.containsKey(c)) {
                connValidos.add(c);
            }
        }
        Cliente clienteValido = new Cliente(cliente.getNombre(), cliente.getScoring(), sigValidos, connValidos);
        clientesPorNombre.put(clienteValido.getNombre(), clienteValido);
        clientesPorScoring.computeIfAbsent(clienteValido.getScoring(), k -> new ArrayList<>()).add(clienteValido);
        return true;
    }

    @Override
    public Cliente buscarPorNombre(String nombre) { // complejidad O(1)
        return clientesPorNombre.get(nombre);
    }

    @Override
    public List<Cliente> buscarPorScoring(int scoring) { // complejidad O(log n) por TreeMap
        return new ArrayList<>(clientesPorScoring.getOrDefault(scoring, new ArrayList<>()));
    }

    @Override
    public int cantidadClientes() { // complejidad O(1)
        return clientesPorNombre.size();
    }

    @Override
    public List<Cliente> listarClientes() { // complejidad O(n)
        return new ArrayList<>(clientesPorNombre.values());
    }

    @Override
    public boolean modificarSeguidor(Cliente cliente) { // complejidad O(k), k = clientes con mismo scoring viejo
        String nombre = cliente.getNombre();
        Cliente existente = clientesPorNombre.get(nombre);
        if (existente == null) {
            return false;
        }
        // Quitar de la lista del scoring anterior
        quitarDeScoring(existente);
        // Poner el cliente actualizado
        clientesPorNombre.put(nombre, cliente);
        clientesPorScoring.computeIfAbsent(cliente.getScoring(), k -> new ArrayList<>()).add(cliente);
        return true;
    }

    @Override
    public boolean agregarSeguido(String nombreCliente, String nombreSeguido) { // complejidad O(s), s = siguiendo del cliente
        Cliente cliente = clientesPorNombre.get(nombreCliente);
        if (cliente == null) {
            return false;
        }
        if (!clientesPorNombre.containsKey(nombreSeguido)) {
            return false;
        }
        if (nombreCliente.equals(nombreSeguido)) {
            return false;
        }
        if (cliente.getSiguiendo().contains(nombreSeguido)) {
            return false;
        }
        List<String> nuevoSiguiendo = new ArrayList<>(cliente.getSiguiendo());
        nuevoSiguiendo.add(nombreSeguido);
        Cliente actualizado = new Cliente(cliente.getNombre(), cliente.getScoring(),
                nuevoSiguiendo, new ArrayList<>(cliente.getConexiones()));
        return modificarSeguidor(actualizado);
    }

    @Override
    public boolean eliminarCliente(String nombre) { // complejidad O(n), n = total clientes (limpiar refs)
        Cliente cliente = clientesPorNombre.get(nombre);
        if (cliente == null) {
            return false;
        }
        // Quitar del mapa de scoring
        quitarDeScoring(cliente);
        // Quitar del mapa de nombre
        clientesPorNombre.remove(nombre);
        // Limpiar referencias en otros clientes
        for (Map.Entry<String, Cliente> entry : clientesPorNombre.entrySet()) {
            Cliente c = entry.getValue();
            boolean tieneSiguiendo = c.getSiguiendo().contains(nombre);
            boolean tieneConexion = c.getConexiones().contains(nombre);
            if (tieneSiguiendo || tieneConexion) {
                List<String> newSig = new ArrayList<>(c.getSiguiendo());
                List<String> newConn = new ArrayList<>(c.getConexiones());
                if (tieneSiguiendo) newSig.remove(nombre);
                if (tieneConexion) newConn.remove(nombre);
                Cliente limpio = new Cliente(c.getNombre(), c.getScoring(), newSig, newConn);
                entry.setValue(limpio);
                // Actualizar la referencia en el mapa de scoring
                reemplazarEnScoring(c, limpio);
            }
        }
        return true;
    }

    @Override
    public boolean quitarSeguido(String nombreCliente, String nombreSeguido) { // complejidad O(s), s = siguiendo del cliente
        Cliente cliente = clientesPorNombre.get(nombreCliente);
        if (cliente == null || !cliente.getSiguiendo().contains(nombreSeguido)) {
            return false;
        }
        List<String> nuevoSiguiendo = new ArrayList<>(cliente.getSiguiendo());
        nuevoSiguiendo.remove(nombreSeguido);
        Cliente actualizado = new Cliente(cliente.getNombre(), cliente.getScoring(),
                nuevoSiguiendo, new ArrayList<>(cliente.getConexiones()));
        return modificarSeguidor(actualizado);
    }

    /** Quita un cliente de la lista del TreeMap de scoring. */
    private void quitarDeScoring(Cliente cliente) { // complejidad O(k), k = clientes con mismo scoring
        List<Cliente> lista = clientesPorScoring.get(cliente.getScoring());
        if (lista != null) {
            lista.removeIf(c -> c.getNombre().equals(cliente.getNombre()));
            if (lista.isEmpty()) {
                clientesPorScoring.remove(cliente.getScoring());
            }
        }
    }

    /** Reemplaza la referencia vieja de un cliente por la nueva en el mapa de scoring. */
    private void reemplazarEnScoring(Cliente viejo, Cliente nuevo) { // complejidad O(k), k = clientes con mismo scoring
        List<Cliente> lista = clientesPorScoring.get(viejo.getScoring());
        if (lista != null) {
            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i).getNombre().equals(viejo.getNombre())) {
                    lista.set(i, nuevo);
                    return;
                }
            }
        }
    }
}
