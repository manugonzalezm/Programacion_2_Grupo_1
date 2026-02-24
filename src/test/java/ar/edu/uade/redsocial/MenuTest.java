package ar.edu.uade.redsocial;

import ar.edu.uade.redsocial.utils.Menu;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class MenuTest {

    @Test
    void ejecutarSaleConCero() {
        Scanner sc = new Scanner(new ByteArrayInputStream("0\n".getBytes()));
        Menu m = new Menu();
        m.agregarOpcion("1", "Algo", () -> {});
        m.ejecutar(sc);
        sc.close();
    }

    @Test
    void ejecutarOpcionValida() {
        boolean[] ok = {false};
        Scanner sc = new Scanner(new ByteArrayInputStream("1\n\n0\n".getBytes()));
        Menu m = new Menu();
        m.agregarOpcion("1", "Op", () -> ok[0] = true);
        m.ejecutar(sc);
        assertTrue(ok[0]);
        sc.close();
    }

    @Test
    void ejecutarOpcionInvalida() {
        Scanner sc = new Scanner(new ByteArrayInputStream("99\n\n0\n".getBytes()));
        Menu m = new Menu();
        m.agregarOpcion("1", "Op", () -> {});
        m.ejecutar(sc); // no debe lanzar excepción
        sc.close();
    }

    @Test
    void ejecutarMultiplesOpciones() {
        int[] contador = {0};
        Scanner sc = new Scanner(new ByteArrayInputStream("1\n\n2\n\n0\n".getBytes()));
        Menu m = new Menu("Menu Multi");
        m.agregarOpcion("1", "Op1", () -> contador[0]++);
        m.agregarOpcion("2", "Op2", () -> contador[0]++);
        m.ejecutar(sc);
        assertEquals(2, contador[0]);
        sc.close();
    }
}
