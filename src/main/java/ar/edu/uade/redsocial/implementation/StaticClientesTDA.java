package ar.edu.uade.redsocial.implementation;

import ar.edu.uade.redsocial.model.Cliente;
import ar.edu.uade.redsocial.tda.ClientesTDA;
import ar.edu.uade.redsocial.EstructuraABB.ABB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;

/**
 * Implementación eficiente de ClientesTDA usando HashMap y TreeMap.
 * - HashMap nombre -> Buscar cliente por nombre → O(1)
 * - TreeMap scoring -> Insertar por scoring → O(log n)
 */
public class StaticClientesTDA implements ClientesTDA {

    private final Map<String, Cliente> clientesPorNombre = new HashMap<>();
    private final Map<Integer, List<String>> clientesPorScoring = new TreeMap<>();

    @Override
    public boolean agregarCliente(Cliente cliente) {

        if (clientesPorNombre.containsKey(cliente.getNombre())) {
            return false;
        }

        List<String> siguiendoValido = new ArrayList<>();
        for (String s : cliente.getSiguiendo()) {
            if (clientesPorNombre.containsKey(s)) {
                siguiendoValido.add(s);
            }
        }

        Cliente clienteValido = new Cliente(
                cliente.getNombre(),
                cliente.getScoring(),
                siguiendoValido,
                cliente.getConexiones(),
                cliente.getSolicitudesPendientes()
        );

        clientesPorNombre.put(clienteValido.getNombre(), clienteValido);

        clientesPorScoring
                .computeIfAbsent(clienteValido.getScoring(), k -> new ArrayList<>())
                .add(clienteValido.getNombre());

        return true;
    }

    @Override
    public Cliente buscarPorNombre(String nombre) {
        return clientesPorNombre.get(nombre);
    }

    @Override
    public List<Cliente> buscarPorScoring(int scoring) {

        List<String> nombres =
                clientesPorScoring.getOrDefault(scoring, new ArrayList<>());

        List<Cliente> resultado = new ArrayList<>();

        for (String nombre : nombres) {
            Cliente cliente = clientesPorNombre.get(nombre);
            if (cliente != null) {
                resultado.add(cliente);
            }
        }

        return resultado;
    }

    @Override
    public int cantidadClientes() {
        return clientesPorNombre.size();
    }

    @Override
    public List<Cliente> listarClientes() {
        return new ArrayList<>(clientesPorNombre.values());
    }

    @Override
    public boolean modificarSeguidor(Cliente cliente) {

        String nombre = cliente.getNombre();
        Cliente existente = clientesPorNombre.get(nombre);

        if (existente == null) {
            return false;
        }

        // Quitar del índice anterior
        quitarDeScoring(existente);

        // Actualizar en HashMap
        clientesPorNombre.put(nombre, cliente);

        // Agregar al nuevo índice
        clientesPorScoring
                .computeIfAbsent(cliente.getScoring(), k -> new ArrayList<>())
                .add(cliente.getNombre());

        return true;
    }

    @Override
    public boolean agregarSeguido(String nombreCliente, String nombreSeguido) {

        Cliente cliente = clientesPorNombre.get(nombreCliente);
        Cliente seguido = clientesPorNombre.get(nombreSeguido);

        if (cliente == null || seguido == null) return false;
        if (nombreCliente.equals(nombreSeguido)) return false;
        if (cliente.getSiguiendo().size() >= 2) return false;
        if (cliente.getSiguiendo().contains(nombreSeguido)) return false;

        List<String> nuevoSiguiendo = new ArrayList<>(cliente.getSiguiendo());
        nuevoSiguiendo.add(nombreSeguido);

        Cliente actualizado = new Cliente(
                cliente.getNombre(),
                cliente.getScoring(),
                nuevoSiguiendo,
                cliente.getConexiones(),
                cliente.getSolicitudesPendientes()
        );

        clientesPorNombre.put(nombreCliente, actualizado);
        return true;
    }
    


    @Override
    public boolean quitarSeguido(String nombreCliente, String nombreSeguido) {

        Cliente cliente = clientesPorNombre.get(nombreCliente);
        if (cliente == null) return false;
        if (!cliente.getSiguiendo().contains(nombreSeguido)) return false;

        List<String> nuevoSiguiendo = new ArrayList<>(cliente.getSiguiendo());
        nuevoSiguiendo.remove(nombreSeguido);

        Cliente actualizado = new Cliente(
                cliente.getNombre(),
                cliente.getScoring(),
                nuevoSiguiendo,
                cliente.getConexiones(),
                cliente.getSolicitudesPendientes()
        );

        clientesPorNombre.put(nombreCliente, actualizado);

        return true;
    }


    
    @Override
    public boolean eliminarCliente(String nombre) {

        Cliente cliente = clientesPorNombre.get(nombre);
        if (cliente == null) return false;

        quitarDeScoring(cliente);
        clientesPorNombre.remove(nombre);

        for (String key : new ArrayList<>(clientesPorNombre.keySet())) {
            Cliente c = clientesPorNombre.get(key);
            List<String> nuevoSiguiendo = new ArrayList<>(c.getSiguiendo());
            List<String> nuevasConexiones = new ArrayList<>(c.getConexiones());
            List<String> nuevasSolicitudes = new ArrayList<>(c.getSolicitudesPendientes());

            boolean changed = nuevoSiguiendo.remove(nombre)
                            | nuevasConexiones.remove(nombre)
                            | nuevasSolicitudes.remove(nombre);

            if (changed) {
                Cliente actualizado = new Cliente(
                        c.getNombre(), c.getScoring(),
                        nuevoSiguiendo, nuevasConexiones, nuevasSolicitudes
                );
                clientesPorNombre.put(key, actualizado);
            }
        }

        return true;
    }


    /** Quita un cliente del índice por scoring */
    private void quitarDeScoring(Cliente cliente) {

        List<String> lista = clientesPorScoring.get(cliente.getScoring());

        if (lista != null) {

            lista.removeIf(nombre -> nombre.equals(cliente.getNombre()));

            if (lista.isEmpty()) {
                clientesPorScoring.remove(cliente.getScoring());
            }
        }
    }


    public boolean enviarSolicitud(String emisor, String receptor) {

        Cliente clienteReceptor = clientesPorNombre.get(receptor);
        if (clienteReceptor == null) return false;
        if (emisor.equals(receptor)) return false;
        if (clienteReceptor.getSolicitudesPendientes().contains(emisor)) return false;

        List<String> nuevasSolicitudes =
                new ArrayList<>(clienteReceptor.getSolicitudesPendientes());
        nuevasSolicitudes.add(emisor);

        Cliente actualizado = new Cliente(
                clienteReceptor.getNombre(),
                clienteReceptor.getScoring(),
                clienteReceptor.getSiguiendo(),
                clienteReceptor.getConexiones(),
                nuevasSolicitudes
        );

        clientesPorNombre.put(receptor, actualizado);

        return true;
    }

    public boolean aceptarSolicitud(String receptor, String emisor) {

        Cliente clienteReceptor = clientesPorNombre.get(receptor);
        Cliente clienteEmisor = clientesPorNombre.get(emisor);

        if (clienteReceptor == null || clienteEmisor == null) return false;
        if (!clienteReceptor.getSolicitudesPendientes().contains(emisor)) return false;

        //Cada cliente puede seguir hasta dos clientes
        if (clienteEmisor.getSiguiendo().size() >= 2) return false;

        // quitar solicitud
        List<String> nuevasSolicitudes =
                new ArrayList<>(clienteReceptor.getSolicitudesPendientes());
        nuevasSolicitudes.remove(emisor);

        Cliente receptorActualizado = new Cliente(
                clienteReceptor.getNombre(),
                clienteReceptor.getScoring(),
                clienteReceptor.getSiguiendo(),
                clienteReceptor.getConexiones(),
                nuevasSolicitudes
        );
        clientesPorNombre.put(receptor, receptorActualizado);

        // agregar en siguiendo del emisor
        List<String> nuevoSiguiendo =
                new ArrayList<>(clienteEmisor.getSiguiendo());
        nuevoSiguiendo.add(receptor);

        Cliente emisorActualizado = new Cliente(
                clienteEmisor.getNombre(),
                clienteEmisor.getScoring(),
                nuevoSiguiendo,
                clienteEmisor.getConexiones(),
                clienteEmisor.getSolicitudesPendientes()
        );
        clientesPorNombre.put(emisor, emisorActualizado);

        return true;
    }

    public boolean rechazarSolicitud(String receptor, String emisor) {

        Cliente clienteReceptor = clientesPorNombre.get(receptor);
        if (clienteReceptor == null) return false;
        if (!clienteReceptor.getSolicitudesPendientes().contains(emisor)) return false;

        List<String> nuevasSolicitudes =
                new ArrayList<>(clienteReceptor.getSolicitudesPendientes());
        nuevasSolicitudes.remove(emisor);

        Cliente actualizado = new Cliente(
                clienteReceptor.getNombre(),
                clienteReceptor.getScoring(),
                clienteReceptor.getSiguiendo(),
                clienteReceptor.getConexiones(),
                nuevasSolicitudes
        );

        clientesPorNombre.put(receptor, actualizado);

        return true;
    }


    /**
     * Construye un ABB con los scorings de toda la red de conexiones alcanzable
     * desde el cliente indicado (recorrido transitivo BFS por "siguiendo")
     * y retorna los scorings que caen en el nivel 4 del árbol.
     *
     * Complejidad: O(v + e) para el recorrido BFS + O(v log v) para las inserciones en el ABB,
     * donde v = clientes alcanzables y e = aristas de "siguiendo".
     */
    @Override
    public List<Integer> consultarConexionesNivel4(String nombre) {

        Cliente cliente = clientesPorNombre.get(nombre);
        if (cliente == null) return new ArrayList<>();

        ABB<Integer> arbol = new ABB<>();
        Set<String> visitados = new HashSet<>();
        visitados.add(nombre);

        Queue<String> cola = new LinkedList<>(cliente.getSiguiendo());

        while (!cola.isEmpty()) {
            String actual = cola.poll();
            if (visitados.contains(actual)) continue;
            visitados.add(actual);

            Cliente c = clientesPorNombre.get(actual);
            if (c != null) {
                arbol.agregar(c.getScoring());
                for (String sig : c.getSiguiendo()) {
                    if (!visitados.contains(sig)) {
                        cola.add(sig);
                    }
                }
            }
        }

        return arbol.obtenerNivel(4);
    }

}
