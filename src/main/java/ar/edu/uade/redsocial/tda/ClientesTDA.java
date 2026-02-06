package ar.edu.uade.redsocial.tda;

import ar.edu.uade.redsocial.basic_tdas.tda.ConjuntoTDA;

public interface ClientesTDA {

    void InicializarClientes();

    /**
     * Agrega un cliente (nombre, scoring). Siguiendo y conexiones se gestionan con Seguir/Conectar.
     */
    void AgregarCliente(String nombre, int scoring);

    void Seguir(String nombreCliente, String nombreSeguido);

    void Conectar(String nombreCliente, String nombreConexion);

    /** Scoring del cliente identificado por nombre. */
    int ObtenerScoring(String nombre);

    /** Seguidos del cliente (devuelve IDs; usar ObtenerNombre(id) para el nombre). */
    ConjuntoTDA ObtenerSeguidos(String nombre);

    /** Conexiones del cliente (devuelve IDs; usar ObtenerNombre(id) para el nombre). */
    ConjuntoTDA ObtenerConexiones(String nombre);

    /** Todos los clientes con el scoring dado (devuelve IDs; usar ObtenerNombre(id) para el nombre). */
    ConjuntoTDA ObtenerClientesPorScoring(int scoring);

    /** Nombre del cliente a partir de su ID interno (útil al iterar ConjuntoTDA devueltos). */
    String ObtenerNombre(int id);

    /** Conjunto de todos los IDs de clientes. */
    ConjuntoTDA Clientes();
}
