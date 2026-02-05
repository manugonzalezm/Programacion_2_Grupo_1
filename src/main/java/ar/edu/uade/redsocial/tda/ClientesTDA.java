package ar.edu.uade.redsocial.tda;

import ar.edu.uade.redsocial.basic_tdas.tda.ConjuntoTDA;

public interface ClientesTDA {

    void InicializarClientes();

    void AgregarCliente(int clienteId, int scoring);

    void Seguir(int clienteId, int seguidoId);

    void Conectar(int clienteId, int conexionId);

    int ObtenerScoring(int clienteId);

    ConjuntoTDA ObtenerSeguidos(int clienteId);

    ConjuntoTDA ObtenerConexiones(int clienteId);

    ConjuntoTDA Clientes();
}

