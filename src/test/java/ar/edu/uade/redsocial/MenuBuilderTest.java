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
    void builderFluidoCompleto() {
        int[] contador = {0};
        Menu m = new MenuBuilder("Full")
            .agregarOpcion("1", "Runnable", () -> contador[0]++)
            .agregarOpcion("2", "Consumer", scanner -> contador[0]++)
            .setMensajeSalida("Bye")
            .setOpcionSalida("0")
            .setLimpiarConsola(false)
            .build();

        Scanner sc = new Scanner(new ByteArrayInputStream("1\n\n2\n\n0\n".getBytes()));
        m.ejecutar(sc);
        sc.close();
        assertEquals(2, contador[0]);
    }
}
