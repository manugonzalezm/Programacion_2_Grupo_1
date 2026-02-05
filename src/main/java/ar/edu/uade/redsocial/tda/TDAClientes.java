package ar.edu.uade.redsocial.tda;

import ar.edu.uade.redsocial.model.Cliente;
import java.util.List;

// TDA para la gestión de clientes
public interface TDAClientes {

    boolean agregarCliente(Cliente cliente);

    Cliente buscarPorNombre(String nombre);

    List<Cliente> buscarPorScoring(int scoring);

    int cantidadClientes();

    List<Cliente> listarClientes();
}
