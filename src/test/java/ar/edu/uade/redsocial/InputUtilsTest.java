package ar.edu.uade.redsocial;

import ar.edu.uade.redsocial.utils.InputUtils;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class InputUtilsTest {

    @Test
    void leerTexto() {
        Scanner sc = new Scanner(new ByteArrayInputStream("hola mundo\n".getBytes()));
        assertEquals("hola mundo", InputUtils.leerTexto(sc, "> "));
        sc.close();
    }

    @Test
    void leerTextoNoVacioReintentoConVacio() {
        Scanner sc = new Scanner(new ByteArrayInputStream("\nvalido\n".getBytes()));
        assertEquals("valido", InputUtils.leerTextoNoVacio(sc, "> "));
        sc.close();
    }

    @Test
    void leerEntero() {
        Scanner sc = new Scanner(new ByteArrayInputStream("42\n".getBytes()));
        assertEquals(42, InputUtils.leerEntero(sc, ""));
        sc.close();
    }

    @Test
    void leerEnteroConReintentosReintenta() {
        Scanner sc = new Scanner(new ByteArrayInputStream("abc\n99\n".getBytes()));
        assertEquals(99, InputUtils.leerEnteroConReintentos(sc, "> "));
        sc.close();
    }

    @Test
    void confirmar() {
        Scanner scSi = new Scanner(new ByteArrayInputStream("S\n".getBytes()));
        assertTrue(InputUtils.confirmar(scSi, ""));
        scSi.close();

        Scanner scNo = new Scanner(new ByteArrayInputStream("N\n".getBytes()));
        assertFalse(InputUtils.confirmar(scNo, ""));
        scNo.close();
    }
}
