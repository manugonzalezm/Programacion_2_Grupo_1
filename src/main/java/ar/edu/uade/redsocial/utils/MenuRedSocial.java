package ar.edu.uade.redsocial.utils;

import ar.edu.uade.redsocial.model.Accion;
import ar.edu.uade.redsocial.model.Cliente;
import ar.edu.uade.redsocial.model.SolicitudSeguimiento;
import ar.edu.uade.redsocial.services.CargadorClientesJson;
import ar.edu.uade.redsocial.services.ColaSolicitudesSeguimiento;
import ar.edu.uade.redsocial.services.ExportadorAccionesCsv;
import ar.edu.uade.redsocial.services.GestorClientes;
import ar.edu.uade.redsocial.services.GuardadorClientesJson;
import ar.edu.uade.redsocial.services.HistorialAcciones;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuRedSocial {

    private final GestorClientes gestorClientes;
    private final HistorialAcciones historial;
    private final ColaSolicitudesSeguimiento colaSolicitudes;
    private final Scanner scanner;
    private Cliente usuarioLogueado;

    public MenuRedSocial(Scanner scanner, GestorClientes gestorClientes, HistorialAcciones historial, ColaSolicitudesSeguimiento colaSolicitudes) { // complejidad O(1)
        this.scanner = scanner;
        this.gestorClientes = gestorClientes;
        this.historial = historial;
        this.colaSolicitudes = colaSolicitudes;
    }

    /** Carga el JSON en el gestor recibido por constructor. */
    public void cargarDatosIniciales() { // complejidad O(n)
        CargadorClientesJson.readFromFile(gestorClientes);
        System.out.println("Datos iniciales cargados correctamente.");
    }

    // ─────────────────────────────────────────────────────────
    //  MENÚ SIN SESIÓN: opciones 1-7, salir con 0
    // ─────────────────────────────────────────────────────────

    /** Permite asignar el usuario logueado (uso en tests). */
    public void setUsuarioLogueado(Cliente cliente) {
        this.usuarioLogueado = cliente;
    }

    public Cliente getUsuarioLogueado() {
        return this.usuarioLogueado;
    }

    public Menu crearMenuSinLogin() {
        return new MenuBuilder("🌐 RED SOCIAL EMPRESARIAL")
            .setEstadoHeader("[ 🔴 Sin sesion iniciada ]")
            .agregarOpcion("1", "🔑 Iniciar Sesion",                   scanner -> login())
            .agregarOpcion("2", "🔍 Buscar Cliente por nombre",        scanner -> buscarClientePorNombre())
            .agregarOpcion("3", "📊 Buscar Cliente por Scoring",       scanner -> buscarClientePorScoring())
            .agregarOpcion("4", "📝 Registrarse",                      scanner -> agregarCliente())
            .agregarOpcion("5", "📋 Ver solicitudes pendientes",       () -> listarSolicitudesPendientes())
            .agregarOpcion("6", "📜 Ultimas 10 acciones",              () -> listarUltimasAcciones())
            .agregarOpcion("7", "👥 Ver todos los clientes",           () -> listarTodosLosClientes())
            .agregarOpcion("8", "🌳 Explorar mi red de contactos", scanner -> consultarRedConexiones())
            .setOpcionSalida("0")
            .setMensajeSalida("👋 Saliendo del sistema...")
            .setLimpiarConsola(false)
            .build();
    }

    // ─────────────────────────────────────────────────────────
    //  MENÚ CON SESIÓN: opciones 1-13, salir con 0
    // ─────────────────────────────────────────────────────────

    public Menu crearMenuConLogin() {
        String nombre = usuarioLogueado.getNombre();
        return new MenuBuilder("🌐 RED SOCIAL EMPRESARIAL")
            .setEstadoHeader("[ 🟢 Sesion activa: " + nombre + " ]")
            .agregarOpcion("1",  "🔍 Buscar Cliente por nombre",              scanner -> buscarClientePorNombre())
            .agregarOpcion("2",  "📊 Buscar Cliente por Scoring",             scanner -> buscarClientePorScoring())
            .agregarOpcion("3",  "➕ Seguir a un cliente",                    scanner -> seguirCliente())
            .agregarOpcion("4",  "✅ Aceptar solicitud de seguimiento",       () -> aceptarSolicitudSeguimiento())
            .agregarOpcion("5",  "❌ Rechazar solicitud de seguimiento",      () -> rechazarSolicitudSeguimiento())
            .agregarOpcion("6",  "↩️  Deshacer ultima accion",                () -> deshacerUltimaAccion())
            .agregarOpcion("7",  "📋 Mis solicitudes pendientes",             () -> listarSolicitudesPendientes())
            .agregarOpcion("8",  "📜 Ultimas 10 acciones",                    () -> listarUltimasAcciones())
            .agregarOpcion("9",  "👥 Ver todos los clientes",                () -> listarTodosLosClientes())
            .agregarOpcion("10", "👤 Ver mis datos",                          () -> verMisDatos())
            .agregarOpcion("11", "🌳 Explorar mi red de contactos",           scanner -> consultarRedConexiones())
            .agregarOpcion("12", "🚪 Cerrar Sesion",                        scanner -> logout())
            .setOpcionSalida("0")
            .setMensajeSalida("👋 Saliendo del sistema...")
            .setLimpiarConsola(false)
            .build();
    }

    /**
     * Ejecuta el sistema. El bucle principal determina qué menú mostrar según el estado de sesión.
     * Tras cada acción se re-evalúa (login/logout cambia el menú inmediatamente).
     */
    public void ejecutar() { // complejidad según uso
        boolean continuar = true;
        while (continuar) {
            Menu menu = (usuarioLogueado == null) ? crearMenuSinLogin() : crearMenuConLogin();
            continuar = menu.ejecutarUnaVez(scanner);
        }
        GuardadorClientesJson.guardar(gestorClientes);
        exportarAccionesCsv();
    }

    // ─────────────────────────────────────────────────────────
    //  ACCIONES DEL MENÚ
    // ─────────────────────────────────────────────────────────

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
            System.out.println("❌ No existe un cliente con ese nombre.");
        }
    }

    private void logout() {
        System.out.println("\n=== 🚪 Cerrar Sesion ===");

        historial.registrarAccion(new Accion("Logout", usuarioLogueado.getNombre()));
        System.out.println("✅ Sesion cerrada. Hasta luego, " + usuarioLogueado.getNombre() + "!");
        usuarioLogueado = null;
    }

    private void agregarCliente() { // complejidad O(1)
        System.out.println("\n=== 📝 Registrarse ===");
        String nombre = InputUtils.leerTextoNoVacio(scanner, "Ingrese su nombre: ");

        if (gestorClientes.buscarPorNombre(nombre) != null) {
            System.out.println("❌ Ya existe un cliente con el nombre: " + nombre);
            return;
        }

        int scoring = InputUtils.leerEnteroConReintentos(scanner, "Ingrese su scoring inicial: ");
        Cliente nuevoCliente = new Cliente(nombre, scoring);
        boolean agregado = gestorClientes.agregarCliente(nuevoCliente);

        if (agregado) {
            System.out.println("✅ Registro exitoso: " + nuevoCliente);
            historial.registrarAccion(new Accion("Registrarse", nombre));
        } else {
            System.out.println("❌ No se pudo completar el registro.");
        }
    }

    private void buscarClientePorNombre() { // complejidad O(1)
        System.out.println("\n=== 🔍 Buscar Cliente por nombre ===");
        String nombre = InputUtils.leerTexto(scanner, "Ingrese el nombre del cliente: ");
        Cliente cliente = gestorClientes.buscarPorNombre(nombre);

        if (cliente != null) {
            System.out.println("\n✅ Cliente encontrado: " + cliente.toString());
            historial.registrarAccion(new Accion("Buscar por nombre", nombre));
        } else {
            System.out.println("\n❌ No se encontro ningun cliente con el nombre: " + nombre);
        }
    }

    private void buscarClientePorScoring() { // complejidad O(log n + k)
        System.out.println("\n=== 📊 Buscar Cliente por Scoring ===");
        int scoring = InputUtils.leerEnteroConReintentos(scanner, "Ingrese el scoring: ");
        List<Cliente> clientes = gestorClientes.buscarPorScoring(scoring);

        if (clientes.isEmpty()) {
            System.out.println("\n❌ No se encontraron clientes con scoring: " + scoring);
        } else {
            System.out.println("\n✅ Clientes encontrados con scoring " + scoring + ":");
            for (Cliente cliente : clientes) {
                System.out.println("  - " + cliente);
            }
            historial.registrarAccion(new Accion("Buscar por scoring", String.valueOf(scoring)));
        }
    }

    private void seguirCliente() {
        System.out.println("\n=== ➕ Seguir a un cliente ===");
        String seguido = InputUtils.leerTexto(scanner, "Ingrese el nombre del cliente a seguir: ");

        if (gestorClientes.buscarPorNombre(seguido) == null) {
            System.out.println("❌ No existe ese cliente.");
            return;
        }

        SolicitudSeguimiento solicitud = new SolicitudSeguimiento(usuarioLogueado.getNombre(), seguido);
        colaSolicitudes.agregarSolicitud(solicitud);
        historial.registrarAccion(new Accion("Seguir cliente", usuarioLogueado.getNombre() + " -> " + seguido));
        System.out.println("✅ Solicitud enviada a " + seguido + ".");
    }

    /**
     * Muestra las solicitudes pendientes para el usuario logueado y permite elegir una para aceptar.
     * Aceptar aplica el seguimiento en el sistema.
     */
    private void aceptarSolicitudSeguimiento() { // complejidad O(n)
        System.out.println("\n=== ✅ Aceptar Solicitud de Seguimiento ===");
        SolicitudSeguimiento solicitud = seleccionarSolicitudParaUsuario("aceptar");
        if (solicitud == null) return;

        boolean quitada = colaSolicitudes.quitarSolicitud(solicitud);
        if (quitada) {
            boolean aplicada = gestorClientes.agregarSeguido(solicitud.getOrigen(), solicitud.getDestino());
            if (aplicada) {
                System.out.println("✅ " + solicitud.getOrigen() + " ahora te sigue.");
            } else {
                System.out.println("⚠️  No se pudo aplicar el seguimiento (ya existia o datos invalidos).");
            }
            historial.registrarAccion(new Accion("Aceptar solicitud", solicitud.getOrigen() + " -> " + solicitud.getDestino()));
        }
    }

    /**
     * Muestra las solicitudes pendientes para el usuario logueado y permite elegir una para rechazar.
     * Rechazar elimina la solicitud de la cola sin aplicar el seguimiento.
     */
    private void rechazarSolicitudSeguimiento() { // complejidad O(n)
        System.out.println("\n=== ❌ Rechazar Solicitud de Seguimiento ===");
        SolicitudSeguimiento solicitud = seleccionarSolicitudParaUsuario("rechazar");
        if (solicitud == null) return;

        boolean quitada = colaSolicitudes.quitarSolicitud(solicitud);
        if (quitada) {
            System.out.println("❌ Solicitud de " + solicitud.getOrigen() + " rechazada.");
            historial.registrarAccion(new Accion("Rechazar solicitud", solicitud.getOrigen() + " -> " + solicitud.getDestino()));
        }
    }

    /**
     * Muestra las solicitudes pendientes del usuario y pide que elija una.
     * Retorna null si no hay solicitudes o el usuario cancela.
     */
    private SolicitudSeguimiento seleccionarSolicitudParaUsuario(String verbo) {
        List<SolicitudSeguimiento> todas = colaSolicitudes.listarPendientes();
        List<SolicitudSeguimiento> pendientes = new ArrayList<>();
        for (SolicitudSeguimiento s : todas) {
            if (s.getDestino().equals(usuarioLogueado.getNombre())) {
                pendientes.add(s);
            }
        }

        if (pendientes.isEmpty()) {
            System.out.println("📭 No tienes solicitudes de seguimiento pendientes.");
            return null;
        }

        System.out.println("Solicitudes pendientes para ti:");
        for (int i = 0; i < pendientes.size(); i++) {
            System.out.println("  " + (i + 1) + ". 👤 " + pendientes.get(i).getOrigen() + " quiere seguirte");
        }

        int numero = InputUtils.leerEnteroConReintentos(scanner,
                "Selecciona el numero de solicitud a " + verbo + " (0 para cancelar): ");

        if (numero == 0) {
            System.out.println("Operacion cancelada.");
            return null;
        }
        if (numero < 1 || numero > pendientes.size()) {
            System.out.println("❌ Numero invalido.");
            return null;
        }
        return pendientes.get(numero - 1);
    }

    private void deshacerUltimaAccion() { // complejidad O(1) o O(n) segun tipo
        System.out.println("\n=== ↩️ Deshacer Ultima Accion ===");
        Accion accionDeshecha = historial.deshacerUltimaAccion();

        if (accionDeshecha == null) {
            System.out.println("📭 No hay acciones para deshacer.");
            return;
        }

        String tipo = accionDeshecha.getTipo();
        String detalle = accionDeshecha.getDetalle();
        boolean impacto = false;

        if ("Registrarse".equals(tipo) || "Agregar cliente".equals(tipo)) {
            impacto = gestorClientes.eliminarCliente(detalle);
            if (impacto) System.out.println("🗑️  Cliente \"" + detalle + "\" eliminado del sistema.");

        } else if (("Aceptar solicitud".equals(tipo) || "Procesar solicitud".equals(tipo))
                && detalle != null && detalle.contains(" -> ")) {
            String[] partes = detalle.split(" -> ", 2);
            if (partes.length == 2) {
                impacto = gestorClientes.quitarSeguido(partes[0].trim(), partes[1].trim());
                if (impacto) System.out.println("↩️  Seguimiento " + detalle + " revertido.");
            }

        } else if ("Rechazar solicitud".equals(tipo) && detalle != null && detalle.contains(" -> ")) {
            String[] partes = detalle.split(" -> ", 2);
            if (partes.length == 2) {
                colaSolicitudes.agregarSolicitud(new SolicitudSeguimiento(partes[0].trim(), partes[1].trim()));
                System.out.println("↩️  Solicitud " + detalle + " restaurada a la cola.");
                impacto = true;
            }

        } else if ("Seguir cliente".equals(tipo) && detalle != null && detalle.contains(" -> ")) {
            String[] partes = detalle.split(" -> ", 2);
            if (partes.length == 2) {
                impacto = colaSolicitudes.quitarSolicitud(new SolicitudSeguimiento(partes[0].trim(), partes[1].trim()));
                if (impacto) System.out.println("↩️  Solicitud " + detalle + " quitada de la cola.");
            }

        } else {
            System.out.println("↩️  Accion deshecha del historial: [" + tipo + "] " + detalle);
            impacto = true;
        }

        if (!impacto) {
            System.out.println("⚠️  Accion removida del historial pero no habia datos que revertir: " + accionDeshecha);
        }
    }

    private void listarSolicitudesPendientes() { // complejidad O(n)
        System.out.println("\n=== 📋 Solicitudes Pendientes ===");

        List<SolicitudSeguimiento> pendientes;

        if (usuarioLogueado != null) {
            System.out.println("(Solo solicitudes que involucran a: " + usuarioLogueado.getNombre() + ")");
            pendientes = colaSolicitudes.listarPendientesParaUsuario(usuarioLogueado.getNombre());
        } else {
            pendientes = colaSolicitudes.listarPendientes();
        }

        if (pendientes.isEmpty()) {
            System.out.println("📭 No hay solicitudes pendientes.");
        } else {
            System.out.println("Total: " + pendientes.size() + " solicitud(es):");
            int i = 1;
            for (SolicitudSeguimiento s : pendientes) {
                System.out.println("  " + i + ". " + s.getOrigen() + " ➡️  " + s.getDestino());
                i++;
            }
        }
    }

    private void listarUltimasAcciones() { // complejidad O(n)
        System.out.println("\n=== 📜 Ultimas 10 Acciones ===");
        List<Accion> ultimas = historial.listarUltimas(10);
        if (ultimas.isEmpty()) {
            System.out.println("📭 No hay acciones registradas.");
        } else {
            System.out.println("Mostrando " + ultimas.size() + " accion(es) (mas reciente primero):");
            int i = 1;
            for (Accion a : ultimas) {
                System.out.println("  " + i + ". [" + a.getTipo() + "] " + a.getDetalle() + " (" + a.getFechaHora() + ")");
                i++;
            }
        }
    }

    private void listarTodosLosClientes() { // complejidad O(n)
        System.out.println("\n=== 👥 Lista de Clientes ===");
        List<Cliente> clientes = gestorClientes.listarClientes();
        if (clientes.isEmpty()) {
            System.out.println("📭 No hay clientes registrados.");
        } else {
            System.out.println("Total: " + clientes.size() + " cliente(s):");
            for (Cliente c : clientes) {
                System.out.println("  - " + c);
            }
        }
        historial.registrarAccion(new Accion("Listar clientes", "total=" + clientes.size()));
    }

    private void verMisDatos() { // complejidad O(1)
        System.out.println("\n=== 👤 Mis Datos ===");
        System.out.println("Nombre:      " + usuarioLogueado.getNombre());
        System.out.println("Scoring:     " + usuarioLogueado.getScoring());
        System.out.println("Siguiendo  (" + usuarioLogueado.getSiguiendo().size() + "): "
                + (usuarioLogueado.getSiguiendo().isEmpty() ? "(ninguno)" : usuarioLogueado.getSiguiendo()));
        System.out.println("Conexiones (" + usuarioLogueado.getConexiones().size() + "): "
                + (usuarioLogueado.getConexiones().isEmpty() ? "(ninguna)" : usuarioLogueado.getConexiones()));
    }

    private void consultarRedConexiones() {
        System.out.println("\n=== 🌳 Explorar Red de Contactos ===");
        String nombre = InputUtils.leerTextoNoVacio(scanner, "Ingrese el nombre del usuario: ");

        Cliente cliente = gestorClientes.buscarPorNombre(nombre);
        if (cliente == null) {
            System.out.println("❌ No existe un usuario con ese nombre.");
            return;
        }

        System.out.println("\n📊 Red de " + nombre + ":");
        System.out.println("   Siguiendo: " + (cliente.getSiguiendo().isEmpty() ? "(nadie)" : cliente.getSiguiendo()));

        List<Integer> scoringsNivel4 = gestorClientes.consultarConexionesNivel4(nombre);

        if (scoringsNivel4.isEmpty()) {
            System.out.println("\n🌳 " + nombre + " no tiene contactos lejanos suficientes en su red.");
            System.out.println("   (Necesita más conexiones para descubrir personas distantes)");
        } else {
            System.out.println("\n🌳 Personas descubiertas en tu red extendida:");
            for (int scoring : scoringsNivel4) {
                List<Cliente> clientes = gestorClientes.buscarPorScoring(scoring);
                for (Cliente c : clientes) {
                    System.out.println("     - " + c.getNombre() + " (puntuación: " + scoring + ")");
                }
            }
        }
    }

    private void exportarAccionesCsv() { // complejidad O(n)
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
