package ar.edu.uade.redsocial.services;

import ar.edu.uade.redsocial.model.Cliente;
import ar.edu.uade.redsocial.tda.TDAClientes;

import java.util.*;

// Implementación concreta del TDAClientes
public class GestorClientes implements TDAClientes {

    private Map<String, Cliente> clientesPorNombre = new HashMap<>();
    private Map<Integer, List<Cliente>> clientesPorScoring = new TreeMap<>();

    @Override
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

    @Override
    public Cliente buscarPorNombre(String nombre) {
        return clientesPorNombre.get(nombre);
    }

    @Override
    public List<Cliente> buscarPorScoring(int scoring) {
        return clientesPorScoring.getOrDefault(scoring, new ArrayList<>());
    }

    @Override
    public int cantidadClientes() {
        return clientesPorNombre.size();
    }
}
