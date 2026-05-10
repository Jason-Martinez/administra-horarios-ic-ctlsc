package modelo.infraestructura;

import modelo.academico.Asignatura;
import modelo.academico.AsignaturaPractica;

/**
 * modelo.infraestructura.Aula de laboratorio destinada a clases prácticas.
 * Cuenta con equipo técnico especializado para los estudiantes.
 */
public class AulaLab extends Aula {
    private String equipoTecnico;
    private int cantidadEquipo;

    /**
     * Crea un aula de laboratorio con su equipamiento técnico.
     * @param nombre         Nombre del aula
     * @param numAula        Número del aula
     * @param ubicacion      Ubicación física
     * @param capacidad      Capacidad máxima de estudiantes
     * @param equipoTecnico  Descripción del equipo técnico disponible (ej: "Computadoras HP")
     * @param cantidadEquipo Cantidad de equipos disponibles en el laboratorio
     */
    public AulaLab(String nombre, int numAula, String ubicacion, int capacidad,
                   String equipoTecnico, int cantidadEquipo) {
        super(nombre, numAula, ubicacion, capacidad);
        this.equipoTecnico = equipoTecnico;
        this.cantidadEquipo = cantidadEquipo;
    }

    public String getEquipoTecnico()                    { return equipoTecnico; }
    public int getCantidadEquipo()                      { return cantidadEquipo; }
    public void setEquipoTecnico(String equipoTecnico)  { this.equipoTecnico = equipoTecnico; }
    public void setCantidadEquipo(int cantidadEquipo)   { this.cantidadEquipo = cantidadEquipo; }

    @Override
    public void obtenerDetalles() {
        System.out.println("modelo.infraestructura.Aula de Laboratorio: " + getNombre() + " (#" + getNumAula() + ")");
        System.out.println("  Ubicación: " + getUbicacion() + " | Capacidad: " + getCapacidad());
        System.out.println("  Equipo técnico: " + equipoTecnico + " (x" + cantidadEquipo + ")");
    }

    @Override
    public String obtenerTipo() { return "Laboratorio"; }

    /**
     * Solo es apropiada para asignaturas de tipo práctico.
     */
    @Override
    public boolean esApropiada(Asignatura asignatura) {
        return asignatura instanceof AsignaturaPractica;
    }
}
