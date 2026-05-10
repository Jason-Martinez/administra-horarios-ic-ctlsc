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

/**
 * Clase principal del sistema Administra-Horarios IC-CTLSC.
 * Contiene tres escenarios con datos quemados que demuestran la generación
 * automática de horarios, detección de choques y consulta por rol de usuario.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("   SISTEMA ADMINISTRA-HORARIOS IC-CTLSC");
        System.out.println("============================================================\n");

        escenario1();
        escenario2();
        escenario3();
    }

    // =========================================================================
    // ESCENARIO 1 — Semestre I: generación sin choques
    // =========================================================================
    static void escenario1() {
        System.out.println("************************************************************");
        System.out.println("   ESCENARIO 1 — Semestre I: generación sin choques");
        System.out.println("************************************************************\n");

        // --- Departamentos ---
        Departamento deptoCiencias = new Departamento("Ciencias de la Computación");
        Departamento deptoMate     = new Departamento("Matemática");

        // --- Aulas ---
        AulaTeoria aulaT1 = new AulaTeoria("Aula T-101", 101, "Edificio A", 30, true, true);
        AulaLab    aulaL1 = new AulaLab("Lab L-201", 201, "Edificio B", 20, "Computadoras HP", 20);

        LinkedList<Aula> aulas = new LinkedList<>();
        aulas.add(aulaT1);
        aulas.add(aulaL1);

        // --- Asignaturas ---
        AsignaturaTeorica progI = new AsignaturaTeorica(
                "Programación I", 4, true, "http://apuntes.tec.ac.cr/progI");

        LinkedList<String> materiales = new LinkedList<>();
        materiales.add("NetBeans");
        materiales.add("Guía de laboratorio");
        AsignaturaPractica labProgI = new AsignaturaPractica(
                "Laboratorio de Programación I", 1, true, materiales, "Windows 10");

        LinkedList<Asignatura> asignaturas = new LinkedList<>();
        asignaturas.add(progI);
        asignaturas.add(labProgI);

        // --- Profesores ---
        Profesor profJuan = new Profesor("Juan Pérez", "112233445", "profesor_112233445", "pass1", deptoCiencias);
        profJuan.registrarDisponibilidad("Lunes",     "07:00", "07:50");
        profJuan.registrarDisponibilidad("Miércoles", "07:00", "07:50");
        profJuan.setCursoImpartido(progI);

        Profesor profMaria = new Profesor("María Solís", "556677889", "profesor_556677889", "pass2", deptoMate);
        profMaria.registrarDisponibilidad("Martes", "08:00", "08:50");
        profMaria.setCursoImpartido(labProgI);

        LinkedList<Profesor> profesores = new LinkedList<>();
        profesores.add(profJuan);
        profesores.add(profMaria);

        // --- Usuarios ---
        Coordinador coord1 = new Coordinador("Ana Vargas",   "100200300", "coordinador_100200300", "coord1");
        Estudiante  estud1 = new Estudiante("Carlos Mora",   "200300400", "estudiante_2024",       "estud1");

        // --- Semestre ---
        Semestre semestre1 = new Semestre(1);

        // Login y generación
        System.out.println("--- Login Coordinador ---");
        coord1.iniciarSesion();
        System.out.println();

        coord1.generarHorario(semestre1, aulas, profesores, asignaturas);
        System.out.println();

        System.out.println("--- Consulta Coordinador ---");
        coord1.consultarHorario(semestre1);
        System.out.println();

        System.out.println("--- Login Estudiante ---");
        estud1.iniciarSesion();
        System.out.println();

        System.out.println("--- Consulta Estudiante ---");
        estud1.consultarHorario(semestre1);
        System.out.println();

        System.out.println("--- Login Profesor ---");
        profJuan.iniciarSesion();
        System.out.println();

        System.out.println("--- Consulta Profesor Juan Pérez ---");
        profJuan.consultarHorario(semestre1);
        System.out.println();
    }

    // =========================================================================
    // ESCENARIO 2 — Semestre III: choque de profesor y ajuste manual
    // =========================================================================
    static void escenario2() {
        System.out.println("************************************************************");
        System.out.println("   ESCENARIO 2 — Semestre III: ajuste manual de horario");
        System.out.println("************************************************************\n");

        // --- Departamentos ---
        Departamento deptoSistemas = new Departamento("Sistemas de Información");

        // --- Aulas ---
        AulaTeoria aulaT2 = new AulaTeoria("Aula T-305", 305, "Edificio C", 35, false, true);
        AulaTeoria aulaT3 = new AulaTeoria("Aula T-306", 306, "Edificio C", 35, true,  false);
        AulaLab    aulaL2 = new AulaLab("Lab L-401", 401, "Edificio D", 25, "Raspberry Pi", 25);

        LinkedList<Aula> aulas = new LinkedList<>();
        aulas.add(aulaT2);
        aulas.add(aulaT3);
        aulas.add(aulaL2);

        // --- Asignaturas ---
        AsignaturaTeorica estructuras = new AsignaturaTeorica(
                "Estructuras de Datos", 3, true, "http://apuntes.tec.ac.cr/ed");
        AsignaturaTeorica discreta = new AsignaturaTeorica(
                "Matemática Discreta", 3, false, "http://mate.tec.ac.cr/discreta");

        LinkedList<String> mat2 = new LinkedList<>();
        mat2.add("Eclipse IDE");
        mat2.add("Manual de laboratorio ED");
        AsignaturaPractica labED = new AsignaturaPractica(
                "Laboratorio de Estructuras", 1, true, mat2, "Linux Ubuntu");

        LinkedList<Asignatura> asignaturas = new LinkedList<>();
        asignaturas.add(estructuras);
        asignaturas.add(discreta);
        asignaturas.add(labED);

        // --- Profesores ---
        // profLuis imparte dos cursos con el mismo bloque → choque intencional
        Profesor profLuis = new Profesor("Luis Quesada", "334455667", "profesor_334455667", "pass3", deptoSistemas);
        profLuis.registrarDisponibilidad("Lunes", "09:00", "09:50");
        profLuis.setCursoImpartido(estructuras);
        profLuis.setCursoImpartido(discreta);

        Profesor profSofia = new Profesor("Sofía Castro", "778899001", "profesor_778899001", "pass4", deptoSistemas);
        profSofia.registrarDisponibilidad("Jueves", "10:00", "10:50");
        profSofia.setCursoImpartido(labED);

        LinkedList<Profesor> profesores = new LinkedList<>();
        profesores.add(profLuis);
        profesores.add(profSofia);

        // --- Usuarios ---
        Coordinador coord2 = new Coordinador("Pedro Jiménez", "400500600", "coordinador_400500600", "coord2");
        Estudiante  estud2 = new Estudiante("Laura Rojas",    "500600700", "estudiante_2023",       "estud2");

        // --- Semestre ---
        Semestre semestre3 = new Semestre(3);

        System.out.println("--- Login Coordinador ---");
        coord2.iniciarSesion();
        System.out.println();

        System.out.println("--- Generación automática (detectará choque de profesor) ---");
        coord2.generarHorario(semestre3, aulas, profesores, asignaturas);
        System.out.println();

        // Ajuste manual: mover Matemática Discreta al Martes para resolver el choque
        System.out.println("--- Ajuste manual: reasignando Matemática Discreta al Martes ---");
        BloqueTiempo nuevoBloque       = new BloqueTiempo("Martes", "09:00", "09:50");
        Asignacion   asigCorregida     = new Asignacion(nuevoBloque, aulaT3, profLuis, discreta);
        coord2.ajustarHorario(semestre3, asigCorregida);
        System.out.println();

        System.out.println("--- Horario final tras ajuste ---");
        coord2.consultarHorario(semestre3);
        System.out.println();

        System.out.println("--- Login Estudiante ---");
        estud2.iniciarSesion();
        System.out.println();

        System.out.println("--- Consulta Estudiante ---");
        estud2.consultarHorario(semestre3);
        System.out.println();

        System.out.println("--- Login Profesor Luis Quesada ---");
        profLuis.iniciarSesion();
        System.out.println();

        System.out.println("--- Consulta Profesor Luis Quesada ---");
        profLuis.consultarHorario(semestre3);
        System.out.println();
    }

    // =========================================================================
    // ESCENARIO 3 — Semestre V: múltiples cursos y tres profesores distintos
    // =========================================================================
    static void escenario3() {
        System.out.println("************************************************************");
        System.out.println("   ESCENARIO 3 — Semestre V: múltiples cursos y profesores");
        System.out.println("************************************************************\n");

        // --- Departamentos ---
        Departamento deptoRedes = new Departamento("Redes y Telecomunicaciones");
        Departamento deptoSO    = new Departamento("Sistemas Operativos");

        // --- Aulas ---
        AulaTeoria aulaT4 = new AulaTeoria("Aula T-501", 501, "Edificio E", 40, true, true);
        AulaTeoria aulaT5 = new AulaTeoria("Aula T-502", 502, "Edificio E", 40, true, true);
        AulaLab    aulaL3 = new AulaLab("Lab L-601", 601, "Edificio F", 30, "Servidores Dell", 15);

        LinkedList<Aula> aulas = new LinkedList<>();
        aulas.add(aulaT4);
        aulas.add(aulaT5);
        aulas.add(aulaL3);

        // --- Asignaturas ---
        AsignaturaTeorica redes = new AsignaturaTeorica(
                "Redes de Computadoras", 4, true, "http://apuntes.tec.ac.cr/redes");
        AsignaturaTeorica so = new AsignaturaTeorica(
                "Sistemas Operativos", 4, true, "http://apuntes.tec.ac.cr/so");

        LinkedList<String> matRedes = new LinkedList<>();
        matRedes.add("Wireshark");
        matRedes.add("Packet Tracer");
        AsignaturaPractica labRedes = new AsignaturaPractica(
                "Laboratorio de Redes", 1, true, matRedes, "Linux Debian");

        LinkedList<Asignatura> asignaturas = new LinkedList<>();
        asignaturas.add(redes);
        asignaturas.add(so);
        asignaturas.add(labRedes);

        // --- Profesores (bloques distintos → sin choques) ---
        Profesor profAndres = new Profesor("Andrés Mora", "112244668", "profesor_112244668", "pass5", deptoRedes);
        profAndres.registrarDisponibilidad("Lunes",     "12:30", "13:20");
        profAndres.registrarDisponibilidad("Miércoles", "12:30", "13:20");
        profAndres.setCursoImpartido(redes);

        Profesor profValeria = new Profesor("Valeria Núñez", "223355779", "profesor_223355779", "pass6", deptoSO);
        profValeria.registrarDisponibilidad("Martes", "14:00", "14:50");
        profValeria.registrarDisponibilidad("Jueves", "14:00", "14:50");
        profValeria.setCursoImpartido(so);

        Profesor profRicardo = new Profesor("Ricardo Ulate", "334466880", "profesor_334466880", "pass7", deptoRedes);
        profRicardo.registrarDisponibilidad("Viernes", "07:00", "07:50");
        profRicardo.setCursoImpartido(labRedes);

        LinkedList<Profesor> profesores = new LinkedList<>();
        profesores.add(profAndres);
        profesores.add(profValeria);
        profesores.add(profRicardo);

        // --- Usuarios ---
        Coordinador coord3 = new Coordinador("Diana Brenes", "700800900", "coordinador_700800900", "coord3");
        Estudiante  estud3 = new Estudiante("Miguel Torres", "800900100", "estudiante_2022",       "estud3");

        // --- Semestre ---
        Semestre semestre5 = new Semestre(5);

        System.out.println("--- Login Coordinador ---");
        coord3.iniciarSesion();
        System.out.println();

        coord3.generarHorario(semestre5, aulas, profesores, asignaturas);
        System.out.println();

        System.out.println("--- Consulta Coordinador ---");
        coord3.consultarHorario(semestre5);
        System.out.println();

        System.out.println("--- Login Estudiante ---");
        estud3.iniciarSesion();
        System.out.println();

        System.out.println("--- Consulta Estudiante ---");
        estud3.consultarHorario(semestre5);
        System.out.println();

        System.out.println("--- Login Profesor Andrés Mora ---");
        profAndres.iniciarSesion();
        System.out.println();

        System.out.println("--- Consulta Profesor Andrés Mora ---");
        profAndres.consultarHorario(semestre5);
        System.out.println();

        System.out.println("--- Login Profesor Valeria Núñez ---");
        profValeria.iniciarSesion();
        System.out.println();

        System.out.println("--- Consulta Profesor Valeria Núñez ---");
        profValeria.consultarHorario(semestre5);
        System.out.println();
    }
}
