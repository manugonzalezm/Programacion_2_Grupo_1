package ar.edu.uade.redsocial.implementation;

import ar.edu.uade.redsocial.EstructuraABB.ABB;
import ar.edu.uade.redsocial.basic_tdas.implementation.GrafoLA;
import ar.edu.uade.redsocial.basic_tdas.tda.ConjuntoTDA;
import ar.edu.uade.redsocial.model.Cliente;
import ar.edu.uade.redsocial.model.SolicitudSeguimiento;
import ar.edu.uade.redsocial.tda.ClientesTDA;

import java.util.*;

/**
 * Implementación de ClientesTDA que usa cuatro estructuras para
 * no duplicar datos y mantener las búsquedas eficientes:
 *
 *  - HashMap por nombre  → búsqueda directa O(1)
 *  - TreeMap por scoring → búsqueda ordenada O(log n)
 *  - GrafoLA dirigido    → seguimiento (A sigue a B sin aprobación)
 *  - GrafoLA no dirigido → amistades (requieren solicitud y aceptación)
 *
 * Las solicitudes de amistad se almacenan directamente en el objeto Cliente
 * receptor (List<SolicitudSeguimiento>), no en una cola global. Esto permite:
 *  - Acceso por índice en O(1)
 *  - Operaciones acotadas al volumen del usuario, no del sistema
 *
 * Los grafos trabajan con IDs enteros, así que se mantiene un mapeo
 * interno entre nombres y números para poder traducir entre ambos.
 */
public class StaticClientesTDA implements ClientesTDA {

    private static final int MAX_SEGUIDOS = 2;

    // datos de clientes
    private final Map<String, Cliente>        clientesPorNombre  = new HashMap<>();
    private final Map<Integer, List<String>>  clientesPorScoring = new TreeMap<>();

    // mapeo nombre <-> id para los grafos
    private final Map<String, Integer> nombreAId = new HashMap<>();
    private final Map<Integer, String> idANombre = new HashMap<>();
    private int nextId = 1;

    // grafos de relaciones
    private final GrafoLA grafoDirigido;    // seguimiento
    private final GrafoLA grafoNoDirigido;  // amistades

    public StaticClientesTDA() {
        grafoDirigido = new GrafoLA();
        grafoDirigido.InicializarGrafo();
        grafoNoDirigido = new GrafoLA();
        grafoNoDirigido.InicializarGrafo();
    }

    private int idDe(String nombre) {
        Integer id = nombreAId.get(nombre);
        return (id != null) ? id : -1;
    }

    private Set<String> idsANombres(ConjuntoTDA<Integer> ids) {
        Set<String> result = new HashSet<>();
        while (!ids.ConjuntoVacio()) {
            int id = ids.Elegir();
            ids.Sacar(id);
            String nombre = idANombre.get(id);
            if (nombre != null) result.add(nombre);
        }
        return result;
    }

    @Override
    public boolean agregarCliente(Cliente cliente) { // O(1)
        if (clientesPorNombre.containsKey(cliente.getNombre())) return false;

        int id = nextId++;
        clientesPorNombre.put(cliente.getNombre(), cliente);
        clientesPorScoring
                .computeIfAbsent(cliente.getScoring(), k -> new ArrayList<>())
                .add(cliente.getNombre());
        nombreAId.put(cliente.getNombre(), id);
        idANombre.put(id, cliente.getNombre());
        grafoDirigido.AgregarVertice(id);
        grafoNoDirigido.AgregarVertice(id);
        return true;
    }

    @Override
    public Cliente buscarPorNombre(String nombre) { // O(1)
        return clientesPorNombre.get(nombre);
    }

    @Override
    public List<Cliente> buscarPorScoring(int scoring) { // O(log n)
        List<String> nombres = clientesPorScoring.getOrDefault(scoring, new ArrayList<>());
        List<Cliente> resultado = new ArrayList<>();
        for (String nombre : nombres) {
            Cliente c = clientesPorNombre.get(nombre);
            if (c != null) resultado.add(c);
        }
        return resultado;
    }

    @Override
    public int cantidadClientes() { // O(1)
        return clientesPorNombre.size();
    }

    @Override
    public List<Cliente> listarClientes() { // O(n)
        return new ArrayList<>(clientesPorNombre.values());
    }

    @Override
    public boolean modificarCliente(Cliente cliente) { // O(log n)
        Cliente existente = clientesPorNombre.get(cliente.getNombre());
        if (existente == null) return false;

        quitarDeScoring(existente);
        clientesPorNombre.put(cliente.getNombre(), cliente);
        clientesPorScoring
                .computeIfAbsent(cliente.getScoring(), k -> new ArrayList<>())
                .add(cliente.getNombre());
        return true;
    }

    @Override
    public boolean eliminarCliente(String nombre) { // O(V + E)
        Cliente cliente = clientesPorNombre.get(nombre);
        if (cliente == null) return false;

        int id = idDe(nombre);
        quitarDeScoring(cliente);
        clientesPorNombre.remove(nombre);
        nombreAId.remove(nombre);
        idANombre.remove(id);

        grafoDirigido.EliminarVertice(id);
        grafoNoDirigido.EliminarVertice(id);

        // limpiar solicitudes pendientes donde el cliente eliminado sea el emisor
        for (Cliente c : clientesPorNombre.values()) {
            c.eliminarSolicitudRecibida(new SolicitudSeguimiento(nombre, c.getNombre()));
        }
        return true;
    }

    private void quitarDeScoring(Cliente cliente) {
        List<String> lista = clientesPorScoring.get(cliente.getScoring());
        if (lista != null) {
            lista.removeIf(n -> n.equals(cliente.getNombre()));
            if (lista.isEmpty()) clientesPorScoring.remove(cliente.getScoring());
        }
    }

    // --- seguimiento ---

    @Override
    public boolean agregarSeguido(String nombreCliente, String nombreSeguido) { // O(grado)
        if (!clientesPorNombre.containsKey(nombreCliente)) return false;
        if (!clientesPorNombre.containsKey(nombreSeguido)) return false;
        if (nombreCliente.equals(nombreSeguido)) return false;

        int idCliente  = idDe(nombreCliente);
        int idSeguido  = idDe(nombreSeguido);
        if (grafoDirigido.GradoSalida(idCliente) >= MAX_SEGUIDOS) return false;
        if (grafoDirigido.ExisteArista(idCliente, idSeguido)) return false;

        grafoDirigido.AgregarArista(idCliente, idSeguido, 1);
        return true;
    }

    @Override
    public boolean quitarSeguido(String nombreCliente, String nombreSeguido) { // O(grado)
        if (!clientesPorNombre.containsKey(nombreCliente)) return false;
        int idCliente = idDe(nombreCliente);
        int idSeguido = idDe(nombreSeguido);
        if (idSeguido == -1 || !grafoDirigido.ExisteArista(idCliente, idSeguido)) return false;

        grafoDirigido.EliminarArista(idCliente, idSeguido);
        return true;
    }

    @Override
    public Set<String> obtenerSeguidos(String nombreCliente) { // O(grado)
        int id = idDe(nombreCliente);
        if (id == -1) return Collections.emptySet();
        return idsANombres(grafoDirigido.ObtenerAdyacentes(id));
    }

    @Override
    public int calcularDistanciaSeguimiento(String origen, String destino) { // O(Vert + Arist)
        int idO = idDe(origen);
        int idD = idDe(destino);
        if (idO == -1 || idD == -1) return -1;
        return bfs(grafoDirigido, idO, idD);
    }

    // --- amistades ---

    @Override
    public void agregarAmistad(String a, String b) { // O(grado)
        if (!clientesPorNombre.containsKey(a) || !clientesPorNombre.containsKey(b)) return;
        int idA = idDe(a);
        int idB = idDe(b);
        grafoNoDirigido.AgregarArista(idA, idB, 1);
        grafoNoDirigido.AgregarArista(idB, idA, 1);
    }

    @Override
    public void eliminarAmistad(String a, String b) { // O(grado)
        int idA = idDe(a);
        int idB = idDe(b);
        if (idA == -1 || idB == -1) return;
        grafoNoDirigido.EliminarArista(idA, idB);
        grafoNoDirigido.EliminarArista(idB, idA);
    }

    @Override
    public Set<String> obtenerVecinos(String nombreCliente) { // O(grado)
        int id = idDe(nombreCliente);
        if (id == -1) return Collections.emptySet();
        return idsANombres(grafoNoDirigido.ObtenerAdyacentes(id));
    }

    @Override
    public int calcularDistanciaAmistad(String origen, String destino) { // O(Vert + Arist)
        int idO = idDe(origen);
        int idD = idDe(destino);
        if (idO == -1 || idD == -1) return -1;
        return bfs(grafoNoDirigido, idO, idD);
    }

    // --- solicitudes de amistad (por usuario) ---

    @Override
    public boolean enviarSolicitudAmistad(String emisor, String receptor) { // O(n_solicitudes_receptor)
        Cliente clienteReceptor = clientesPorNombre.get(receptor);
        if (clienteReceptor == null) return false;
        if (!clientesPorNombre.containsKey(emisor)) return false;
        if (emisor.equals(receptor)) return false;

        for (SolicitudSeguimiento s : clienteReceptor.getSolicitudesRecibidas()) {
            if (s.getOrigen().equals(emisor)) return false; // ya existe
        }

        clienteReceptor.agregarSolicitudRecibida(new SolicitudSeguimiento(emisor, receptor));
        return true;
    }

    @Override
    public List<SolicitudSeguimiento> listarSolicitudesRecibidas(String nombre) { // O(1)
        Cliente c = clientesPorNombre.get(nombre);
        if (c == null) return new ArrayList<>();
        return new ArrayList<>(c.getSolicitudesRecibidas());
    }

    @Override
    public boolean aceptarSolicitudAmistad(String receptor, int indice) { // O(grado)
        Cliente clienteReceptor = clientesPorNombre.get(receptor);
        if (clienteReceptor == null) return false;

        SolicitudSeguimiento s = clienteReceptor.eliminarSolicitudRecibidaPorIndice(indice);
        if (s == null) return false;

        int idReceptor = idDe(receptor);
        int idEmisor   = idDe(s.getOrigen());
        if (idEmisor == -1) return false;

        grafoNoDirigido.AgregarArista(idReceptor, idEmisor, 1);
        grafoNoDirigido.AgregarArista(idEmisor, idReceptor, 1);
        return true;
    }

    @Override
    public boolean rechazarSolicitudAmistad(String receptor, int indice) { // O(1)
        Cliente clienteReceptor = clientesPorNombre.get(receptor);
        if (clienteReceptor == null) return false;
        return clienteReceptor.eliminarSolicitudRecibidaPorIndice(indice) != null;
    }

    @Override
    public boolean revocarSolicitudAmistad(String emisor, String receptor) { // O(n_solicitudes_receptor)
        Cliente clienteReceptor = clientesPorNombre.get(receptor);
        if (clienteReceptor == null) return false;
        return clienteReceptor.eliminarSolicitudRecibida(new SolicitudSeguimiento(emisor, receptor));
    }

    // --- ABB de conexiones ---

    @Override
    public List<Integer> consultarConexionesNivel4(String nombre) { // O(Vert + Arist + Vert·log Vert)
        if (!clientesPorNombre.containsKey(nombre)) return new ArrayList<>();

        ABB<Integer> arbol = new ABB<>();
        Set<Integer> visitados = new HashSet<>();
        int idInicio = idDe(nombre);
        visitados.add(idInicio);

        Queue<Integer> cola = new LinkedList<>();
        ConjuntoTDA<Integer> vecinosInicio = grafoDirigido.ObtenerAdyacentes(idInicio);
        while (!vecinosInicio.ConjuntoVacio()) {   //O(grado(idInicio))
            int v = vecinosInicio.Elegir();
            vecinosInicio.Sacar(v);          //DEPENTE implementacion: si es Hadhset: O(1), si es arreglo: O(n)
            cola.add(v);
        }

        while (!cola.isEmpty()) {             //El bucle principal de BFS
            int idActual = cola.poll();
            if (visitados.contains(idActual)) continue;
            visitados.add(idActual);

            String nombreActual = idANombre.get(idActual);
            Cliente c = (nombreActual != null) ? clientesPorNombre.get(nombreActual) : null;
            if (c != null) {
                arbol.agregar(c.getScoring());// Insertar en ABB → O(log V), (peor caso O(V) si desbalanceado)
                ConjuntoTDA<Integer> vecinos = grafoDirigido.ObtenerAdyacentes(idActual);
                while (!vecinos.ConjuntoVacio()) {     // Recorre todos los vecinos → suma total O(E)
                    int v = vecinos.Elegir();
                    vecinos.Sacar(v);
                    if (!visitados.contains(v)) cola.add(v);
                }
            }
        }
        return arbol.obtenerNivel(4);         // recorrer árbol hasta nivel 4 → O(V)
    }

    // BFS sobre cualquier GrafoLA, devuelve la distancia en saltos o -1 si no hay camino
    private int bfs(GrafoLA grafo, int idOrigen, int idDestino) { // O(Vert + Arist)
        if (idOrigen == idDestino) return 0;
        Map<Integer, Integer> distancias = new HashMap<>();
        Queue<Integer> cola = new LinkedList<>();
        distancias.put(idOrigen, 0);
        cola.add(idOrigen);

        while (!cola.isEmpty()) {
            int actual = cola.poll();
            int dist   = distancias.get(actual);

            ConjuntoTDA<Integer> vecinos = grafo.ObtenerAdyacentes(actual);
            while (!vecinos.ConjuntoVacio()) {
                int v = vecinos.Elegir();
                vecinos.Sacar(v);
                if (!distancias.containsKey(v)) {
                    int nueva = dist + 1;
                    distancias.put(v, nueva);
                    if (v == idDestino) return nueva;
                    cola.add(v);
                }
            }
        }
        return -1;
    }
}
