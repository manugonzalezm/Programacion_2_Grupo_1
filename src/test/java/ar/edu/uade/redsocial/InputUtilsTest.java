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
    void leerTextoConEspacios() {
        Scanner sc = new Scanner(new ByteArrayInputStream("  hola mundo  \n".getBytes()));
        assertEquals("hola mundo", InputUtils.leerTexto(sc, "> "));
        sc.close();
    }

    @Test
    void leerTextoNoVacioConTextoValido() {
        Scanner sc = new Scanner(new ByteArrayInputStream("texto\n".getBytes()));
        assertEquals("texto", InputUtils.leerTextoNoVacio(sc, "> "));
        sc.close();
    }

    @Test
    void leerTextoNoVacioReintentoConVacio() {
        // Primero envia vacio, luego un valor valido
        Scanner sc = new Scanner(new ByteArrayInputStream("\nvalido\n".getBytes()));
        assertEquals("valido", InputUtils.leerTextoNoVacio(sc, "> "));
        sc.close();
    }

    @Test
    void leerTextoNoVacioReintentoConEspacios() {
        // Espacios se trimmean, quedan vacios, luego valor valido
        Scanner sc = new Scanner(new ByteArrayInputStream("   \nok\n".getBytes()));
        assertEquals("ok", InputUtils.leerTextoNoVacio(sc, "> "));
        sc.close();
    }

    @Test
    void leerTextoNoVacioMultiplesReintentos() {
        Scanner sc = new Scanner(new ByteArrayInputStream("\n\n\nfinal\n".getBytes()));
        assertEquals("final", InputUtils.leerTextoNoVacio(sc, "> "));
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
    void leerEnteroNegativo() {
        Scanner sc = new Scanner(new ByteArrayInputStream("-10\n".getBytes()));
        assertEquals(-10, InputUtils.leerEntero(sc, ""));
        sc.close();
    }

    @Test
    void leerEnteroLanzaSiNoEsNumero() {
        Scanner sc = new Scanner(new ByteArrayInputStream("abc\n".getBytes()));
        assertThrows(NumberFormatException.class, () -> InputUtils.leerEntero(sc, ""));
        sc.close();
    }

    @Test
    void leerEnteroConReintentosExito() {
        Scanner sc = new Scanner(new ByteArrayInputStream("55\n".getBytes()));
        assertEquals(55, InputUtils.leerEnteroConReintentos(sc, "> "));
        sc.close();
    }

    @Test
    void leerEnteroConReintentosReintenta() {
        // Primero "abc" falla, luego "99" funciona
        Scanner sc = new Scanner(new ByteArrayInputStream("abc\n99\n".getBytes()));
        assertEquals(99, InputUtils.leerEnteroConReintentos(sc, "> "));
        sc.close();
    }

    @Test
    void leerEnteroConReintentosMultiples() {
        Scanner sc = new Scanner(new ByteArrayInputStream("x\ny\n7\n".getBytes()));
        assertEquals(7, InputUtils.leerEnteroConReintentos(sc, "> "));
        sc.close();
    }

    @Test
    void leerEnteroConReintentosConMensajeError() {
        Scanner sc = new Scanner(new ByteArrayInputStream("abc\n10\n".getBytes()));
        assertEquals(10, InputUtils.leerEnteroConReintentos(sc, "> ", "Error personalizado"));
        sc.close();
    }

    @Test
    void leerDecimal() {
        Scanner sc = new Scanner(new ByteArrayInputStream("3.14\n".getBytes()));
        double d = InputUtils.leerDecimal(sc, "");
        assertEquals(3.14, d, 0.001);
        sc.close();
    }

    @Test
    void leerDecimalLanzaSiNoEsNumero() {
        Scanner sc = new Scanner(new ByteArrayInputStream("abc\n".getBytes()));
        assertThrows(NumberFormatException.class, () -> InputUtils.leerDecimal(sc, ""));
        sc.close();
    }

    @Test
    void leerDecimalConReintentosExito() {
        Scanner sc = new Scanner(new ByteArrayInputStream("2.5\n".getBytes()));
        assertEquals(2.5, InputUtils.leerDecimalConReintentos(sc, "> "), 0.001);
        sc.close();
    }

    @Test
    void leerDecimalConReintentosReintenta() {
        Scanner sc = new Scanner(new ByteArrayInputStream("abc\n1.5\n".getBytes()));
        assertEquals(1.5, InputUtils.leerDecimalConReintentos(sc, "> "), 0.001);
        sc.close();
    }

    @Test
    void leerDecimalConReintentosConMensajeError() {
        Scanner sc = new Scanner(new ByteArrayInputStream("abc\n4.2\n".getBytes()));
        assertEquals(4.2, InputUtils.leerDecimalConReintentos(sc, "> ", "Error"), 0.001);
        sc.close();
    }

    @Test
    void confirmarConS() {
        Scanner sc = new Scanner(new ByteArrayInputStream("S\n".getBytes()));
        assertTrue(InputUtils.confirmar(sc, ""));
        sc.close();
    }

    @Test
    void confirmarConSI() {
        Scanner sc = new Scanner(new ByteArrayInputStream("SI\n".getBytes()));
        assertTrue(InputUtils.confirmar(sc, ""));
        sc.close();
    }

    @Test
    void confirmarConY() {
        Scanner sc = new Scanner(new ByteArrayInputStream("y\n".getBytes()));
        assertTrue(InputUtils.confirmar(sc, ""));
        sc.close();
    }

    @Test
    void confirmarConYES() {
        Scanner sc = new Scanner(new ByteArrayInputStream("yes\n".getBytes()));
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
    void confirmarConTextoAleatorio() {
        Scanner sc = new Scanner(new ByteArrayInputStream("quizas\n".getBytes()));
        assertFalse(InputUtils.confirmar(sc, ""));
        sc.close();
    }

    @Test
    void pausarSinMensaje() {
        Scanner sc = new Scanner(new ByteArrayInputStream("\n".getBytes()));
        InputUtils.pausar(sc);
        sc.close();
    }

    @Test
    void pausarConMensaje() {
        Scanner sc = new Scanner(new ByteArrayInputStream("\n".getBytes()));
        InputUtils.pausar(sc, "Esperando...");
        sc.close();
    }
}
