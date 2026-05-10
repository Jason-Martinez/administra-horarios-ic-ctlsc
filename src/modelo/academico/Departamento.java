package modelo.academico;

/**
 * Representa el departamento académico al que pertenece un profesor.
 */
public class Departamento {
    private String nombre;

    /**
     * Crea un departamento con su nombre.
     * @param nombre Nombre del departamento (ej: "Ciencias de la Computación")
     */
    public Departamento(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre()              { return nombre; }
    public void setNombre(String nombre)   { this.nombre = nombre; }

    @Override
    public String toString() { return nombre; }
}
