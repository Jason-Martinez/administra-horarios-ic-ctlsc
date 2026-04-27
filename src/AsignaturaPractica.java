import java.util.LinkedList;

public class AsignaturaPractica extends Asignatura{
    private LinkedList<String> materialApoyo;
    private String sistemaOperativo;

    public AsignaturaPractica(String nombre, int creditos, boolean esExclusivaCarrera, LinkedList<String> materialApoyo, String sistemaOperativo) {
        super(nombre, creditos, esExclusivaCarrera);
        this.materialApoyo = materialApoyo;
        this.sistemaOperativo = sistemaOperativo;
    }
}
