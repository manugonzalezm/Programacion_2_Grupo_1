package ar.edu.uade.redsocial.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

public class Menu {
    
    private String titulo;
    private List<OpcionMenu> opciones;
    private String mensajeSalida;
    private String opcionSalida;
    private boolean limpiarConsola;
    
    public Menu() {
        this.titulo = "MENU";
        this.opciones = new ArrayList<>();
        this.mensajeSalida = "Saliendo...";
        this.opcionSalida = "0";
        this.limpiarConsola = false;
    }
    
    public Menu(String titulo) {
        this();
        this.titulo = titulo;
    }
    
    public Menu agregarOpcion(String codigo, String descripcion, Runnable accion) {
        opciones.add(new OpcionMenu(codigo, descripcion, accion));
        return this;
    }
    
    public Menu agregarOpcion(String codigo, String descripcion, Consumer<Scanner> accion) {
        opciones.add(new OpcionMenu(codigo, descripcion, accion));
        return this;
    }
    
    public Menu setMensajeSalida(String mensaje) {
        this.mensajeSalida = mensaje;
        return this;
    }
    
    public Menu setOpcionSalida(String codigo) {
        this.opcionSalida = codigo;
        return this;
    }
    
    public Menu setLimpiarConsola(boolean limpiar) {
        this.limpiarConsola = limpiar;
        return this;
    }
    
    public Menu setTitulo(String titulo) {
        this.titulo = titulo;
        return this;
    }
    
    public void ejecutar(Scanner scanner) {
        boolean continuar = true;
        
        while (continuar) {
            if (limpiarConsola) {
                limpiarConsola();
            }
            
            imprimirMenu();
            
            String opcion = scanner.nextLine().trim();
            
            if (opcion.equals(opcionSalida)) {
                System.out.println("\n" + mensajeSalida);
                continuar = false;
            } else {
                boolean opcionEncontrada = false;
                
                for (OpcionMenu opcionMenu : opciones) {
                    if (opcionMenu.getCodigo().equals(opcion)) {
                        opcionEncontrada = true;
                        try {
                            opcionMenu.ejecutar(scanner);
                        } catch (Exception e) {
                            System.out.println("\nError al ejecutar la opción: " + e.getMessage());
                        }
                        break;
                    }
                }
                
                if (!opcionEncontrada) {
                    System.out.println("\nOpción no válida. Por favor, seleccione una opción del menú.");
                }
                
                System.out.println("\nPresione Enter para continuar...");
                scanner.nextLine();
            }
        }
    }
    
    private void imprimirMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println(titulo);
        System.out.println("=".repeat(50));
        
        for (OpcionMenu opcion : opciones) {
            System.out.println(opcion.getCodigo() + "- " + opcion.getDescripcion());
        }
        
        System.out.println(opcionSalida + "- SALIR");
        System.out.println("=".repeat(50));
        System.out.print("Seleccione una opción: ");
    }
    
    private void limpiarConsola() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
    
    private static class OpcionMenu {
        private String codigo;
        private String descripcion;
        private Runnable accionRunnable;
        private Consumer<Scanner> accionConsumer;
        
        public OpcionMenu(String codigo, String descripcion, Runnable accion) {
            this.codigo = codigo;
            this.descripcion = descripcion;
            this.accionRunnable = accion;
            this.accionConsumer = null;
        }
        
        public OpcionMenu(String codigo, String descripcion, Consumer<Scanner> accion) {
            this.codigo = codigo;
            this.descripcion = descripcion;
            this.accionRunnable = null;
            this.accionConsumer = accion;
        }
        
        public String getCodigo() {
            return codigo;
        }
        
        public String getDescripcion() {
            return descripcion;
        }
        
        public void ejecutar(Scanner scanner) {
            if (accionRunnable != null) {
                accionRunnable.run();
            } else if (accionConsumer != null) {
                accionConsumer.accept(scanner);
            }
        }
    }
}
