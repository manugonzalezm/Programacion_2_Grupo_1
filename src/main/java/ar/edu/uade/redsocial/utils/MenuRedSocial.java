package ar.edu.uade.redsocial.utils;

import ar.edu.uade.redsocial.model.Accion;
import ar.edu.uade.redsocial.model.Cliente;
import ar.edu.uade.redsocial.model.SolicitudSeguimiento;
import ar.edu.uade.redsocial.services.CargadorClientesJson;
import ar.edu.uade.redsocial.services.ExportadorAccionesCsv;
import ar.edu.uade.redsocial.services.GestorClientes;
import ar.edu.uade.redsocial.services.GuardadorClientesJson;
import ar.edu.uade.redsocial.services.HistorialAcciones;

import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class MenuRedSocial {

    private final GestorClientes gestorClientes;
    private final HistorialAcciones historial;
    private final Scanner scanner;
    private Cliente usuarioLogueado;

    public MenuRedSocial(Scanner scanner, GestorClientes gestorClientes,
                         HistorialAcciones historial) {
        this.scanner = scanner;
        this.gestorClientes = gestorClientes;
        this.historial = historial;
    }

    public void cargarDatosIniciales() {
        CargadorClientesJson.readFromFile(gestorClientes);
        System.out.println("Datos iniciales cargados correctamente.");
    }

    public void setUsuarioLogueado(Cliente cliente) { this.usuarioLogueado = cliente; }
    public Cliente getUsuarioLogueado() { return this.usuarioLogueado; }

    // ─────────────────────────────────────────────────────────────────────────
    //  MENÚ SIN SESIÓN  (1-9, salir=0)
    // ─────────────────────────────────────────────────────────────────────────

    public Menu crearMenuSinLogin() {
        return new MenuBuilder("🌐 RED SOCIAL EMPRESARIAL")
            .setEstadoHeader("[ 🔴 Sin sesion iniciada ]")
            .agregarOpcion("1",  "🔑 Iniciar Sesion",                        scanner -> login())
            .agregarOpcion("2",  "🔍 Buscar usuario por nombre",             scanner -> buscarClientePorNombre())
            .agregarOpcion("3",  "📊 Buscar usuario por puntuacion",         scanner -> buscarClientePorScoring())
            .agregarOpcion("4",  "📝 Registrarse",                           scanner -> agregarCliente())
            .agregarOpcion("5",  "📜 Ultimas 10 acciones",                   () -> listarUltimasAcciones())
            .agregarOpcion("6",  "👥 Ver todos los usuarios",                () -> listarTodosLosClientes())
            .agregarOpcion("7",  "🌳 Explorar red de contactos",             scanner -> consultarRedConexiones())
            .agregarOpcion("8",  "🤝 Ver seguidos y amigos de un usuario",   scanner -> verRelacionesUsuario())
            .agregarOpcion("9",  "📏 Distancia entre dos usuarios",          scanner -> calcularDistancia())
            .setOpcionSalida("0")
            .setMensajeSalida("👋 Saliendo del sistema...")
            .setLimpiarConsola(false)
            .build();
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  MENÚ CON SESIÓN  (1-15, salir=0)
    // ─────────────────────────────────────────────────────────────────────────

    public Menu crearMenuConLogin() {
        String nombre = usuarioLogueado.getNombre();
        return new MenuBuilder("🌐 RED SOCIAL EMPRESARIAL")
            .setEstadoHeader("[ 🟢 Sesion activa: " + nombre + " ]")
            .agregarOpcion("1",  "🔍 Buscar usuario por nombre",             scanner -> buscarClientePorNombre())
            .agregarOpcion("2",  "📊 Buscar usuario por puntuacion",         scanner -> buscarClientePorScoring())
            .agregarOpcion("3",  "➕ Seguir a un usuario",                   scanner -> seguirCliente())
            .agregarOpcion("4",  "➖ Dejar de seguir a un usuario",          scanner -> dejarDeSeguir())
            .agregarOpcion("5",  "🤝 Enviar solicitud de amistad",           scanner -> enviarSolicitudAmistad())
            .agregarOpcion("6",  "✅ Aceptar solicitud de amistad",          () -> aceptarSolicitudAmistad())
            .agregarOpcion("7",  "❌ Rechazar solicitud de amistad",         () -> rechazarSolicitudAmistad())
            .agregarOpcion("8",  "↩️  Deshacer ultima accion",               () -> deshacerUltimaAccion())
            .agregarOpcion("9",  "📋 Mis solicitudes de amistad pendientes", () -> listarMisSolicitudes())
            .agregarOpcion("10", "📜 Ultimas 10 acciones",                   () -> listarUltimasAcciones())
            .agregarOpcion("11", "👥 Ver todos los usuarios",                () -> listarTodosLosClientes())
            .agregarOpcion("12", "👤 Ver mis datos",                         () -> verMisDatos())
            .agregarOpcion("13", "🌳 Explorar red de contactos",             scanner -> consultarRedConexiones())
            .agregarOpcion("14", "🤝 Ver seguidos y amigos de un usuario",   scanner -> verRelacionesUsuario())
            .agregarOpcion("15", "📏 Distancia entre dos usuarios",          scanner -> calcularDistancia())
            .agregarOpcion("16", "🚪 Cerrar Sesion",                         scanner -> logout())
            .setOpcionSalida("0")
            .setMensajeSalida("👋 Saliendo del sistema...")
            .setLimpiarConsola(false)
            .build();
    }

    public void ejecutar() {
        boolean continuar = true;
        while (continuar) {
            Menu menu = (usuarioLogueado == null) ? crearMenuSinLogin() : crearMenuConLogin();
            continuar = menu.ejecutarUnaVez(scanner);
        }
        GuardadorClientesJson.guardar(gestorClientes);
        exportarAccionesCsv();
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  ACCIONES — sesión
    // ─────────────────────────────────────────────────────────────────────────

    private void login() {
        System.out.println("\n=== 🔑 Iniciar Sesion ===");
        if (usuarioLogueado != null) {
            System.out.println("⚠️  Ya tienes una sesion activa como: " + usuarioLogueado.getNombre());
            return;
        }
        String nombre = InputUtils.leerTextoNoVacio(scanner, "Ingrese su nombre: ");
        Cliente cliente = gestorClientes.buscarPorNombre(nombre);
        if (cliente != null) {
            usuarioLogueado = cliente;
            System.out.println("✅ Sesion iniciada como: " + nombre);
            historial.registrarAccion(new Accion("Login", nombre));
        } else {
            System.out.println("❌ No existe un usuario con ese nombre.");
        }
    }

    private void logout() {
        System.out.println("\n=== 🚪 Cerrar Sesion ===");
        historial.registrarAccion(new Accion("Logout", usuarioLogueado.getNombre()));
        System.out.println("✅ Sesion cerrada. Hasta luego, " + usuarioLogueado.getNombre() + "!");
        usuarioLogueado = null;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  ACCIONES — búsqueda y registro
    // ─────────────────────────────────────────────────────────────────────────

    private void agregarCliente() {
        System.out.println("\n=== 📝 Registrarse ===");
        String nombre = InputUtils.leerTextoNoVacio(scanner, "Ingrese su nombre: ");
        if (gestorClientes.buscarPorNombre(nombre) != null) {
            System.out.println("❌ Ya existe un usuario con el nombre: " + nombre);
            return;
        }
        int scoring = InputUtils.leerEnteroConReintentos(scanner, "Ingrese su puntuacion inicial: ");
        Cliente nuevoCliente = new Cliente(nombre, scoring);
        if (gestorClientes.agregarCliente(nuevoCliente)) {
            System.out.println("✅ Registro exitoso: " + nuevoCliente);
            historial.registrarAccion(new Accion("Registrarse", nombre));
        } else {
            System.out.println("❌ No se pudo completar el registro.");
        }
    }

    private void buscarClientePorNombre() {
        System.out.println("\n=== 🔍 Buscar usuario por nombre ===");
        String nombre = InputUtils.leerTexto(scanner, "Ingrese el nombre: ");
        Cliente cliente = gestorClientes.buscarPorNombre(nombre);
        if (cliente != null) {
            System.out.println("\n✅ Usuario encontrado: " + cliente);
            Set<String> seguidos = gestorClientes.obtenerSeguidos(nombre);
            Set<String> amigos   = gestorClientes.obtenerVecinos(nombre);
            System.out.println("   Siguiendo  (" + seguidos.size() + "): " + (seguidos.isEmpty() ? "(nadie)" : seguidos));
            System.out.println("   Amistades  (" + amigos.size()   + "): " + (amigos.isEmpty()   ? "(nadie)" : amigos));
            historial.registrarAccion(new Accion("Buscar por nombre", nombre));
        } else {
            System.out.println("\n❌ No se encontro ningun usuario con el nombre: " + nombre);
        }
    }

    private void buscarClientePorScoring() {
        System.out.println("\n=== 📊 Buscar usuario por puntuacion ===");
        int scoring = InputUtils.leerEnteroConReintentos(scanner, "Ingrese la puntuacion: ");
        List<Cliente> clientes = gestorClientes.buscarPorScoring(scoring);
        if (clientes.isEmpty()) {
            System.out.println("\n❌ No se encontraron usuarios con puntuacion: " + scoring);
        } else {
            System.out.println("\n✅ Usuarios con puntuacion " + scoring + ":");
            for (Cliente c : clientes) System.out.println("  - " + c);
            historial.registrarAccion(new Accion("Buscar por scoring", String.valueOf(scoring)));
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  ACCIONES — seguimiento directo (sin aprobación)
    // ─────────────────────────────────────────────────────────────────────────

    private void seguirCliente() {
        System.out.println("\n=== ➕ Seguir a un usuario ===");
        String seguido = InputUtils.leerTexto(scanner, "Ingrese el nombre del usuario a seguir: ");
        if (gestorClientes.buscarPorNombre(seguido) == null) {
            System.out.println("❌ No existe ese usuario.");
            return;
        }
        String yo = usuarioLogueado.getNombre();
        if (yo.equals(seguido)) {
            System.out.println("❌ No puedes seguirte a ti mismo.");
            return;
        }
        boolean ok = gestorClientes.agregarSeguido(yo, seguido);
        if (ok) {
            System.out.println("✅ Ahora sigues a " + seguido + ".");
            historial.registrarAccion(new Accion("Seguir", yo + " -> " + seguido));
        } else {
            System.out.println("⚠️  No se pudo seguir a " + seguido
                    + " (ya lo sigues o alcanzaste el limite de seguidos).");
        }
    }

    private void dejarDeSeguir() {
        System.out.println("\n=== ➖ Dejar de seguir a un usuario ===");
        String yo = usuarioLogueado.getNombre();
        Set<String> seguidos = gestorClientes.obtenerSeguidos(yo);
        if (seguidos.isEmpty()) {
            System.out.println("📭 No sigues a nadie actualmente.");
            return;
        }
        System.out.println("Usuarios que sigues: " + seguidos);
        String objetivo = InputUtils.leerTexto(scanner, "Ingrese el nombre del usuario a dejar de seguir: ");
        boolean ok = gestorClientes.quitarSeguido(yo, objetivo);
        if (ok) {
            System.out.println("✅ Dejaste de seguir a " + objetivo + ".");
            historial.registrarAccion(new Accion("Dejar de seguir", yo + " -> " + objetivo));
        } else {
            System.out.println("⚠️  No seguias a ese usuario.");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  ACCIONES — solicitudes de amistad (con aprobación, por usuario)
    // ─────────────────────────────────────────────────────────────────────────

    private void enviarSolicitudAmistad() {
        System.out.println("\n=== 🤝 Enviar Solicitud de Amistad ===");
        String destino = InputUtils.leerTexto(scanner, "Ingrese el nombre del usuario al que desea enviar la solicitud: ");
        if (gestorClientes.buscarPorNombre(destino) == null) {
            System.out.println("❌ No existe ese usuario.");
            return;
        }
        String yo = usuarioLogueado.getNombre();
        if (yo.equals(destino)) {
            System.out.println("❌ No puedes enviarte una solicitud a ti mismo.");
            return;
        }
        boolean ok = gestorClientes.enviarSolicitudAmistad(yo, destino);
        if (ok) {
            System.out.println("✅ Solicitud de amistad enviada a " + destino + ".");
            historial.registrarAccion(new Accion("Enviar solicitud amistad", yo + " -> " + destino));
        } else {
            System.out.println("⚠️  Ya existe una solicitud pendiente para " + destino + ".");
        }
    }

    private void aceptarSolicitudAmistad() {
        System.out.println("\n=== ✅ Aceptar Solicitud de Amistad ===");
        String yo = usuarioLogueado.getNombre();
        List<SolicitudSeguimiento> pendientes = gestorClientes.listarSolicitudesRecibidas(yo);
        if (pendientes.isEmpty()) {
            System.out.println("📭 No tienes solicitudes de amistad pendientes.");
            return;
        }
        mostrarSolicitudes(pendientes);
        int numero = InputUtils.leerEnteroConReintentos(scanner, "Selecciona el numero a aceptar (0 para cancelar): ");
        if (numero == 0) { System.out.println("Operacion cancelada."); return; }
        if (numero < 1 || numero > pendientes.size()) { System.out.println("❌ Numero invalido."); return; }

        SolicitudSeguimiento s = pendientes.get(numero - 1);
        boolean ok = gestorClientes.aceptarSolicitudAmistad(yo, numero - 1);
        if (ok) {
            System.out.println("✅ " + s.getOrigen() + " y tu ahora son amigos.");
            historial.registrarAccion(new Accion("Aceptar solicitud amistad",
                    s.getOrigen() + " <-> " + s.getDestino()));
        }
    }

    private void rechazarSolicitudAmistad() {
        System.out.println("\n=== ❌ Rechazar Solicitud de Amistad ===");
        String yo = usuarioLogueado.getNombre();
        List<SolicitudSeguimiento> pendientes = gestorClientes.listarSolicitudesRecibidas(yo);
        if (pendientes.isEmpty()) {
            System.out.println("📭 No tienes solicitudes de amistad pendientes.");
            return;
        }
        mostrarSolicitudes(pendientes);
        int numero = InputUtils.leerEnteroConReintentos(scanner, "Selecciona el numero a rechazar (0 para cancelar): ");
        if (numero == 0) { System.out.println("Operacion cancelada."); return; }
        if (numero < 1 || numero > pendientes.size()) { System.out.println("❌ Numero invalido."); return; }

        SolicitudSeguimiento s = pendientes.get(numero - 1);
        boolean ok = gestorClientes.rechazarSolicitudAmistad(yo, numero - 1);
        if (ok) {
            System.out.println("❌ Solicitud de amistad de " + s.getOrigen() + " rechazada.");
            historial.registrarAccion(new Accion("Rechazar solicitud amistad",
                    s.getOrigen() + " -> " + s.getDestino()));
        }
    }

    private void mostrarSolicitudes(List<SolicitudSeguimiento> pendientes) {
        for (int i = 0; i < pendientes.size(); i++) {
            System.out.println("  " + (i + 1) + ". 👤 " + pendientes.get(i).getOrigen() + " quiere ser tu amigo");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  ACCIONES — deshacer
    // ─────────────────────────────────────────────────────────────────────────

    private void deshacerUltimaAccion() {
        System.out.println("\n=== ↩️ Deshacer Ultima Accion ===");
        Accion accion = historial.deshacerUltimaAccion();
        if (accion == null) { System.out.println("📭 No hay acciones para deshacer."); return; }

        String tipo    = accion.getTipo();
        String detalle = accion.getDetalle();
        boolean impacto = false;

        if ("Registrarse".equals(tipo) || "Agregar cliente".equals(tipo)) {
            impacto = gestorClientes.eliminarCliente(detalle);
            if (impacto) System.out.println("🗑️  Usuario \"" + detalle + "\" eliminado.");

        } else if ("Seguir".equals(tipo) && detalle != null && detalle.contains(" -> ")) {
            String[] p = detalle.split(" -> ", 2);
            if (p.length == 2) {
                impacto = gestorClientes.quitarSeguido(p[0].trim(), p[1].trim());
                if (impacto) System.out.println("↩️  Seguimiento " + detalle + " revertido.");
            }

        } else if ("Dejar de seguir".equals(tipo) && detalle != null && detalle.contains(" -> ")) {
            String[] p = detalle.split(" -> ", 2);
            if (p.length == 2) {
                impacto = gestorClientes.agregarSeguido(p[0].trim(), p[1].trim());
                if (impacto) System.out.println("↩️  Seguimiento " + detalle + " restaurado.");
            }

        } else if ("Enviar solicitud amistad".equals(tipo) && detalle != null && detalle.contains(" -> ")) {
            String[] p = detalle.split(" -> ", 2);
            if (p.length == 2) {
                impacto = gestorClientes.revocarSolicitudAmistad(p[0].trim(), p[1].trim());
                if (impacto) System.out.println("↩️  Solicitud de amistad " + detalle + " cancelada.");
            }

        } else if ("Aceptar solicitud amistad".equals(tipo) && detalle != null && detalle.contains(" <-> ")) {
            String[] p = detalle.split(" <-> ", 2);
            if (p.length == 2) {
                gestorClientes.eliminarAmistad(p[0].trim(), p[1].trim());
                System.out.println("↩️  Amistad " + detalle + " eliminada.");
                impacto = true;
            }

        } else if ("Rechazar solicitud amistad".equals(tipo) && detalle != null && detalle.contains(" -> ")) {
            String[] p = detalle.split(" -> ", 2);
            if (p.length == 2) {
                impacto = gestorClientes.enviarSolicitudAmistad(p[0].trim(), p[1].trim());
                if (impacto) System.out.println("↩️  Solicitud de amistad " + detalle + " restaurada.");
            }

        } else {
            System.out.println("↩️  Accion deshecha: [" + tipo + "] " + detalle);
            impacto = true;
        }

        if (!impacto) System.out.println("⚠️  Accion removida del historial pero no habia datos que revertir.");
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  ACCIONES — consultas generales
    // ─────────────────────────────────────────────────────────────────────────

    private void listarMisSolicitudes() {
        System.out.println("\n=== 📋 Mis Solicitudes de Amistad Pendientes ===");
        List<SolicitudSeguimiento> pendientes =
                gestorClientes.listarSolicitudesRecibidas(usuarioLogueado.getNombre());
        if (pendientes.isEmpty()) {
            System.out.println("📭 No tienes solicitudes de amistad pendientes.");
        } else {
            int i = 1;
            for (SolicitudSeguimiento s : pendientes) {
                System.out.println("  " + i++ + ". " + s.getOrigen() + " ➡️  " + s.getDestino());
            }
        }
    }

    private void listarUltimasAcciones() {
        System.out.println("\n=== 📜 Ultimas 10 Acciones ===");
        List<Accion> ultimas = historial.listarUltimas(10);
        if (ultimas.isEmpty()) {
            System.out.println("📭 No hay acciones registradas.");
        } else {
            int i = 1;
            for (Accion a : ultimas) {
                System.out.println("  " + i++ + ". [" + a.getTipo() + "] " + a.getDetalle() + " (" + a.getFechaHora() + ")");
            }
        }
    }

    private static final int MAX_CLIENTES_MOSTRAR = 30;

    private void listarTodosLosClientes() {
        System.out.println("\n=== 👥 Lista de Usuarios ===");
        List<Cliente> clientes = gestorClientes.listarClientes();
        if (clientes.isEmpty()) {
            System.out.println("📭 No hay usuarios registrados.");
        } else {
            int total = clientes.size();
            int mostrar = Math.min(total, MAX_CLIENTES_MOSTRAR);
            System.out.println("Total: " + total
                    + (total > MAX_CLIENTES_MOSTRAR ? " (mostrando primeros " + MAX_CLIENTES_MOSTRAR + ")" : ""));
            for (int i = 0; i < mostrar; i++) System.out.println("  - " + clientes.get(i));
        }
        historial.registrarAccion(new Accion("Listar clientes", "total=" + clientes.size()));
    }

    private void verMisDatos() {
        System.out.println("\n=== 👤 Mis Datos ===");
        String nombre = usuarioLogueado.getNombre();
        Set<String> seguidos = gestorClientes.obtenerSeguidos(nombre);
        Set<String> amigos   = gestorClientes.obtenerVecinos(nombre);
        List<SolicitudSeguimiento> pendientes = gestorClientes.listarSolicitudesRecibidas(nombre);
        System.out.println("Nombre:      " + nombre);
        System.out.println("Puntuacion:  " + usuarioLogueado.getScoring());
        System.out.println("Siguiendo  (" + seguidos.size() + "): " + (seguidos.isEmpty() ? "(nadie)" : seguidos));
        System.out.println("Amistades  (" + amigos.size()   + "): " + (amigos.isEmpty()   ? "(nadie)" : amigos));
        System.out.println("Solicitudes pendientes: " + pendientes.size());
    }

    private void consultarRedConexiones() {
        System.out.println("\n=== 🌳 Explorar Red de Contactos ===");
        String nombre = InputUtils.leerTextoNoVacio(scanner, "Ingrese el nombre del usuario: ");
        Cliente cliente = gestorClientes.buscarPorNombre(nombre);
        if (cliente == null) { System.out.println("❌ No existe ese usuario."); return; }

        Set<String> seguidos = gestorClientes.obtenerSeguidos(nombre);
        System.out.println("\n📊 " + nombre + " sigue a: " + (seguidos.isEmpty() ? "(nadie)" : seguidos));

        List<Integer> nivel4 = gestorClientes.consultarConexionesNivel4(nombre);
        if (nivel4.isEmpty()) {
            System.out.println("🌳 " + nombre + " no tiene contactos lejanos suficientes en su red.");
        } else {
            System.out.println("🌳 Personas descubiertas en la red extendida:");
            for (int scoring : nivel4) {
                List<Cliente> coincidentes = gestorClientes.buscarPorScoring(scoring);
                for (Cliente c : coincidentes) {
                    System.out.println("     - " + c.getNombre() + " (puntuacion: " + scoring + ")");
                }
            }
        }
    }

    private void verRelacionesUsuario() {
        System.out.println("\n=== 🤝 Ver Seguidos y Amigos de un Usuario ===");
        String nombre = InputUtils.leerTextoNoVacio(scanner, "Ingrese el nombre del usuario: ");
        if (gestorClientes.buscarPorNombre(nombre) == null) {
            System.out.println("❌ No existe ese usuario.");
            return;
        }
        Set<String> seguidos = gestorClientes.obtenerSeguidos(nombre);
        Set<String> amigos   = gestorClientes.obtenerVecinos(nombre);

        System.out.println("\n➡️  Siguiendo (" + seguidos.size() + "): "
                + (seguidos.isEmpty() ? "(nadie)" : seguidos));
        System.out.println("🤝 Amistades  (" + amigos.size() + "): "
                + (amigos.isEmpty() ? "(nadie)" : amigos));
    }

    private void calcularDistancia() {
        System.out.println("\n=== 📏 Distancia entre Dos Usuarios ===");
        String origen  = InputUtils.leerTextoNoVacio(scanner, "Ingrese el nombre del primer usuario: ");
        String destino = InputUtils.leerTextoNoVacio(scanner, "Ingrese el nombre del segundo usuario: ");

        if (gestorClientes.buscarPorNombre(origen) == null || gestorClientes.buscarPorNombre(destino) == null) {
            System.out.println("❌ Uno o ambos usuarios no existen.");
            return;
        }

        int distSeguimiento = gestorClientes.calcularDistanciaSeguimiento(origen, destino);
        int distAmistad     = gestorClientes.calcularDistanciaAmistad(origen, destino);

        System.out.println("\n📊 Distancia entre " + origen + " y " + destino + ":");
        System.out.println("  ➡️  Por seguimiento: " + (distSeguimiento == -1 ? "sin camino" : distSeguimiento + " salto(s)"));
        System.out.println("  🤝 Por amistad:      " + (distAmistad     == -1 ? "sin camino" : distAmistad     + " salto(s)"));
    }

    private void exportarAccionesCsv() {
        List<Accion> todas = historial.listarUltimas(1000);
        if (!todas.isEmpty()) {
            try {
                ExportadorAccionesCsv.exportar(todas, "acciones.csv");
                System.out.println("📄 Historial exportado a acciones.csv");
            } catch (Exception e) {
                System.out.println("⚠️  No se pudo exportar acciones: " + e.getMessage());
            }
        }
    }
}
