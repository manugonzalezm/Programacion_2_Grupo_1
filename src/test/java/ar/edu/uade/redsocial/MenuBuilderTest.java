package ar.edu.uade.redsocial;

import ar.edu.uade.redsocial.utils.Menu;
import ar.edu.uade.redsocial.utils.MenuBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MenuBuilderTest {

    @Test
    void buildDevuelveMenu() {
        Menu m = new MenuBuilder("Titulo").build();
        assertNotNull(m);
    }

    @Test
    void agregarOpcionYBuild() {
        Menu m = new MenuBuilder("Menu")
            .agregarOpcion("1", "Primera", () -> {})
            .setMensajeSalida("Adios")
            .build();
        assertNotNull(m);
    }
}
