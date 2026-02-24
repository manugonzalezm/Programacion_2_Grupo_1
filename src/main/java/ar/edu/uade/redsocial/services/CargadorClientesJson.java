package ar.edu.uade.redsocial.services;

import ar.edu.uade.redsocial.model.Cliente;
import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CargadorClientesJson {

    private static class ClienteJson {
        String nombre;
        int scoring;
        List<String> siguiendo  = new ArrayList<>();
        List<String> conexiones = new ArrayList<>();
    }

    private static class ClientesJson {
        List<ClienteJson> clientes;
    }

    /**
     * Lee el archivo clientes.json y carga los datos en el gestor.
     * Primero agrega todos los clientes y después sus relaciones,
     * para asegurarse de que ambos extremos existan antes de vincularlos.
     */
    public static void readFromFile(GestorClientes gestor) { // O(n + e)

        InputStream is = CargadorClientesJson.class
                .getClassLoader()
                .getResourceAsStream("clientes.json");

        if (is == null) throw new RuntimeException("No se encontró el archivo clientes.json");

        Gson gson = new Gson();
        ClientesJson datos = gson.fromJson(new InputStreamReader(is), ClientesJson.class);

        // primera pasada: cargar todos los clientes
        for (ClienteJson cj : datos.clientes) {
            gestor.agregarCliente(new Cliente(cj.nombre, cj.scoring));
        }

        // segunda pasada: cargar las relaciones entre ellos
        for (ClienteJson cj : datos.clientes) {
            for (String sig : cj.siguiendo) {
                gestor.agregarSeguido(cj.nombre, sig);
            }
            for (String con : cj.conexiones) {
                gestor.agregarAmistad(cj.nombre, con);
            }
        }
    }
}
