import java.util.LinkedList;

public class Semestre {
    private int numero;
    private LinkedList<Asignacion> asignacions;

    public Semestre(int numero, LinkedList<Asignacion> asignacions) {
        this.numero = numero;
        this.asignacions = asignacions;
    }
}
