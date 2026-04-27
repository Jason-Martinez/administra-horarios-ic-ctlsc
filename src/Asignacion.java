public class Asignacion {
    private Asignatura curso;
    private Profesor profesor;
    private Aula aula;
    private BloqueTiempo tiempo;

    public Asignacion(BloqueTiempo tiempo, Aula aula, Profesor profesor, Asignatura curso) {
        this.tiempo = tiempo;
        this.aula = aula;
        this.profesor = profesor;
        this.curso = curso;
    }
}
