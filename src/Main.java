import java.util.LinkedList;

import logica.horario.Asignacion;
import logica.horario.Semestre;
import logica.tiempo.BloqueTiempo;
import modelo.academico.Asignatura;
import modelo.academico.AsignaturaPractica;
import modelo.academico.AsignaturaTeorica;
import modelo.academico.Departamento;
import modelo.infraestructura.Aula;
import modelo.infraestructura.AulaLab;
import modelo.infraestructura.AulaTeoria;
import modelo.usuarios.Coordinador;
import modelo.usuarios.Estudiante;
import modelo.usuarios.Profesor;
import modelo.usuarios.Usuario;
import logica.horario.GeneradorHorario;
import java.util.Scanner;

/**
 * Clase principal del sistema Administra-Horarios IC-CTLSC.
 * Menu interactivo en consola con login por rol.
 * Datos de los tres escenarios quemados, se cargan al iniciar.
 */
public class Main {

    static LinkedList<Usuario>    todosUsuarios  = new LinkedList<>();
    static LinkedList<Semestre>   todosSemestres = new LinkedList<>();
    static LinkedList<String[]>   credenciales   = new LinkedList<>();

    static LinkedList<Aula>       aulasE1, aulasE2, aulasE3;
    static LinkedList<Profesor>   profsE1, profsE2, profsE3;
    static LinkedList<Asignatura> asigsE1, asigsE2, asigsE3;

    public static void main(String[] args) {
        cargarEscenarios();

        Scanner sc = new Scanner(System.in);
        System.out.println("============================================================");
        System.out.println("       SISTEMA ADMINISTRA-HORARIOS IC-CTLSC");
        System.out.println("============================================================");

        boolean salir = false;
        while (!salir) {
            System.out.println("\n  1. Iniciar sesion");
            System.out.println("  2. Ver usuarios disponibles");
            System.out.println("  0. Salir");
            System.out.print("Opcion: ");
            String op = sc.nextLine().trim();
            switch (op) {
                case "1" -> menuLogin(sc);
                case "2" -> mostrarUsuarios();
                case "0" -> salir = true;
                default  -> System.out.println("Opcion no valida.");
            }
        }
        System.out.println("\nSistema cerrado.");
        sc.close();
    }

    // =========================================================================
    // LOGIN
    // =========================================================================
    static void menuLogin(Scanner sc) {
        System.out.println("\n--- INICIO DE SESION ---");
        System.out.print("Login     : ");
        String login = sc.nextLine().trim();
        System.out.print("Contrasena: ");
        String pass  = sc.nextLine().trim();

        Usuario usuario = buscarUsuario(login, pass);

        if (usuario == null) {
            System.out.println("Credenciales incorrectas.");
            return;
        }

        usuario.iniciarSesion();

        if      (usuario instanceof Coordinador c) menuCoordinador(sc, c);
        else if (usuario instanceof Profesor    p) menuProfesor(sc, p);
        else if (usuario instanceof Estudiante  e) menuEstudiante(sc, e);
    }

    // =========================================================================
    // MENU COORDINADOR
    // =========================================================================
    static void menuCoordinador(Scanner sc, Coordinador coord) {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- MENU COORDINADOR: " + coord.getNombre() + " ---");
            System.out.println("  1. Generar horario de un semestre");
            System.out.println("  2. Consultar horario de un semestre");
            System.out.println("  3. Ajustar horario manualmente");
            System.out.println("  0. Cerrar sesion");
            System.out.print("Opcion: ");
            String op = sc.nextLine().trim();
            switch (op) {
                case "1" -> generarHorarioMenu(sc, coord);
                case "2" -> consultarMenu(sc, null);
                case "3" -> ajustarHorarioMenu(sc, coord);
                case "0" -> volver = true;
                default  -> System.out.println("Opcion no valida.");
            }
        }
    }

    static void generarHorarioMenu(Scanner sc, Coordinador coord) {
        Semestre sem = seleccionarSemestre(sc);
        if (sem == null) return;
        int num = sem.getNumero();
        sem.getAsignaciones().clear();
        coord.generarHorario(sem, getAulas(num), getProfesores(num), getAsignaturas(num));
    }

    static void ajustarHorarioMenu(Scanner sc, Coordinador coord) {
        Semestre sem = seleccionarSemestre(sc);
        if (sem == null) return;

        if (sem.getAsignaciones().isEmpty()) {
            System.out.println("No hay horario generado. Generelo primero.");
            return;
        }

        imprimirTabla(sem, null);

        System.out.print("\nNumero de asignacion a ajustar: ");
        int idx;
        try { idx = Integer.parseInt(sc.nextLine().trim()) - 1; }
        catch (NumberFormatException e) { System.out.println("Entrada no valida."); return; }

        LinkedList<Asignacion> lista = sem.getAsignaciones();
        if (idx < 0 || idx >= lista.size()) {
            System.out.println("Numero fuera de rango.");
            return;
        }

        Asignacion asigActual = (Asignacion) lista.toArray()[idx];
        Asignatura curso  = asigActual.getCurso();
        Profesor   profe  = asigActual.getProfesor();

        System.out.println("Ajustando: " + curso.getNombre());
        String dia = leerDia(sc);
        String inicio = leerHora(sc, "  Hora inicio (ej: 09:00): ");
        String fin    = leerHoraFin(sc, inicio);
        System.out.println("  Hora fin               : " + fin);

        LinkedList<Aula> aulas = getAulas(sem.getNumero());
        System.out.println("\nAulas disponibles:");
        int j = 1;
        for (Aula a : aulas) {
            System.out.println("  " + j + ". " + a.getNombre() + " (" + a.obtenerTipo() + ")");
            j++;
        }
        System.out.print("Numero de aula: ");
        int idxAula;
        try { idxAula = Integer.parseInt(sc.nextLine().trim()) - 1; }
        catch (NumberFormatException e) { System.out.println("Entrada no valida."); return; }
        if (idxAula < 0 || idxAula >= aulas.size()) {
            System.out.println("Numero fuera de rango.");
            return;
        }

        Aula nuevaAula = (Aula) aulas.toArray()[idxAula];

        System.out.print("Razon del ajuste: ");
        String razon = sc.nextLine().trim();

        BloqueTiempo nuevoBloque = new BloqueTiempo(dia, inicio, fin);
        Asignacion   ajustada   = new Asignacion(nuevoBloque, nuevaAula, profe, curso);
        coord.ajustarHorario(sem, ajustada);
        System.out.println("Razon registrada: " + razon);

        // Revalidar choques con el horario ya ajustado
        System.out.println("\nValidando choques tras ajuste:");
        GeneradorHorario validador = new GeneradorHorario(
                sem.getAsignaciones(), getAulas(sem.getNumero()),
                getProfesores(sem.getNumero()), getAsignaturas(sem.getNumero())
        );
        validador.validarChoques();

        System.out.println("\nHorario tras ajuste:");
        imprimirTabla(sem, null);
    }

    // =========================================================================
    // MENU PROFESOR
    // =========================================================================
    static void menuProfesor(Scanner sc, Profesor prof) {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- MENU PROFESOR: " + prof.getNombre() + " ---");
            System.out.println("  1. Ver mi disponibilidad y cursos");
            System.out.println("  2. Consultar mi horario en un semestre");
            System.out.println("  0. Cerrar sesion");
            System.out.print("Opcion: ");
            String op = sc.nextLine().trim();
            switch (op) {
                case "1" -> {
                    System.out.println("\nDisponibilidad registrada:");
                    if (prof.getDisponibilidad().isEmpty()) {
                        System.out.println("  Sin disponibilidad.");
                    } else {
                        for (BloqueTiempo b : prof.getDisponibilidad())
                            System.out.println("  " + b);
                    }
                    System.out.println("Cursos que imparte:");
                    for (Asignatura a : prof.getCursoImpartido())
                        System.out.println("  - " + a.getNombre());
                }
                case "2" -> consultarMenu(sc, prof);
                case "0" -> volver = true;
                default  -> System.out.println("Opcion no valida.");
            }
        }
    }

    // =========================================================================
    // MENU ESTUDIANTE
    // =========================================================================
    static void menuEstudiante(Scanner sc, Estudiante estud) {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- MENU ESTUDIANTE: " + estud.getNombre() + " ---");
            System.out.println("  1. Consultar horario de un semestre");
            System.out.println("  0. Cerrar sesion");
            System.out.print("Opcion: ");
            String op = sc.nextLine().trim();
            switch (op) {
                case "1" -> consultarMenu(sc, null);
                case "0" -> volver = true;
                default  -> System.out.println("Opcion no valida.");
            }
        }
    }

    // =========================================================================
    // CONSULTA COMPARTIDA
    // prof != null: filtra solo sus asignaciones
    // prof == null: muestra el semestre completo
    // =========================================================================
    static void consultarMenu(Scanner sc, Profesor prof) {
        Semestre sem = seleccionarSemestre(sc);
        if (sem == null) return;

        if (sem.getAsignaciones().isEmpty()) {
            System.out.println("Horario no disponible para el semestre " + sem.getNumero() + ".");
            return;
        }
        imprimirTabla(sem, prof);
    }

    // =========================================================================
    // TABLA EN CONSOLA
    // Si aula == null muestra "Sin aula (choque)"
    // =========================================================================
    static void imprimirTabla(Semestre sem, Profesor filtroProf) {
        String fmt = "%-3s | %-28s | %-9s | %-14s | %-16s | %-12s | %s%n";
        String sep = "-".repeat(105);

        System.out.println("\nHorario - Semestre " + sem.getNumero());
        System.out.println(sep);
        System.out.printf(fmt, "#", "Asignatura", "Tipo", "Aula", "Profesor", "Dia", "Bloque");
        System.out.println(sep);

        int i = 1;
        for (Asignacion a : sem.getAsignaciones()) {
            if (filtroProf != null && a.getProfesor() != filtroProf) {
                i++;
                continue;
            }
            String aulaStr = (a.getAula() != null)
                    ? a.getAula().getNombre()
                    : "Sin aula (choque)";
            System.out.printf(fmt,
                    i,
                    a.getCurso().getNombre(),
                    a.getCurso().obtenerTipo(),
                    aulaStr,
                    a.getProfesor().getNombre(),
                    a.getTiempo().getDiaSemana(),
                    a.getTiempo().getHorarioInicio() + "-" + a.getTiempo().getHoraFin()
            );
            i++;
        }
        System.out.println(sep);
    }

    // =========================================================================
    // HELPERS
    // =========================================================================
    static Semestre seleccionarSemestre(Scanner sc) {
        System.out.println("\nSemestres disponibles:");
        for (Semestre s : todosSemestres) {
            String estado = s.getAsignaciones().isEmpty() ? "[sin generar]" : "[generado]";
            System.out.println("  " + s.getNumero() + ". Semestre " + s.getNumero() + " " + estado);
        }
        System.out.print("Numero de semestre: ");
        try {
            int num = Integer.parseInt(sc.nextLine().trim());
            for (Semestre s : todosSemestres)
                if (s.getNumero() == num) return s;
            System.out.println("Semestre no encontrado.");
        } catch (NumberFormatException e) {
            System.out.println("Entrada no valida.");
        }
        return null;
    }

    static Usuario buscarUsuario(String login, String pass) {
        for (Usuario u : todosUsuarios) {
            if (u.getLogin().equals(login)) {
                String stored = passwordDe(login);
                if (stored != null && stored.equals(pass)) return u;
            }
        }
        return null;
    }

    static String passwordDe(String login) {
        for (String[] par : credenciales)
            if (par[0].equals(login)) return par[1];
        return null;
    }

    static void registrar(Usuario u, String pass) {
        todosUsuarios.add(u);
        credenciales.add(new String[]{u.getLogin(), pass});
    }

    static void mostrarUsuarios() {
        System.out.println("\nUsuarios de prueba:");
        System.out.printf("%-30s | %-12s | %s%n", "Login", "Contrasena", "Rol");
        System.out.println("-".repeat(60));
        for (String[] c : credenciales) {
            Usuario u = null;
            for (Usuario x : todosUsuarios)
                if (x.getLogin().equals(c[0])) { u = x; break; }
            if (u != null) {
                String rol = u instanceof Coordinador ? "Coordinador"
                        : u instanceof Profesor    ? "Profesor"
                        :                            "Estudiante";
                System.out.printf("%-30s | %-12s | %s%n", c[0], c[1], rol);
            }
        }
    }

    static LinkedList<Aula>       getAulas(int n)       { return n==1?aulasE1:n==3?aulasE2:aulasE3; }
    static LinkedList<Profesor>   getProfesores(int n)  { return n==1?profsE1:n==3?profsE2:profsE3; }
    static LinkedList<Asignatura> getAsignaturas(int n) { return n==1?asigsE1:n==3?asigsE2:asigsE3; }

    // =========================================================================
    // CARGA DE ESCENARIOS (datos quemados)
    // =========================================================================
    static void cargarEscenarios() {

        // ── ESCENARIO 1 — Semestre I ──────────────────────────────────────────
        Departamento deptoCiencias = new Departamento("Ciencias de la Computacion");
        Departamento deptoMate     = new Departamento("Matematica");

        aulasE1 = new LinkedList<>();
        aulasE1.add(new AulaTeoria("Aula T-101", 101, "Edificio A", 30, true, true));
        aulasE1.add(new AulaLab("Lab L-201", 201, "Edificio B", 20, "Computadoras HP", 20));

        AsignaturaTeorica progI = new AsignaturaTeorica(
                "Programacion I", 4, true, "http://apuntes.tec.ac.cr/progI");
        LinkedList<String> mat1 = new LinkedList<>();
        mat1.add("NetBeans");
        mat1.add("Guia de laboratorio");
        AsignaturaPractica labProgI = new AsignaturaPractica(
                "Lab. Programacion I", 1, true, mat1, "Windows 10");

        asigsE1 = new LinkedList<>();
        asigsE1.add(progI);
        asigsE1.add(labProgI);

        Profesor profJuan = new Profesor("Juan Perez", "112233445",
                "prof1", "prof1", deptoCiencias);
        profJuan.registrarDisponibilidad("Lunes",     "07:00", "07:50");
        profJuan.registrarDisponibilidad("Miercoles", "07:00", "07:50");
        profJuan.setCursoImpartido(progI);

        Profesor profMaria = new Profesor("Maria Solis", "556677889",
                "prof2", "prof2", deptoMate);
        profMaria.registrarDisponibilidad("Martes", "08:00", "08:50");
        profMaria.setCursoImpartido(labProgI);

        profsE1 = new LinkedList<>();
        profsE1.add(profJuan);
        profsE1.add(profMaria);

        todosSemestres.add(new Semestre(1));

        registrar(new Coordinador("Ana Vargas",  "100200300", "coord1", "coord1"), "coord1");
        registrar(new Estudiante("Carlos Mora",  "200300400", "estud1", "estud1"), "estud1");
        registrar(profJuan,  "prof1");
        registrar(profMaria, "prof2");

        // ── ESCENARIO 2 — Semestre III ────────────────────────────────────────
        Departamento deptoSistemas = new Departamento("Sistemas de Informacion");

        aulasE2 = new LinkedList<>();
        aulasE2.add(new AulaTeoria("Aula T-305", 305, "Edificio C", 35, false, true));
        aulasE2.add(new AulaTeoria("Aula T-306", 306, "Edificio C", 35, true,  false));
        aulasE2.add(new AulaLab("Lab L-401", 401, "Edificio D", 25, "Raspberry Pi", 25));

        AsignaturaTeorica estructuras = new AsignaturaTeorica(
                "Estructuras de Datos", 3, true, "http://apuntes.tec.ac.cr/ed");
        AsignaturaTeorica discreta = new AsignaturaTeorica(
                "Matematica Discreta", 3, false, "http://mate.tec.ac.cr/discreta");
        LinkedList<String> mat2 = new LinkedList<>();
        mat2.add("Eclipse IDE");
        mat2.add("Manual de laboratorio ED");
        AsignaturaPractica labED = new AsignaturaPractica(
                "Lab. Estructuras", 1, true, mat2, "Linux Ubuntu");

        asigsE2 = new LinkedList<>();
        asigsE2.add(estructuras);
        asigsE2.add(discreta);
        asigsE2.add(labED);

        Profesor profLuis = new Profesor("Luis Quesada", "334455667",
                "prof3", "prof3", deptoSistemas);
        profLuis.registrarDisponibilidad("Lunes", "09:00", "09:50");
        profLuis.setCursoImpartido(estructuras);
        profLuis.setCursoImpartido(discreta);

        Profesor profSofia = new Profesor("Sofia Castro", "778899001",
                "prof4", "prof4", deptoSistemas);
        profSofia.registrarDisponibilidad("Jueves", "10:00", "10:50");
        profSofia.setCursoImpartido(labED);

        profsE2 = new LinkedList<>();
        profsE2.add(profLuis);
        profsE2.add(profSofia);

        todosSemestres.add(new Semestre(3));

        registrar(new Coordinador("Pedro Jimenez", "400500600", "coord2", "coord2"), "coord2");
        registrar(new Estudiante("Laura Rojas",    "500600700", "estud2", "estud2"), "estud2");
        registrar(profLuis,  "prof3");
        registrar(profSofia, "prof4");

        // ── ESCENARIO 3 — Semestre V ──────────────────────────────────────────
        Departamento deptoRedes = new Departamento("Redes y Telecomunicaciones");
        Departamento deptoSO    = new Departamento("Sistemas Operativos");

        aulasE3 = new LinkedList<>();
        aulasE3.add(new AulaTeoria("Aula T-501", 501, "Edificio E", 40, true, true));
        aulasE3.add(new AulaTeoria("Aula T-502", 502, "Edificio E", 40, true, true));
        aulasE3.add(new AulaLab("Lab L-601", 601, "Edificio F", 30, "Servidores Dell", 15));

        AsignaturaTeorica redes = new AsignaturaTeorica(
                "Redes de Computadoras", 4, true, "http://apuntes.tec.ac.cr/redes");
        AsignaturaTeorica so = new AsignaturaTeorica(
                "Sistemas Operativos", 4, true, "http://apuntes.tec.ac.cr/so");
        LinkedList<String> matRedes = new LinkedList<>();
        matRedes.add("Wireshark");
        matRedes.add("Packet Tracer");
        AsignaturaPractica labRedes = new AsignaturaPractica(
                "Lab. Redes", 1, true, matRedes, "Linux Debian");

        asigsE3 = new LinkedList<>();
        asigsE3.add(redes);
        asigsE3.add(so);
        asigsE3.add(labRedes);

        Profesor profAndres = new Profesor("Andres Mora", "112244668",
                "prof5", "prof5", deptoRedes);
        profAndres.registrarDisponibilidad("Lunes",     "12:30", "13:20");
        profAndres.registrarDisponibilidad("Miercoles", "12:30", "13:20");
        profAndres.setCursoImpartido(redes);

        Profesor profValeria = new Profesor("Valeria Nunez", "223355779",
                "prof6", "prof6", deptoSO);
        profValeria.registrarDisponibilidad("Martes", "14:00", "14:50");
        profValeria.registrarDisponibilidad("Jueves", "14:00", "14:50");
        profValeria.setCursoImpartido(so);

        Profesor profRicardo = new Profesor("Ricardo Ulate", "334466880",
                "prof7", "prof7", deptoRedes);
        profRicardo.registrarDisponibilidad("Viernes", "07:00", "07:50");
        profRicardo.setCursoImpartido(labRedes);

        profsE3 = new LinkedList<>();
        profsE3.add(profAndres);
        profsE3.add(profValeria);
        profsE3.add(profRicardo);

        todosSemestres.add(new Semestre(5));

        registrar(new Coordinador("Diana Brenes",  "700800900", "coord3", "coord3"), "coord3");
        registrar(new Estudiante("Miguel Torres",  "800900100", "estud3", "estud3"), "estud3");
        registrar(profAndres,  "prof5");
        registrar(profValeria, "prof6");
        registrar(profRicardo, "prof7");

        System.out.println("Datos cargados. Sistema listo.\n");
    }

    static final java.util.List<String> DIAS_VALIDOS = java.util.Arrays.asList(
            "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado"
    );

    static String leerDia(Scanner sc) {
        while (true) {
            System.out.print("  Dia (Lunes-Sabado)     : ");
            String input = sc.nextLine().trim();
            for (String d : DIAS_VALIDOS) {
                if (d.equalsIgnoreCase(input)) return d;
            }
            System.out.println("  Dia invalido. Opciones: Lunes, Martes, Miercoles, Jueves, Viernes, Sabado");
        }
    }

    // Pide una hora HH:mm en loop hasta que el formato sea valido
    static String leerHora(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (input.matches("\\d{2}:\\d{2}")) {
                try { aMinutos(input); return input; }
                catch (Exception ignored) {}
            }
            System.out.println("  Formato invalido. Use HH:mm (ej: 09:00)");
        }
    }
    //co
    // Pide hora fin: Enter = automatico, formato invalido = reintento,
    // menor a inicio+50 = reintento
    static String leerHoraFin(Scanner sc, String inicio) {
        while (true) {
            System.out.print("  Hora fin (Enter = auto): ");
            String input = sc.nextLine().trim();
            if (input.isEmpty()) return calcularHoraFin(inicio);
            if (!input.matches("\\d{2}:\\d{2}")) {
                System.out.println("  Formato invalido. Use HH:mm o presione Enter para automatico.");
                continue;
            }
            try {
                int minInicio = aMinutos(inicio);
                int minFin    = aMinutos(input);
                if (minFin - minInicio >= 50) return input;
                System.out.println("  La hora fin debe ser al menos 50 minutos despues del inicio.");
            } catch (Exception e) {
                System.out.println("  Formato invalido. Use HH:mm o presione Enter para automatico.");
            }
        }
    }

    // Convierte HH:mm a minutos totales
    static int aMinutos(String hora) {
        String[] p = hora.split(":");
        return Integer.parseInt(p[0]) * 60 + Integer.parseInt(p[1]);
    }

    // Suma 50 minutos a una hora en formato HH:mm
    static String calcularHoraFin(String inicio) {
        try {
            int total = aMinutos(inicio) + 50;
            return String.format("%02d:%02d", total / 60, total % 60);
        } catch (Exception e) { return "00:00"; }
    }

    // Si el usuario ingresa una hora fin valida y >= inicio+50min, la respeta.
    // De lo contrario calcula automatico.
    static String resolverHoraFin(String inicio, String finInput) {
        if (finInput.isEmpty()) return calcularHoraFin(inicio);
        try {
            int minInicio = aMinutos(inicio);
            int minFin    = aMinutos(finInput);
            if (minFin - minInicio >= 50) return finInput;
            System.out.println("  (Hora fin invalida o menor a 50 min, se calcula automatico)");
            return calcularHoraFin(inicio);
        } catch (Exception e) {
            System.out.println("  (Formato invalido, se calcula automatico)");
            return calcularHoraFin(inicio);
        }
    }
}