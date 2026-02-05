package ar.edu.uade.redsocial;

import ar.edu.uade.redsocial.utils.InputUtils;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class InputUtilsTest {

    @Test
    void leerTexto() {
        Scanner sc = new Scanner(new ByteArrayInputStream("hola\n".getBytes()));
        String t = InputUtils.leerTexto(sc, "> ");
        assertEquals("hola", t);
        sc.close();
    }

    @Test
    void leerTextoSinMensaje() {
        Scanner sc = new Scanner(new ByteArrayInputStream("abc\n".getBytes()));
        assertEquals("abc", InputUtils.leerTexto(sc));
        sc.close();
    }

    @Test
    void leerEntero() {
        Scanner sc = new Scanner(new ByteArrayInputStream("42\n".getBytes()));
        int n = InputUtils.leerEntero(sc, "");
        assertEquals(42, n);
        sc.close();
    }

    @Test
    void leerEnteroLanzaSiNoEsNumero() {
        Scanner sc = new Scanner(new ByteArrayInputStream("abc\n".getBytes()));
        assertThrows(NumberFormatException.class, () -> InputUtils.leerEntero(sc, ""));
        sc.close();
    }

    @Test
    void confirmarConS() {
        Scanner sc = new Scanner(new ByteArrayInputStream("S\n".getBytes()));
        assertTrue(InputUtils.confirmar(sc, ""));
        sc.close();
    }

    @Test
    void confirmarConN() {
        Scanner sc = new Scanner(new ByteArrayInputStream("N\n".getBytes()));
        assertFalse(InputUtils.confirmar(sc, ""));
        sc.close();
    }

    @Test
    void leerDecimal() {
        Scanner sc = new Scanner(new ByteArrayInputStream("3.14\n".getBytes()));
        double d = InputUtils.leerDecimal(sc, "");
        assertEquals(3.14, d, 0.001);
        sc.close();
    }
}
