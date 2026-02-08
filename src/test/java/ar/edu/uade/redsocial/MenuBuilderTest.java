package ar.edu.uade.redsocial;

import ar.edu.uade.redsocial.utils.Menu;
import ar.edu.uade.redsocial.utils.MenuBuilder;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class MenuBuilderTest {

    @Test
    void buildDevuelveMenu() {
        Menu m = new MenuBuilder("Titulo").build();
        assertNotNull(m);
    }

    @Test
    void agregarOpcionRunnableYBuild() {
        Menu m = new MenuBuilder("Menu")
            .agregarOpcion("1", "Primera", () -> {})
            .setMensajeSalida("Adios")
            .build();
        assertNotNull(m);
    }

    @Test
    void agregarOpcionConsumerYBuild() {
        Menu m = new MenuBuilder("Menu")
            .agregarOpcion("1", "Primera", scanner -> {})
            .build();
        assertNotNull(m);
    }

    @Test
    void constructorSinTitulo() {
        Menu m = new MenuBuilder().build();
        assertNotNull(m);
    }

    @Test
    void setOpcionSalida() {
        Menu m = new MenuBuilder("Menu")
            .agregarOpcion("1", "Op", () -> {})
            .setOpcionSalida("X")
            .build();
        // Sale con X
        String input = "X\n";
        Scanner sc = new Scanner(new ByteArrayInputStream(input.getBytes()));
        m.ejecutar(sc);
        sc.close();
    }

    @Test
    void setLimpiarConsola() {
        Menu m = new MenuBuilder("Menu")
            .setLimpiarConsola(false)
            .build();
        assertNotNull(m);
    }

    @Test
    void setMensajeSalida() {
        Menu m = new MenuBuilder("Menu")
            .setMensajeSalida("Chau!")
            .build();
        assertNotNull(m);
    }

    @Test
    void builderFluidoCompleto() {
        Menu m = new MenuBuilder("Full")
            .agregarOpcion("1", "Runnable", () -> {})
            .agregarOpcion("2", "Consumer", scanner -> {})
            .setMensajeSalida("Bye")
            .setOpcionSalida("0")
            .setLimpiarConsola(false)
            .build();

        String input = "1\n\n2\n\n0\n";
        Scanner sc = new Scanner(new ByteArrayInputStream(input.getBytes()));
        m.ejecutar(sc);
        sc.close();
    }
}
