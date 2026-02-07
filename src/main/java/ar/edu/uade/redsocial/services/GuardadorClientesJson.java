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

    public static void guardar(GestorClientes gestor) { // complejidad O(n*m), n = clientes, m = avg relaciones
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
            // Si estamos en target/classes (Maven), guardar en src/main/resources para que se actualice el fuente
            String pathStr = path.toString();
            if (pathStr.contains("target" + java.io.File.separator + "classes")) {
                path = Paths.get(pathStr.replace("target" + java.io.File.separator + "classes", "src" + java.io.File.separator + "main" + java.io.File.separator + "resources"));
            }

            List<ClienteJson> lista = new ArrayList<>();
            for (Cliente c : gestor.listarClientes()) {
                ClienteJson cj = new ClienteJson();
                cj.nombre = c.getNombre();
                cj.scoring = c.getScoring();
                cj.siguiendo = new ArrayList<>(c.getSiguiendo());
                cj.conexiones = new ArrayList<>(c.getConexiones());
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
