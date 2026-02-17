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
 * - TreeMap scoring -> List(String nombre): índice por scoring O(log n).
 */
public class StaticClientesTDA implements ClientesTDA {

    private final Map<String, Cliente> clientesPorNombre = new HashMap<>();
    private final Map<Integer, List<String>> clientesPorScoring = new TreeMap<>();

    @Override
    public boolean agregarCliente(Cliente cliente) {

        if (clientesPorNombre.containsKey(cliente.getNombre())) {
            return false;
        }

        Cliente clienteValido = new Cliente(
                cliente.getNombre(),
                cliente.getScoring(),
                cliente.getSiguiendo(),
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

        //Ahora solo envía solicitud
        return enviarSolicitud(nombreCliente, nombreSeguido);
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

        for (Cliente c : clientesPorNombre.values()) {
            c.getSiguiendo().remove(nombre);
            c.getConexiones().remove(nombre);
            c.getSolicitudesPendientes().remove(nombre);
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
}
