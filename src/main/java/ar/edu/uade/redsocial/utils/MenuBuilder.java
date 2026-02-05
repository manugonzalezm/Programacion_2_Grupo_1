package ar.edu.uade.redsocial.utils;

import java.util.Scanner;
import java.util.function.Consumer;

public class MenuBuilder {
    
    private Menu menu;
    
    public MenuBuilder(String titulo) {
        this.menu = new Menu(titulo);
    }
    
    public MenuBuilder() {
        this.menu = new Menu();
    }
    
    public MenuBuilder agregarOpcion(String codigo, String descripcion, Runnable accion) {
        menu.agregarOpcion(codigo, descripcion, accion);
        return this;
    }
    
    public MenuBuilder agregarOpcion(String codigo, String descripcion, Consumer<Scanner> accion) {
        menu.agregarOpcion(codigo, descripcion, accion);
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
    
    public Menu build() {
        return menu;
    }
}
