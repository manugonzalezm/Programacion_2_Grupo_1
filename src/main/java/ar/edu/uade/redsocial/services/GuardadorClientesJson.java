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
        List<String> siguiendo  = new ArrayList<>();
        List<String> conexiones = new ArrayList<>();
    }

    private static class ClientesJson {
        List<ClienteJson> clientes;
    }

    /**
     * Serializa el estado actual del gestor a clientes.json.
     * Las relaciones se obtienen de los grafos (via gestor), no del objeto Cliente,
     * evitando duplicación de datos.
     */
    public static void guardar(GestorClientes gestor) { // O(n + e)
        try {
            URL url = CargadorClientesJson.class.getClassLoader().getResource("clientes.json");
            if (url == null) return;

            URI uri = url.toURI();
            if (!"file".equals(uri.getScheme())) return;

            Path path = Paths.get(uri);
            String pathStr = path.toString();
            if (pathStr.contains("target" + java.io.File.separator + "classes")) {
                path = Paths.get(pathStr.replace(
                        "target" + java.io.File.separator + "classes",
                        "src" + java.io.File.separator + "main" + java.io.File.separator + "resources"));
            }

            List<ClienteJson> lista = new ArrayList<>();
            for (Cliente c : gestor.listarClientes()) {
                ClienteJson cj = new ClienteJson();
                cj.nombre   = c.getNombre();
                cj.scoring  = c.getScoring();
                cj.siguiendo  = new ArrayList<>(gestor.obtenerSeguidos(c.getNombre()));
                cj.conexiones = new ArrayList<>(gestor.obtenerVecinos(c.getNombre()));
                lista.add(cj);
            }

            ClientesJson datos = new ClientesJson();
            datos.clientes = lista;

            Files.writeString(path, new Gson().toJson(datos));
        } catch (Exception e) {
            throw new RuntimeException("No se pudo guardar clientes.json: " + e.getMessage());
        }
    }
}
