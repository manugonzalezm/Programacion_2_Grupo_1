package ar.edu.uade.redsocial.services;

import ar.edu.uade.redsocial.model.Cliente;
import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class CargadorClientesJson {

    // Clases auxiliares SOLO para mapear el JSON
    private static class ClienteJson {
        String nombre;
        int scoring;
    }

    private static class ClientesJson {
        List<ClienteJson> clientes;
    }

    public static void readFromFile(GestorClientes gestor) {

        InputStream is = CargadorClientesJson.class
                .getClassLoader()
                .getResourceAsStream("clientes.json");

        if (is == null) {
            throw new RuntimeException("No se encontró el archivo clientes.json");
        }

        Gson gson = new Gson();
        ClientesJson datos =
                gson.fromJson(new InputStreamReader(is), ClientesJson.class);

        for (ClienteJson cj : datos.clientes) {
            gestor.agregarCliente(new Cliente(cj.nombre, cj.scoring));
        }
    }
}

