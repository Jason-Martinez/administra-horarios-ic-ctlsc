package modelo.usuarios;

import logica.horario.Asignacion;
import logica.horario.Semestre;

/**
 * Representa a un estudiante del sistema.
 * Su única acción disponible es consultar el horario completo de un semestre.
 */
public class Estudiante extends Usuario {

    /**
     * Crea un estudiante con sus credenciales de acceso.
     * @param nombre   Nombre completo
     * @param cedula   Cédula de identidad
     * @param login    Identificador de acceso (formato: estudiante_anioIngreso)
     * @param password Contraseña de acceso
     */
    public Estudiante(String nombre, String cedula, String login, String password) {
        super(nombre, cedula, login, password);
    }

    /**
     * Muestra todas las asignaciones del semestre con asignatura, aula, profesor y bloque.
     * @param semestre Semestre cuyo horario se desea consultar
     */
    @Override
    public void consultarHorario(Semestre semestre) {
        System.out.println("----------- Horario semestre: " + semestre.getNumero() + " -----------\n");
        for (Asignacion asig : semestre.getAsignaciones()) {
            System.out.println("modelo.academico.Asignatura: " + asig.getCurso().getNombre());
            System.out.println("Tipo: " + asig.getCurso().obtenerTipo());
            System.out.println("modelo.infraestructura.Aula: " + asig.getAula().getNombre());
            System.out.println("modelo.usuarios.Profesor: " + asig.getProfesor().getNombre());
            System.out.println("Bloque: " + asig.getTiempo());
            System.out.println("------------------------------------------------------------------------------------");
        }
    }

    @Override
    public String obtenerRol() { return "Rol: modelo.usuarios.Estudiante"; }
}
