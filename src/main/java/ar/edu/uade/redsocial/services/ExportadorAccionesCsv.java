package ar.edu.uade.redsocial.services;

import ar.edu.uade.redsocial.model.Accion;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Exporta el historial de acciones a un archivo CSV.
 * Formato: tipo,detalle,fechaHora
 */
public class ExportadorAccionesCsv {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /** Guarda las acciones en el archivo CSV indicado. */
    public static void exportar(List<Accion> acciones, String rutaArchivo) { // complejidad O(n)
        List<String> lineas = new ArrayList<>();
        lineas.add("tipo,detalle,fechaHora");
        for (Accion a : acciones) {
            String tipo = escaparCsv(a.getTipo());
            String detalle = escaparCsv(a.getDetalle());
            String fecha = a.getFechaHora().format(FORMATO);
            lineas.add(tipo + "," + detalle + "," + fecha);
        }
        try {
            Path path = Paths.get(rutaArchivo);
            Files.write(path, lineas);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo exportar acciones a CSV: " + e.getMessage());
        }
    }

    /** Escapa comillas y comas para formato CSV. */
    static String escaparCsv(String valor) { // complejidad O(n)
        if (valor == null) return "";
        if (valor.contains(",") || valor.contains("\"") || valor.contains("\n")) {
            return "\"" + valor.replace("\"", "\"\"") + "\"";
        }
        return valor;
    }
}
