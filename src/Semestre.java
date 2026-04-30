import java.util.LinkedList;

/**
 * Representa un semestre académico con su lista de asignaciones generadas.
 * Las asignaciones son pobladas por el GeneradorHorario a través del Coordinador.
 */
public class Semestre {
    private int numero;
    private LinkedList<Asignacion> asignaciones;

    /**
     * Crea un semestre con su número identificador e inicializa la lista de asignaciones vacía.
     * @param numero Número del semestre según el plan de estudios (ej: 1, 2, 3...)
     */
    public Semestre(int numero) {
        this.numero = numero;
        this.asignaciones = new LinkedList<>();
    }

    public int getNumero()                          { return numero; }
    public LinkedList<Asignacion> getAsignaciones() { return asignaciones; }

    public void setNumero(int numero)               { this.numero = numero; }

    /**
     * Agrega una asignación al semestre.
     * @param asignacion Asignación a incluir en el horario
     */
    public void setAsignacion(Asignacion asignacion) { this.asignaciones.add(asignacion); }
}
