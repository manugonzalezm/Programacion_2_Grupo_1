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
    void exportarCreaArchivo() throws IOException {
        List<Accion> acciones = new ArrayList<>();
        acciones.add(new Accion("Agregar cliente", "Alice"));
        acciones.add(new Accion("Buscar por nombre", "Bob"));

        Path archivo = tempDir.resolve("test_acciones.csv");
        ExportadorAccionesCsv.exportar(acciones, archivo.toString());

        assertTrue(Files.exists(archivo));
        List<String> lineas = Files.readAllLines(archivo);
        assertEquals(3, lineas.size()); // header + 2 data
        assertEquals("tipo,detalle,fechaHora", lineas.get(0));
        assertTrue(lineas.get(1).startsWith("Agregar cliente,Alice,"));
        assertTrue(lineas.get(2).startsWith("Buscar por nombre,Bob,"));
    }

    @Test
    void exportarVacio() throws IOException {
        Path archivo = tempDir.resolve("vacio.csv");
        ExportadorAccionesCsv.exportar(new ArrayList<>(), archivo.toString());

        List<String> lineas = Files.readAllLines(archivo);
        assertEquals(1, lineas.size()); // solo header
    }

    @Test
    void exportarConComasEnDetalle() throws IOException {
        List<Accion> acciones = new ArrayList<>();
        acciones.add(new Accion("tipo", "detalle, con coma"));

        Path archivo = tempDir.resolve("comas.csv");
        ExportadorAccionesCsv.exportar(acciones, archivo.toString());

        List<String> lineas = Files.readAllLines(archivo);
        assertTrue(lineas.get(1).contains("\"detalle, con coma\""));
    }

    @Test
    void exportarConComillasEnDetalle() throws IOException {
        List<Accion> acciones = new ArrayList<>();
        acciones.add(new Accion("tipo", "detalle \"con\" comillas"));

        Path archivo = tempDir.resolve("comillas.csv");
        ExportadorAccionesCsv.exportar(acciones, archivo.toString());

        List<String> lineas = Files.readAllLines(archivo);
        assertTrue(lineas.get(1).contains("\"detalle \"\"con\"\" comillas\""));
    }

    @Test
    void escaparCsvNull() {
        assertEquals("", ExportadorAccionesCsv.escaparCsv(null));
    }

    @Test
    void escaparCsvSimple() {
        assertEquals("hola", ExportadorAccionesCsv.escaparCsv("hola"));
    }

    @Test
    void escaparCsvConNewline() {
        String resultado = ExportadorAccionesCsv.escaparCsv("linea1\nlinea2");
        assertTrue(resultado.startsWith("\""));
        assertTrue(resultado.endsWith("\""));
    }

    @Test
    void exportarRutaInvalidaLanzaExcepcion() {
        List<Accion> acciones = new ArrayList<>();
        acciones.add(new Accion("t", "d"));
        assertThrows(RuntimeException.class, () ->
            ExportadorAccionesCsv.exportar(acciones, "/ruta/imposible/no/existe/file.csv"));
    }
}
