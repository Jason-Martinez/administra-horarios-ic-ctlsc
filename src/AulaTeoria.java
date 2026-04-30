/**
 * Aula destinada a clases teóricas. Puede contar con aire acondicionado y equipo multimedia.
 */
public class AulaTeoria extends Aula {
    private boolean tieneAireAcondicionado;
    private boolean tieneEquipoMultimedia;

    /**
     * Crea un aula de teoría con sus características de equipamiento.
     * @param nombre                 Nombre del aula
     * @param numAula                Número del aula
     * @param ubicacion              Ubicación física
     * @param capacidad              Capacidad máxima de estudiantes
     * @param tieneAireAcondicionado true si el aula cuenta con aire acondicionado
     * @param tieneEquipoMultimedia  true si el aula cuenta con proyector o pantalla
     */
    public AulaTeoria(String nombre, int numAula, String ubicacion, int capacidad,
                      boolean tieneAireAcondicionado, boolean tieneEquipoMultimedia) {
        super(nombre, numAula, ubicacion, capacidad);
        this.tieneAireAcondicionado = tieneAireAcondicionado;
        this.tieneEquipoMultimedia = tieneEquipoMultimedia;
    }

    public boolean isTieneAireAcondicionado()        { return tieneAireAcondicionado; }
    public boolean isTieneEquipoMultimedia()         { return tieneEquipoMultimedia; }
    public void setTieneAireAcondicionado(boolean v) { this.tieneAireAcondicionado = v; }
    public void setTieneEquipoMultimedia(boolean v)  { this.tieneEquipoMultimedia = v; }

    @Override
    public void obtenerDetalles() {
        System.out.println("Aula de Teoría: " + getNombre() + " (#" + getNumAula() + ")");
        System.out.println("  Ubicación: " + getUbicacion() + " | Capacidad: " + getCapacidad());
        System.out.println("  Aire acondicionado: " + tieneAireAcondicionado);
        System.out.println("  Equipo multimedia: " + tieneEquipoMultimedia);
    }

    @Override
    public String obtenerTipo() { return "Teoría"; }

    /**
     * Solo es apropiada para asignaturas de tipo teórico.
     */
    @Override
    public boolean esApropiada(Asignatura asignatura) {
        return asignatura instanceof AsignaturaTeorica;
    }
}
