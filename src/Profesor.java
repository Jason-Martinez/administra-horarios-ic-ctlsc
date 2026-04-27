import java.util.LinkedList;

public class Profesor extends Usuario{
    private Departamento departamento;
    private LinkedList<BloqueTiempo> disponibilidad;
    private LinkedList<Asignatura> cursosImpartidos;

    public Profesor(String nombre, String cedula, String login, String password, Departamento departamento,
                    LinkedList<BloqueTiempo> disponibilidad, LinkedList<Asignatura> cursosImpartidos) {
        super(nombre, cedula, login, password);
        this.departamento = departamento;
        this.disponibilidad = disponibilidad;
        this.cursosImpartidos = cursosImpartidos;
    }

    public void registrarDisponibilidad(){

    }
}
