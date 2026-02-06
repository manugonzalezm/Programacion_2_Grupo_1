package ar.edu.uade.redsocial.implementation;

import ar.edu.uade.redsocial.basic_tdas.implementation.StaticConjuntoTDA;
import ar.edu.uade.redsocial.basic_tdas.implementation.StaticDiccionarioMultipleTDA;
import ar.edu.uade.redsocial.basic_tdas.implementation.StaticDiccionarioSimpleTDA;
import ar.edu.uade.redsocial.basic_tdas.tda.ConjuntoTDA;
import ar.edu.uade.redsocial.basic_tdas.tda.DiccionarioMultipleTDA;
import ar.edu.uade.redsocial.basic_tdas.tda.DiccionarioSimpleTDA;
import ar.edu.uade.redsocial.tda.ClientesTDA;

import java.util.HashMap;
import java.util.Map;

public class StaticClientesTDA implements ClientesTDA {

    private DiccionarioSimpleTDA scoring;
    private DiccionarioMultipleTDA siguiendo;
    private DiccionarioMultipleTDA conexiones;
    private Map<Integer, String> nombres;
    private Map<String, Integer> nombreToId;
    private int nextId;

    public void InicializarClientes() {
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

    public void AgregarCliente(String nombre, int score) {
        if (nombreToId.containsKey(nombre)) {
            throw new IllegalArgumentException("Cliente ya existe: " + nombre);
        }
        int id = nextId++;
        nombreToId.put(nombre, id);
        nombres.put(id, nombre);
        scoring.Agregar(id, score);
    }

    private int idDe(String nombre) {
        Integer id = nombreToId.get(nombre);
        if (id == null) {
            throw new IllegalArgumentException("Cliente no encontrado: " + nombre);
        }
        return id;
    }

    public void Seguir(String nombreCliente, String nombreSeguido) {
        siguiendo.Agregar(idDe(nombreCliente), idDe(nombreSeguido));
    }

    public void Conectar(String nombreCliente, String nombreConexion) {
        conexiones.Agregar(idDe(nombreCliente), idDe(nombreConexion));
    }

    public int ObtenerScoring(String nombre) {
        return scoring.Recuperar(idDe(nombre));
    }

    public String ObtenerNombre(int id) {
        return nombres.get(id);
    }

    public ConjuntoTDA ObtenerSeguidos(String nombre) {
        return siguiendo.Recuperar(idDe(nombre));
    }

    public ConjuntoTDA ObtenerConexiones(String nombre) {
        return conexiones.Recuperar(idDe(nombre));
    }

    public ConjuntoTDA ObtenerClientesPorScoring(int scoringBuscado) {
        ConjuntoTDA resultado = new StaticConjuntoTDA();
        resultado.InicializarConjunto();
        ConjuntoTDA todos = scoring.Claves();
        while (!todos.ConjuntoVacio()) {
            int id = todos.Elegir();
            todos.Sacar(id);
            if (scoring.Recuperar(id) == scoringBuscado) {
                resultado.Agregar(id);
            }
        }
        return resultado;
    }

    public ConjuntoTDA Clientes() {
        return scoring.Claves();
    }
}
