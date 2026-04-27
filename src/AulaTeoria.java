public class AulaTeoria extends Aula{
    private boolean tieneAireAcondicionado;
    private boolean tieneEquipoMultimedia;

    public AulaTeoria(String nombre, int numAula, String ubicacion, int capacidad, boolean tieneAireAcondicionado, boolean tieneEquipoMultimedia) {
        super(nombre, numAula, ubicacion, capacidad);
        this.tieneAireAcondicionado = tieneAireAcondicionado;
        this.tieneEquipoMultimedia = tieneEquipoMultimedia;
    }
}
