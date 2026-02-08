package ar.edu.uade.redsocial.services;

import ar.edu.uade.redsocial.model.Accion;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Exporta el historial de acciones a un archivo CSV.
 * Formato: tipo,detalle,fechaHora
 * Si el archivo ya existe, las nuevas acciones se agregan al final (append).
 */
public class ExportadorAccionesCsv {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /** Agrega las acciones al archivo CSV indicado dentro de src/main/resources.
     *  Si el archivo no existe, lo crea con el header. Si ya existe, hace append. */
    public static void exportar(List<Accion> acciones, String nombreArchivo) { // complejidad O(n)
        List<String> lineas = new ArrayList<>();
        for (Accion a : acciones) {
            String tipo = escaparCsv(a.getTipo());
            String detalle = escaparCsv(a.getDetalle());
            String fecha = a.getFechaHora().format(FORMATO);
            lineas.add(tipo + "," + detalle + "," + fecha);
        }
        try {
            Path path = resolverRutaResources(nombreArchivo);
            boolean archivoNuevo = !Files.exists(path) || Files.size(path) == 0;
            if (archivoNuevo) {
                List<String> conHeader = new ArrayList<>();
                conHeader.add("tipo,detalle,fechaHora");
                conHeader.addAll(lineas);
                Files.write(path, conHeader);
            } else {
                // Agregar un salto de linea antes si el archivo no termina en newline
                Files.write(path, lineas, StandardOpenOption.APPEND);
            }
        } catch (Exception e) {
            throw new RuntimeException("No se pudo exportar acciones a CSV: " + e.getMessage());
        }
    }

    /**
     * Resuelve la ruta del archivo dentro de src/main/resources.
     * Usa el classpath para encontrar la carpeta resources, de forma análoga a GuardadorClientesJson.
     */
    private static Path resolverRutaResources(String nombreArchivo) throws Exception {
        // Usamos clientes.json como referencia para localizar la carpeta resources
        URL url = ExportadorAccionesCsv.class.getClassLoader().getResource("clientes.json");
        if (url == null) {
            throw new RuntimeException("No se pudo localizar la carpeta de resources");
        }
        URI uri = url.toURI();
        if (!"file".equals(uri.getScheme())) {
            throw new RuntimeException("No se pudo localizar la carpeta de resources");
        }
        Path referencePath = Paths.get(uri).getParent();
        // Si estamos en target/classes (Maven), redirigir a src/main/resources
        String pathStr = referencePath.toString();
        if (pathStr.contains("target" + java.io.File.separator + "classes")) {
            referencePath = Paths.get(pathStr.replace(
                "target" + java.io.File.separator + "classes",
                "src" + java.io.File.separator + "main" + java.io.File.separator + "resources"));
        }
        return referencePath.resolve(nombreArchivo);
    }

    /** Escapa comillas y comas para formato CSV. */
    public static String escaparCsv(String valor) { // complejidad O(n)
        if (valor == null) return "";
        if (valor.contains(",") || valor.contains("\"") || valor.contains("\n")) {
            return "\"" + valor.replace("\"", "\"\"") + "\"";
        }
        return valor;
    }
}
