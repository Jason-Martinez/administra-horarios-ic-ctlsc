package modelo.usuarios;

import logica.horario.Asignacion;
import logica.horario.GeneradorHorario;
import logica.horario.Semestre;
import modelo.academico.Asignatura;
import modelo.infraestructura.Aula;

import java.util.LinkedList;

/**
 * Representa al coordinador de la carrera. Es el único usuario con permisos
 * para generar y ajustar los horarios del semestre.
 */
public class Coordinador extends Usuario {

    /**
     * Crea un coordinador con sus credenciales de acceso.
     * @param nombre   Nombre completo
     * @param cedula   Cédula de identidad
     * @param login    Identificador de acceso (formato: coordinador_cedula)
     * @param password Contraseña de acceso
     */
    public Coordinador(String nombre, String cedula, String login, String password) {
        super(nombre, cedula, login, password);
    }

    /**
     * Genera automáticamente el horario de un semestre utilizando el GeneradorHorario.
     * Toma en cuenta la disponibilidad de los profesores, las asignaturas del semestre
     * y las aulas disponibles para construir las asignaciones sin choques.
     * @param semestre    Semestre para el cual se generará el horario
     * @param aulas       Lista de aulas disponibles en el sistema
     * @param profesores  Lista de profesores registrados
     * @param asignaturas Lista de asignaturas que deben ser asignadas
     */
    public void generarHorario(Semestre semestre, LinkedList<Aula> aulas,
                               LinkedList<Profesor> profesores,
                               LinkedList<Asignatura> asignaturas) {
        GeneradorHorario generador = new GeneradorHorario(
                semestre.getAsignaciones(), aulas, profesores, asignaturas
        );
        generador.generadorAutomatico();
        System.out.println("Horario generado para el semestre " + semestre.getNumero());
    }

    /**
     * Reemplaza manualmente una asignación existente en el semestre.
     * Busca la asignación del mismo curso y la sustituye por la nueva.
     * @param semestre        Semestre donde se realizará el ajuste
     * @param nuevaAsignacion Nueva asignación con los datos corregidos
     */
    public void ajustarHorario(Semestre semestre, Asignacion nuevaAsignacion) {
        Asignacion aEliminar = null;
        for (Asignacion a : semestre.getAsignaciones()) {
            if (a.getCurso() == nuevaAsignacion.getCurso()) {
                aEliminar = a;
                break;
            }
        }
        if (aEliminar != null) {
            semestre.getAsignaciones().remove(aEliminar);
        }
        semestre.getAsignaciones().add(nuevaAsignacion);
        System.out.println("Asignación ajustada manualmente para: " +
                nuevaAsignacion.getCurso().getNombre());
    }

    /**
     * Muestra el horario completo del semestre con todos los detalles del aula incluidos.
     * @param semestre Semestre cuyo horario se desea consultar
     */
    @Override
    public void consultarHorario(Semestre semestre) {
        System.out.println("----------- Horario completo -> Semestre: " + semestre.getNumero() + " -----------\n");
        for (Asignacion asig : semestre.getAsignaciones()) {
            System.out.println("modelo.academico.Asignatura: " + asig.getCurso().getNombre());
            System.out.println("Tipo: " + asig.getCurso().obtenerTipo());
            asig.getAula().obtenerDetalles();
            System.out.println("modelo.usuarios.Profesor: " + asig.getProfesor().getNombre());
            System.out.println("Bloque: " + asig.getTiempo());
            System.out.println("------------------------------------------------------------------------------------");
        }
    }

    @Override
    public String obtenerRol() { return "Rol: modelo.usuarios.Coordinador"; }
}
