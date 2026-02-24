package ar.edu.uade.redsocial;

import ar.edu.uade.redsocial.model.Accion;
import ar.edu.uade.redsocial.services.ExportadorAccionesCsv;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExportadorAccionesCsvTest {

    @TempDir
    Path tempDir;

    @Test
    void exportarCreaArchivoConHeader() throws IOException {
        List<Accion> acciones = new ArrayList<>();
        acciones.add(new Accion("Agregar cliente", "Alice"));
        acciones.add(new Accion("Buscar por nombre", "Bob"));

        Path archivo = tempDir.resolve("acciones.csv");
        ExportadorAccionesCsv.exportar(acciones, archivo.toString());

        List<String> lineas = Files.readAllLines(archivo);
        assertEquals(3, lineas.size()); // header + 2 filas
        assertEquals("tipo,detalle,fechaHora", lineas.get(0));
        assertTrue(lineas.get(1).startsWith("Agregar cliente,Alice,"));
    }

    @Test
    void exportarListaVaciaSoloHeader() throws IOException {
        Path archivo = tempDir.resolve("vacio.csv");
        ExportadorAccionesCsv.exportar(new ArrayList<>(), archivo.toString());
        assertEquals(1, Files.readAllLines(archivo).size());
    }

    @Test
    void exportarConComasEnDetalleEscapa() throws IOException {
        List<Accion> acciones = List.of(new Accion("tipo", "detalle, con coma"));
        Path archivo = tempDir.resolve("comas.csv");
        ExportadorAccionesCsv.exportar(acciones, archivo.toString());
        assertTrue(Files.readAllLines(archivo).get(1).contains("\"detalle, con coma\""));
    }
}
