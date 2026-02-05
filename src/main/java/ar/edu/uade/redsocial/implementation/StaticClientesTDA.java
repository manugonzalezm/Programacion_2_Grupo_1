package ar.edu.uade.redsocial.implementation;

import ar.edu.uade.redsocial.basic_tdas.implementation.StaticDiccionarioMultipleTDA;
import ar.edu.uade.redsocial.basic_tdas.implementation.StaticDiccionarioSimpleTDA;
import ar.edu.uade.redsocial.basic_tdas.tda.ConjuntoTDA;
import ar.edu.uade.redsocial.basic_tdas.tda.DiccionarioMultipleTDA;
import ar.edu.uade.redsocial.basic_tdas.tda.DiccionarioSimpleTDA;
import ar.edu.uade.redsocial.tda.ClientesTDA;

public class StaticClientesTDA implements ClientesTDA {

    private DiccionarioSimpleTDA scoring;
    private DiccionarioMultipleTDA siguiendo;
    private DiccionarioMultipleTDA conexiones;

    public void InicializarClientes() {
        scoring = new StaticDiccionarioSimpleTDA();
        scoring.InicializarDiccionario();

        siguiendo = new StaticDiccionarioMultipleTDA();
        siguiendo.InicializarDiccionario();

        conexiones = new StaticDiccionarioMultipleTDA();
        conexiones.InicializarDiccionario();
    }

    public void AgregarCliente(int clienteId, int score) {
        scoring.Agregar(clienteId, score);
    }

    public void Seguir(int clienteId, int seguidoId) {
        siguiendo.Agregar(clienteId, seguidoId);
    }

    public void Conectar(int clienteId, int conexionId) {
        conexiones.Agregar(clienteId, conexionId);
    }

    public int ObtenerScoring(int clienteId) {
        return scoring.Recuperar(clienteId);
    }

    public ConjuntoTDA ObtenerSeguidos(int clienteId) {
        return siguiendo.Recuperar(clienteId);
    }

    public ConjuntoTDA ObtenerConexiones(int clienteId) {
        return conexiones.Recuperar(clienteId);
    }

    public ConjuntoTDA Clientes() {
        return scoring.Claves();
    }
}

