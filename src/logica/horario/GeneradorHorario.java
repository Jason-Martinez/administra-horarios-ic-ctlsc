package logica.horario;

import logica.tiempo.BloqueTiempo;
import modelo.academico.Asignatura;
import modelo.infraestructura.Aula;
import modelo.usuarios.Profesor;

import java.util.LinkedList;

/**
 * Genera automáticamente los horarios de un semestre asignando aulas y bloques de tiempo
 * a cada asignatura según la disponibilidad de los profesores, sin producir choques.
 * Es invocado por el modelo.usuarios.Coordinador a través de generarHorario().
 */
public class GeneradorHorario {
    private LinkedList<Asignacion> asignaciones;
    private LinkedList<Aula> aulasDisponibles;
    private LinkedList<Profesor> profesores;
    private LinkedList<Asignatura> asignaturas;

    /**
     * Crea el generador con todos los datos necesarios para construir el horario.
     * @param asignaciones    Lista de asignaciones del semestre (inicialmente vacía)
     * @param aulasDisponibles Lista de aulas disponibles para asignar
     * @param profesores      Lista de profesores con su disponibilidad y cursos registrados
     * @param asignaturas     Lista de asignaturas que deben ser programadas
     */
    public GeneradorHorario(LinkedList<Asignacion> asignaciones,
                            LinkedList<Aula> aulasDisponibles,
                            LinkedList<Profesor> profesores,
                            LinkedList<Asignatura> asignaturas) {
        this.asignaciones = asignaciones;
        this.aulasDisponibles = aulasDisponibles;
        this.profesores = profesores;
        this.asignaturas = asignaturas;
    }

    public LinkedList<Asignacion> getAsignaciones()    { return asignaciones; }
    public LinkedList<Aula> getAulasDisponibles()      { return aulasDisponibles; }

    /**
     * Orquesta la generación completa del horario.
     * Para cada asignatura busca un profesor disponible, le crea una asignación
     * con su primer bloque libre, luego asigna el aula y valida choques al final.
     */
    public void generadorAutomatico() {
        for (Asignatura asignatura : asignaturas) {
            boolean asignada = false;
            for (Profesor profesor : profesores) {
                if (profesor.getCursoImpartido().contains(asignatura)) {
                    for (BloqueTiempo bloque : profesor.getDisponibilidad()) {
                        Asignacion nueva = new Asignacion(bloque, null, profesor, asignatura);
                        asignaciones.add(nueva);
                        asignada = true;
                        break;
                    }
                }
                if (asignada) break;
            }
            if (!asignada) {
                System.out.println("No se pudo asignar horario a: " + asignatura.getNombre());
            }
        }
        asignarAula();
        validarChoques();
    }

    /**
     * Recorre todas las asignaciones sin aula y les asigna la primera disponible
     * que sea compatible con el tipo de asignatura y no esté ocupada en ese bloque.
     */
    public void asignarAula() {
        for (Asignacion asignacion : asignaciones) {
            if (asignacion.getAula() != null) continue;

            for (Aula aula : aulasDisponibles) {
                if (aula.esApropiada(asignacion.getCurso())) {
                    boolean ocupada = false;
                    for (Asignacion otra : asignaciones) {
                        if (otra.getAula() == aula &&
                                otra.getTiempo().getDiaSemana().equals(asignacion.getTiempo().getDiaSemana()) &&
                                otra.getTiempo().getHorarioInicio().equals(asignacion.getTiempo().getHorarioInicio())) {
                            ocupada = true;
                            break;
                        }
                    }
                    if (!ocupada) {
                        asignacion.setAula(aula);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Revisa todas las asignaciones en busca de choques horarios.
     * Detecta dos tipos de conflicto: mismo aula en el mismo bloque, o
     * mismo profesor asignado a dos cursos en el mismo bloque.
     * Imprime en consola cada conflicto encontrado, o confirma si no hay ninguno.
     */
    public void validarChoques() {
        boolean hayChoques = false;
        for (Asignacion a : asignaciones) {
            for (Asignacion b : asignaciones) {
                if (a != b) {
                    if (a.getTiempo().getDiaSemana().equals(b.getTiempo().getDiaSemana()) &&
                            a.getTiempo().getHorarioInicio().equals(b.getTiempo().getHorarioInicio())) {

                        if (a.getAula() != null && a.getAula() == b.getAula()) {
                            System.out.println("CHOQUE de aula: " +
                                    a.getCurso().getNombre() + " y " + b.getCurso().getNombre() +
                                    " en " + a.getAula().getNombre());
                            hayChoques = true;
                        }
                        if (a.getProfesor() == b.getProfesor()) {
                            System.out.println("CHOQUE de profesor: " +
                                    a.getProfesor().getNombre() +
                                    " asignado a " + a.getCurso().getNombre() +
                                    " y " + b.getCurso().getNombre());
                            hayChoques = true;
                        }
                    }
                }
            }
        }
        if (!hayChoques) {
            System.out.println("No se encontraron choques en el horario.");
        }
    }
}
