package modelo.academico;

/**
 * Clase abstracta que representa una asignatura del plan de estudios.
 * Puede ser de tipo Teórica o Práctica según su subclase.
 */
public abstract class Asignatura {
    private String nombre;
    private int creditos;
    private boolean esExclusivaCarrera;

    /**
     * Crea una asignatura con sus datos académicos básicos.
     * @param nombre            Nombre de la asignatura
     * @param creditos          Número de créditos (cada crédito equivale a 3 horas semanales)
     * @param esExclusivaCarrera true si la asignatura pertenece únicamente a esta carrera
     */
    public Asignatura(String nombre, int creditos, boolean esExclusivaCarrera) {
        this.nombre = nombre;
        this.creditos = creditos;
        this.esExclusivaCarrera = esExclusivaCarrera;
    }

    public String getNombre()             { return nombre; }
    public int getCreditos()              { return creditos; }
    public boolean isExclusivaCarrera()   { return esExclusivaCarrera; }

    public void setNombre(String nombre)                       { this.nombre = nombre; }
    public void setCreditos(int creditos)                      { this.creditos = creditos; }
    public void setEsExclusivaCarrera(boolean esExclusivaCarrera) { this.esExclusivaCarrera = esExclusivaCarrera; }

    /**
     * Calcula las horas semanales de la asignatura según sus créditos.
     * @return Horas semanales (créditos x 3)
     */
    public int getHorasSemanales() { return creditos * 3; }

    /**
     * Imprime en consola los detalles específicos de la asignatura según su tipo.
     */
    public abstract void obtenerDetalles();

    /**
     * Indica si la asignatura es de tipo Teórica o Práctica.
     * @return String con el tipo de asignatura
     */
    public abstract String obtenerTipo();
}
