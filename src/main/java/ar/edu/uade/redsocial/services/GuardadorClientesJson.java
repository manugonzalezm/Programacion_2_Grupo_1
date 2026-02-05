package ar.edu.uade.redsocial.services;

import ar.edu.uade.redsocial.model.Cliente;
import com.google.gson.Gson;

import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GuardadorClientesJson {

    private static class ClienteJson {
        String nombre;
        int scoring;
        List<String> siguiendo = new ArrayList<>();
        List<String> conexiones = new ArrayList<>();
    }

    private static class ClientesJson {
        List<ClienteJson> clientes;
    }

    public static void guardar(GestorClientes gestor) {
        try {
            URL url = CargadorClientesJson.class.getClassLoader().getResource("clientes.json");
            if (url == null) {
                return;
            }
            URI uri = url.toURI();
            if (!"file".equals(uri.getScheme())) {
                return;
            }
            Path path = Paths.get(uri);

            List<ClienteJson> lista = new ArrayList<>();
            for (Cliente c : gestor.listarClientes()) {
                ClienteJson cj = new ClienteJson();
                cj.nombre = c.getNombre();
                cj.scoring = c.getScoring();
                lista.add(cj);
            }
            ClientesJson datos = new ClientesJson();
            datos.clientes = lista;

            Gson gson = new Gson();
            String json = gson.toJson(datos);

            Files.writeString(path, json);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo guardar clientes.json: " + e.getMessage());
        }
    }
}
