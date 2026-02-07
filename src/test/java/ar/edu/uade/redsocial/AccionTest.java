package ar.edu.uade.redsocial;

import ar.edu.uade.redsocial.model.Accion;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AccionTest {

    @Test
    void getters() {
        Accion a = new Accion("agregar", "cliente X");
        assertEquals("agregar", a.getTipo());
        assertEquals("cliente X", a.getDetalle());
        assertNotNull(a.getFechaHora());
    }

    @Test
    void toStringContieneTipoYDetalle() {
        Accion a = new Accion("seguir", "Alice->Bob");
        String s = a.toString();
        assertTrue(s.contains("seguir"));
        assertTrue(s.contains("Alice"));
    }

    @Test
    void fechaHoraCercanaAAhora() {
        Accion a = new Accion("test", "detalle");
        LocalDateTime ahora = LocalDateTime.now();
        assertTrue(a.getFechaHora().isBefore(ahora.plusSeconds(2)));
        assertTrue(a.getFechaHora().isAfter(ahora.minusSeconds(2)));
    }
}
