/**
 * Clase abstracta que representa un aula del sistema.
 * Puede ser un aula de teoría (AulaTeoria) o un laboratorio (AulaLab).
 */
public abstract class Aula {
    private String nombre;
    private int numAula;
    private String ubicacion;
    private int capacidad;

    /**
     * Crea un aula con sus datos físicos básicos.
     * @param nombre    Nombre del aula
     * @param numAula   Número identificador del aula
     * @param ubicacion Ubicación física dentro del campus
     * @param capacidad Cantidad máxima de estudiantes que puede albergar
     */
    public Aula(String nombre, int numAula, String ubicacion, int capacidad) {
        this.nombre = nombre;
        this.numAula = numAula;
        this.ubicacion = ubicacion;
        this.capacidad = capacidad;
    }

    public String getNombre()   { return nombre; }
    public int getNumAula()     { return numAula; }
    public String getUbicacion(){ return ubicacion; }
    public int getCapacidad()   { return capacidad; }

    public void setNombre(String nombre)       { this.nombre = nombre; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public void setCapacidad(int capacidad)    { this.capacidad = capacidad; }

    /**
     * Imprime en consola los detalles físicos y técnicos del aula.
     */
    public abstract void obtenerDetalles();

    /**
     * Indica si el aula es de tipo Teoría o Laboratorio.
     * @return String con el tipo de aula
     */
    public abstract String obtenerTipo();

    /**
     * Determina si esta aula es compatible con el tipo de asignatura recibida.
     * AulaTeoria acepta AsignaturaTeorica y AulaLab acepta AsignaturaPractica.
     * @param asignatura Asignatura que se desea impartir en el aula
     * @return true si el aula es apropiada para esa asignatura
     */
    public abstract boolean esApropiada(Asignatura asignatura);
}
