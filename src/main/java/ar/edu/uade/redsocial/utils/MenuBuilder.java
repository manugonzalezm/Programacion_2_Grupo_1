package ar.edu.uade.redsocial.utils;

import java.util.Scanner;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class MenuBuilder {

    private Menu menu;

    public MenuBuilder(String titulo) {
        this.menu = new Menu(titulo);
    }

    public MenuBuilder() {
        this.menu = new Menu();
    }

    // --- Opciones estáticas (siempre visibles) ---

    public MenuBuilder agregarOpcion(String codigo, String descripcion, Runnable accion) {
        menu.agregarOpcion(codigo, descripcion, accion);
        return this;
    }

    public MenuBuilder agregarOpcion(String codigo, String descripcion, Consumer<Scanner> accion) {
        menu.agregarOpcion(codigo, descripcion, accion);
        return this;
    }

    // --- Opciones dinámicas (visibilidad y etiqueta condicionales) ---

    public MenuBuilder agregarOpcion(String codigo, Supplier<String> descripcion, Runnable accion, BooleanSupplier visible) {
        menu.agregarOpcion(codigo, descripcion, accion, visible);
        return this;
    }

    public MenuBuilder agregarOpcion(String codigo, Supplier<String> descripcion, Consumer<Scanner> accion, BooleanSupplier visible) {
        menu.agregarOpcion(codigo, descripcion, accion, visible);
        return this;
    }

    public MenuBuilder setMensajeSalida(String mensaje) {
        menu.setMensajeSalida(mensaje);
        return this;
    }

    public MenuBuilder setOpcionSalida(String codigo) {
        menu.setOpcionSalida(codigo);
        return this;
    }

    public MenuBuilder setLimpiarConsola(boolean limpiar) {
        menu.setLimpiarConsola(limpiar);
        return this;
    }

    public MenuBuilder setEstadoHeader(Supplier<String> header) {
        menu.setEstadoHeader(header);
        return this;
    }

    public MenuBuilder setEstadoHeader(String header) {
        menu.setEstadoHeader(header);
        return this;
    }

    public Menu build() {
        return menu;
    }
}
