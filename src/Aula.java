public abstract class Aula{
    private String nombre;
    private int numAula;
    private String ubicacion;
    private int capacidad;

    public Aula(String nombre, int numAula, String ubicacion, int capacidad) {
        this.nombre = nombre;
        this.numAula = numAula;
        this.ubicacion = ubicacion;
        this.capacidad = capacidad;
    }

    public void obtenerDetalles(){

    }

    public void obtenerTipo(){

    }

    public void esApropiada(Asignatura asignatura){

    }
}
