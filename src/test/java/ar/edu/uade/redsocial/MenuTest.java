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
    void agregarOpcionConsumer() {
        Menu m = new Menu("Test");
        boolean[] ejecutado = {false};
        m.agregarOpcion("1", "opcion 1", scanner -> ejecutado[0] = true);
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

    @Test
    void ejecutarOpcionConsumerYLuegoSale() {
        String input = "1\n\n0\n";
        Scanner sc = new Scanner(new ByteArrayInputStream(input.getBytes()));
        boolean[] ok = {false};
        Menu m = new Menu();
        m.agregarOpcion("1", "Op", scanner -> ok[0] = true);
        m.ejecutar(sc);
        assertTrue(ok[0]);
        sc.close();
    }

    @Test
    void ejecutarOpcionInvalidaYLuegoSale() {
        String input = "99\n\n0\n";
        Scanner sc = new Scanner(new ByteArrayInputStream(input.getBytes()));
        Menu m = new Menu();
        m.agregarOpcion("1", "Op", () -> {});
        m.ejecutar(sc);
        sc.close();
    }

    @Test
    void ejecutarMultiplesOpcionesYLuegoSale() {
        String input = "1\n\n2\n\n0\n";
        Scanner sc = new Scanner(new ByteArrayInputStream(input.getBytes()));
        int[] contador = {0};
        Menu m = new Menu("Menu Multi");
        m.agregarOpcion("1", "Op1", () -> contador[0]++);
        m.agregarOpcion("2", "Op2", () -> contador[0]++);
        m.ejecutar(sc);
        assertEquals(2, contador[0]);
        sc.close();
    }

    @Test
    void ejecutarOpcionConExcepcionNoRompeMenu() {
        String input = "1\n\n0\n";
        Scanner sc = new Scanner(new ByteArrayInputStream(input.getBytes()));
        Menu m = new Menu();
        m.agregarOpcion("1", "Rompe", () -> { throw new RuntimeException("Error"); });
        m.ejecutar(sc);
        sc.close();
    }

    @Test
    void setTitulo() {
        Menu m = new Menu();
        Menu retorno = m.setTitulo("Nuevo titulo");
        assertSame(m, retorno); // verifica fluent API
    }

    @Test
    void setMensajeSalida() {
        Menu m = new Menu();
        Menu retorno = m.setMensajeSalida("Bye");
        assertSame(m, retorno);
    }

    @Test
    void setOpcionSalida() {
        Menu m = new Menu();
        Menu retorno = m.setOpcionSalida("X");
        assertSame(m, retorno);
    }

    @Test
    void setLimpiarConsola() {
        Menu m = new Menu();
        Menu retorno = m.setLimpiarConsola(true);
        assertSame(m, retorno);
    }

    @Test
    void ejecutarConOpcionSalidaPersonalizada() {
        String input = "X\n";
        Scanner sc = new Scanner(new ByteArrayInputStream(input.getBytes()));
        Menu m = new Menu("Custom");
        m.setOpcionSalida("X");
        m.agregarOpcion("1", "Op", () -> {});
        m.ejecutar(sc);
        sc.close();
    }

    @Test
    void ejecutarConLimpiarConsola() {
        String input = "0\n";
        Scanner sc = new Scanner(new ByteArrayInputStream(input.getBytes()));
        Menu m = new Menu();
        m.setLimpiarConsola(true);
        m.agregarOpcion("1", "Op", () -> {});
        m.ejecutar(sc);
        sc.close();
    }

    @Test
    void constructorSinArgumentos() {
        Menu m = new Menu();
        assertNotNull(m);
    }

    @Test
    void constructorConTitulo() {
        Menu m = new Menu("Mi titulo");
        assertNotNull(m);
    }
}
