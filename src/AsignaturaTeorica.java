/**
 * Asignatura de tipo teórico. Complementa los datos base con un sitio web de apuntes.
 */
public class AsignaturaTeorica extends Asignatura {
    private String sitioWebApuntes;

    /**
     * Crea una asignatura teórica con su sitio de apuntes asociado.
     * @param nombre            Nombre de la asignatura
     * @param creditos          Número de créditos
     * @param esExclusivaCarrera true si es exclusiva de la carrera
     * @param sitioWebApuntes   URL donde se publican los apuntes del curso
     */
    public AsignaturaTeorica(String nombre, int creditos, boolean esExclusivaCarrera, String sitioWebApuntes) {
        super(nombre, creditos, esExclusivaCarrera);
        this.sitioWebApuntes = sitioWebApuntes;
    }

    public String getSitioWebApuntes()                     { return sitioWebApuntes; }
    public void setSitioWebApuntes(String sitioWebApuntes) { this.sitioWebApuntes = sitioWebApuntes; }

    @Override
    public void obtenerDetalles() {
        System.out.println("Asignatura Teórica: " + getNombre());
        System.out.println("  Créditos: " + getCreditos() + " (" + getHorasSemanales() + " hrs/semana)");
        System.out.println("  Exclusiva carrera: " + isExclusivaCarrera());
        System.out.println("  Sitio de apuntes: " + sitioWebApuntes);
    }

    @Override
    public String obtenerTipo() { return "Teórica"; }
}
