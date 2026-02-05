package ar.edu.uade.redsocial;

import ar.edu.uade.redsocial.utils.Menu;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class MenuTest {

    @Test
    void agregarOpcionRunnable() {
        Menu m = new Menu("Test");
        boolean[] ejecutado = {false};
        m.agregarOpcion("1", "opcion 1", () -> ejecutado[0] = true);
        assertNotNull(m);
    }

    @Test
    void buildConTitulo() {
        Menu m = new Menu("Mi Menu");
        m.agregarOpcion("1", "Una", () -> {});
        m.setMensajeSalida("Chau");
        m.setOpcionSalida("0");
        assertNotNull(m);
    }

    @Test
    void ejecutarSaleConCero() {
        String input = "0\n";
        Scanner sc = new Scanner(new ByteArrayInputStream(input.getBytes()));
        Menu m = new Menu();
        m.agregarOpcion("1", "Algo", () -> {});
        m.ejecutar(sc);
        sc.close();
    }

    @Test
    void ejecutarOpcionValidaYLuegoSale() {
        String input = "1\n\n0\n";
        Scanner sc = new Scanner(new ByteArrayInputStream(input.getBytes()));
        boolean[] ok = {false};
        Menu m = new Menu();
        m.agregarOpcion("1", "Op", () -> ok[0] = true);
        m.ejecutar(sc);
        assertTrue(ok[0]);
        sc.close();
    }
}
