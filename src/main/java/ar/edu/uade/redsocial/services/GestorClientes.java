package ar.edu.uade.redsocial.services;

import ar.edu.uade.redsocial.model.Cliente;

import java.util.*;

public class GestorClientes {

    private Map<String, Cliente> clientesPorNombre = new HashMap<>();
    private Map<Integer, List<Cliente>> clientesPorScoring = new TreeMap<>();

    public boolean agregarCliente(Cliente cliente) {
        if (clientesPorNombre.containsKey(cliente.getNombre())) {
            return false;
        }

        clientesPorNombre.put(cliente.getNombre(), cliente);

        clientesPorScoring
                .computeIfAbsent(cliente.getScoring(), k -> new ArrayList<>())
                .add(cliente);

        return true;
    }

    public boolean modificarSeguidor (Cliente cliente) {
        if (! clientesPorNombre.containsKey(cliente.getNombre())) {
            return false;
        }

        clientesPorNombre.put(cliente.getNombre(), cliente);

        clientesPorScoring
                .computeIfAbsent(cliente.getScoring(), k -> new ArrayList<>())
                .add(cliente);

        return true;
    }

    public Cliente buscarPorNombre(String nombre) {
        return clientesPorNombre.get(nombre);
    }

    public List<Cliente> buscarPorScoring(int scoring) {
        return clientesPorScoring.getOrDefault(scoring, new ArrayList<>());
    }

    public int cantidadClientes() {
        return clientesPorNombre.size();
    }

    public List<Cliente> listarClientes() {
        return new ArrayList<>(clientesPorNombre.values());
    }
}
