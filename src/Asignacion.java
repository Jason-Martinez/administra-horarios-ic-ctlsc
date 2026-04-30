/**
 * Representa la asignación de una asignatura a un aula, profesor y bloque de tiempo.
 * Es la unidad central del horario generado y la que el GeneradorHorario construye.
 */
public class Asignacion {
    private Asignatura curso;
    private Profesor profesor;
    private Aula aula;
    private BloqueTiempo tiempo;

    /**
     * Crea una asignación con todos sus componentes definidos.
     * El aula puede ser null inicialmente y asignarse luego por el GeneradorHorario.
     * @param tiempo   Bloque de tiempo en que se imparte la asignatura
     * @param aula     Aula donde se impartirá (puede ser null al momento de crear)
     * @param profesor Profesor que imparte la asignatura
     * @param curso    Asignatura asignada
     */
    public Asignacion(BloqueTiempo tiempo, Aula aula, Profesor profesor, Asignatura curso) {
        this.tiempo = tiempo;
        this.aula = aula;
        this.profesor = profesor;
        this.curso = curso;
    }

    public Asignatura getCurso()     { return curso; }
    public Profesor getProfesor()    { return profesor; }
    public Aula getAula()            { return aula; }
    public BloqueTiempo getTiempo()  { return tiempo; }

    public void setCurso(Asignatura curso)      { this.curso = curso; }
    public void setProfesor(Profesor profesor)  { this.profesor = profesor; }
    public void setAula(Aula aula)              { this.aula = aula; }
    public void setTiempo(BloqueTiempo tiempo)  { this.tiempo = tiempo; }

    @Override
    public String toString() {
        return curso.getNombre() + " | " + aula.getNombre() + " | Prof: " +
               profesor.getNombre() + " | " + tiempo;
    }
}
