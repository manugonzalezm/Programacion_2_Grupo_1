package ar.edu.uade.redsocial.implementation;

import ar.edu.uade.redsocial.basic_tdas.implementation.StaticDiccionarioMultipleTDA;
import ar.edu.uade.redsocial.basic_tdas.implementation.StaticDiccionarioSimpleTDA;
import ar.edu.uade.redsocial.basic_tdas.tda.ConjuntoTDA;
import ar.edu.uade.redsocial.basic_tdas.tda.DiccionarioMultipleTDA;
import ar.edu.uade.redsocial.basic_tdas.tda.DiccionarioSimpleTDA;
import ar.edu.uade.redsocial.model.Cliente;
import ar.edu.uade.redsocial.tda.ClientesTDA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementación estática de ClientesTDA usando TDAs básicos.
 * - DiccionarioSimple: scoring (id -> scoring) — búsqueda por scoring O(n) claves, óptimo con TDA disponible.
 * - DiccionarioMultiple: siguiendo y conexiones (id -> conjunto de ids) — recuperar O(1) por clave.
 * - Maps nombre<->id: no hay TDA de strings en basic_tdas; se mantienen para resolución de nombres.
 */
public class StaticClientesTDA implements ClientesTDA {

    private DiccionarioSimpleTDA scoring;
    private DiccionarioMultipleTDA siguiendo;
    private DiccionarioMultipleTDA conexiones;
    private Map<Integer, String> nombres;
    private Map<String, Integer> nombreToId;
    private int nextId;

    public StaticClientesTDA() { // complejidad O(1)
        inicializar();
    }

    private void inicializar() { // complejidad O(1)
        scoring = new StaticDiccionarioSimpleTDA();
        scoring.InicializarDiccionario();
        siguiendo = new StaticDiccionarioMultipleTDA();
        siguiendo.InicializarDiccionario();
        conexiones = new StaticDiccionarioMultipleTDA();
        conexiones.InicializarDiccionario();
        nombres = new HashMap<>();
        nombreToId = new HashMap<>();
        nextId = 0;
    }

    @Override
    public boolean agregarCliente(Cliente cliente) { // complejidad O(n), n = siguiendo + conexiones del cliente
        if (nombreToId.containsKey(cliente.getNombre())) {
            return false;
        }
        int id = nextId++;
        nombreToId.put(cliente.getNombre(), id);
        nombres.put(id, cliente.getNombre());
        scoring.Agregar(id, cliente.getScoring());

        for (String seg : cliente.getSiguiendo()) {
            if (nombreToId.containsKey(seg)) {
                siguiendo.Agregar(id, nombreToId.get(seg));
            }
        }
        for (String conn : cliente.getConexiones()) {
            if (nombreToId.containsKey(conn)) {
                conexiones.Agregar(id, nombreToId.get(conn));
            }
        }
        return true;
    }

    @Override
    public Cliente buscarPorNombre(String nombre) { // complejidad O(n), n = tamaño siguiendo + conexiones
        Integer id = nombreToId.get(nombre);
        if (id == null) {
            return null;
        }
        int score = scoring.Recuperar(id);
        List<String> segNames = conjuntoIdANombres(siguiendo.Recuperar(id));
        List<String> connNames = conjuntoIdANombres(conexiones.Recuperar(id));
        return new Cliente(nombre, score, segNames, connNames);
    }

    @Override
    public List<Cliente> buscarPorScoring(int scoringBuscado) { // complejidad O(n*m), n = clientes, m = avg relaciones
        List<Cliente> resultado = new ArrayList<>();
        ConjuntoTDA claves = scoring.Claves();
        while (!claves.ConjuntoVacio()) {
            int id = claves.Elegir();
            claves.Sacar(id);
            if (scoring.Recuperar(id) == scoringBuscado) {
                resultado.add(buscarPorNombre(nombres.get(id)));
            }
        }
        return resultado;
    }

    @Override
    public int cantidadClientes() { // complejidad O(1)
        return nombreToId.size();
    }

    @Override
    public List<Cliente> listarClientes() { // complejidad O(n*m), n = clientes, m = avg relaciones
        List<Cliente> lista = new ArrayList<>();
        ConjuntoTDA ids = scoring.Claves();
        while (!ids.ConjuntoVacio()) {
            int id = ids.Elegir();
            ids.Sacar(id);
            lista.add(buscarPorNombre(nombres.get(id)));
        }
        return lista;
    }

    @Override
    public boolean modificarSeguidor(Cliente cliente) { // complejidad O(n), n = siguiendo + conexiones
        Integer id = nombreToId.get(cliente.getNombre());
        if (id == null) {
            return false;
        }
        scoring.Agregar(id, cliente.getScoring());

        ConjuntoTDA segActual = siguiendo.Recuperar(id);
        while (!segActual.ConjuntoVacio()) {
            int v = segActual.Elegir();
            segActual.Sacar(v);
            siguiendo.EliminarValor(id, v);
        }
        for (String seg : cliente.getSiguiendo()) {
            if (nombreToId.containsKey(seg)) {
                siguiendo.Agregar(id, nombreToId.get(seg));
            }
        }

        ConjuntoTDA connActual = conexiones.Recuperar(id);
        while (!connActual.ConjuntoVacio()) {
            int v = connActual.Elegir();
            connActual.Sacar(v);
            conexiones.EliminarValor(id, v);
        }
        for (String conn : cliente.getConexiones()) {
            if (nombreToId.containsKey(conn)) {
                conexiones.Agregar(id, nombreToId.get(conn));
            }
        }
        return true;
    }

    @Override
    public boolean agregarSeguido(String nombreCliente, String nombreSeguido) { // complejidad O(n), n = relaciones
        Cliente cliente = buscarPorNombre(nombreCliente);
        if (cliente == null) {
            return false;
        }
        if (buscarPorNombre(nombreSeguido) == null) {
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
        Cliente actualizado = new Cliente(cliente.getNombre(), cliente.getScoring(), nuevoSiguiendo, new ArrayList<>(cliente.getConexiones()));
        return modificarSeguidor(actualizado);
    }

    @Override
    public boolean eliminarCliente(String nombre) { // complejidad O(n), n = cantidad de clientes
        Integer id = nombreToId.get(nombre);
        if (id == null) {
            return false;
        }
        ConjuntoTDA clavesSig = siguiendo.Claves();
        while (!clavesSig.ConjuntoVacio()) {
            int clave = clavesSig.Elegir();
            clavesSig.Sacar(clave);
            siguiendo.EliminarValor(clave, id);
        }
        ConjuntoTDA clavesConn = conexiones.Claves();
        while (!clavesConn.ConjuntoVacio()) {
            int clave = clavesConn.Elegir();
            clavesConn.Sacar(clave);
            conexiones.EliminarValor(clave, id);
        }
        scoring.Eliminar(id);
        siguiendo.Eliminar(id);
        conexiones.Eliminar(id);
        nombres.remove(id);
        nombreToId.remove(nombre);
        return true;
    }

    @Override
    public boolean quitarSeguido(String nombreCliente, String nombreSeguido) { // complejidad O(n), n = relaciones
        Cliente cliente = buscarPorNombre(nombreCliente);
        if (cliente == null || !cliente.getSiguiendo().contains(nombreSeguido)) {
            return false;
        }
        List<String> nuevoSiguiendo = new ArrayList<>(cliente.getSiguiendo());
        nuevoSiguiendo.remove(nombreSeguido);
        Cliente actualizado = new Cliente(cliente.getNombre(), cliente.getScoring(), nuevoSiguiendo, new ArrayList<>(cliente.getConexiones()));
        return modificarSeguidor(actualizado);
    }

    /** Convierte un ConjuntoTDA de IDs en lista de nombres (consume el conjunto devuelto por Recuperar). */
    private List<String> conjuntoIdANombres(ConjuntoTDA ids) { // complejidad O(n), n = tamaño del conjunto
        List<String> list = new ArrayList<>();
        while (!ids.ConjuntoVacio()) {
            int id = ids.Elegir();
            ids.Sacar(id);
            list.add(nombres.get(id));
        }
        return list;
    }
}
