package ar.edu.uade.redsocial.utils;

import java.util.Scanner;

public class InputUtils {
    
    public static String leerTexto(Scanner scanner, String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }
    
    public static String leerTexto(Scanner scanner) {
        return scanner.nextLine().trim();
    }
    
    public static int leerEntero(Scanner scanner, String mensaje) {
        System.out.print(mensaje);
        String entrada = scanner.nextLine().trim();
        try {
            return Integer.parseInt(entrada);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Debe ingresar un número entero válido.");
        }
    }
    
    public static int leerEnteroConReintentos(Scanner scanner, String mensaje, String mensajeError) {
        while (true) {
            try {
                return leerEntero(scanner, mensaje);
            } catch (NumberFormatException e) {
                System.out.println(mensajeError);
            }
        }
    }
    
    public static int leerEnteroConReintentos(Scanner scanner, String mensaje) {
        return leerEnteroConReintentos(scanner, mensaje, "Valor inválido. Por favor, ingrese un número entero.");
    }
    
    public static double leerDecimal(Scanner scanner, String mensaje) {
        System.out.print(mensaje);
        String entrada = scanner.nextLine().trim();
        try {
            return Double.parseDouble(entrada);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Debe ingresar un número decimal válido.");
        }
    }
    
    public static double leerDecimalConReintentos(Scanner scanner, String mensaje, String mensajeError) {
        while (true) {
            try {
                return leerDecimal(scanner, mensaje);
            } catch (NumberFormatException e) {
                System.out.println(mensajeError);
            }
        }
    }
    
    public static double leerDecimalConReintentos(Scanner scanner, String mensaje) {
        return leerDecimalConReintentos(scanner, mensaje, "Valor inválido. Por favor, ingrese un número decimal.");
    }
    
    public static boolean confirmar(Scanner scanner, String mensaje) {
        System.out.print(mensaje + " (S/N): ");
        String respuesta = scanner.nextLine().trim().toUpperCase();
        return respuesta.equals("S") || respuesta.equals("SI") || respuesta.equals("Y") || respuesta.equals("YES");
    }
    
    public static void pausar(Scanner scanner) {
        System.out.println("\nPresione Enter para continuar...");
        scanner.nextLine();
    }
    
    public static void pausar(Scanner scanner, String mensaje) {
        System.out.println(mensaje);
        scanner.nextLine();
    }
}
