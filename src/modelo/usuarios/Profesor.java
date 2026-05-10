package modelo.usuarios;

import logica.horario.Asignacion;
import logica.horario.Semestre;
import logica.tiempo.BloqueTiempo;
import modelo.academico.Asignatura;
import modelo.academico.Departamento;

import java.util.LinkedList;

/**
 * Representa a un profesor del sistema. Pertenece a un departamento académico,
 * tiene una disponibilidad horaria registrada y una lista de asignaturas que imparte.
 */
public class Profesor extends Usuario {
    private Departamento departamento;
    private LinkedList<BloqueTiempo> disponibilidad;
    private LinkedList<Asignatura> cursoImpartido;

    /**
     * Crea un profesor con su información básica y departamento.
     * La disponibilidad y cursos se agregan posteriormente con sus métodos correspondientes.
     * @param nombre       Nombre completo
     * @param cedula       Cédula de identidad
     * @param login        Identificador de acceso (formato: profesor_cedula)
     * @param password     Contraseña de acceso
     * @param departamento modelo.academico.Departamento académico al que pertenece
     */
    public Profesor(String nombre, String cedula, String login, String password, Departamento departamento) {
        super(nombre, cedula, login, password);
        this.departamento = departamento;
        this.disponibilidad = new LinkedList<>();
        this.cursoImpartido = new LinkedList<>();
    }

    public Departamento getDepartamento()              { return departamento; }
    public LinkedList<BloqueTiempo> getDisponibilidad(){ return disponibilidad; }
    public LinkedList<Asignatura> getCursoImpartido()  { return cursoImpartido; }

    public void setDepartamento(Departamento departamento) { this.departamento = departamento; }

    /**
     * Agrega una asignatura a la lista de cursos que imparte este profesor.
     * @param cursoImpartido modelo.academico.Asignatura que el profesor imparte
     */
    public void setCursoImpartido(Asignatura cursoImpartido) { this.cursoImpartido.add(cursoImpartido); }

    /**
     * Registra un bloque de tiempo en el que el profesor está disponible para dar clases.
     * Crea el BloqueTiempo internamente a partir de los datos recibidos.
     * @param dia    Día de la semana (ej: "Lunes")
     * @param inicio Hora de inicio en formato HH:mm
     * @param fin    Hora de fin en formato HH:mm
     */
    public void registrarDisponibilidad(String dia, String inicio, String fin) {
        BloqueTiempo tiempo = new BloqueTiempo(dia, inicio, fin);
        this.disponibilidad.add(tiempo);
        System.out.println("Disponibilidad registrada: " + tiempo);
    }

    /**
     * Muestra únicamente las asignaciones del semestre que corresponden a este profesor.
     * @param semestre Semestre cuyo horario se desea consultar
     */
    @Override
    public void consultarHorario(Semestre semestre) {
        System.out.println("----------- Horario profesor: " + this.getNombre() + " -----------\n");
        for (Asignacion asig : semestre.getAsignaciones()) {
            if (asig.getProfesor() == this) {
                System.out.println("modelo.academico.Asignatura: " + asig.getCurso().getNombre());
                System.out.println("modelo.infraestructura.Aula: " + asig.getAula().getNombre());
                System.out.println("Bloque: " + asig.getTiempo());
                System.out.println("------------------------------------------------------------------------------------");
            }
        }
    }

    @Override
    public String obtenerRol() { return "Rol: modelo.usuarios.Profesor"; }
}
