public class AsignaturaTeorica extends Asignatura{
    private String sitioWebApuntes;

    public AsignaturaTeorica(String nombre, int creditos, boolean esExclusivaCarrera, String sitioWebApuntes) {
        super(nombre, creditos, esExclusivaCarrera);
        this.sitioWebApuntes = sitioWebApuntes;
    }
}
