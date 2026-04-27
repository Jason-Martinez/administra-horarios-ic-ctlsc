public class AulaLab extends Aula{
    private String equipoTecnico;
    private int cantidadEquipo;

    public AulaLab(String nombre, int numAula, String ubicacion, int capacidad, String equipoTecnico, int cantidadEquipo) {
        super(nombre, numAula, ubicacion, capacidad);
        this.equipoTecnico = equipoTecnico;
        this.cantidadEquipo = cantidadEquipo;
    }
}
