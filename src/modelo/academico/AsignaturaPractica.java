package modelo.academico;

import java.util.LinkedList;

/**
 * modelo.academico.Asignatura de tipo práctico. Requiere materiales de apoyo y un sistema operativo específico.
 */
public class AsignaturaPractica extends Asignatura {
    private LinkedList<String> materialApoyo;
    private String sistemaOperativo;

    /**
     * Crea una asignatura práctica con sus recursos técnicos definidos.
     * @param nombre            Nombre de la asignatura
     * @param creditos          Número de créditos
     * @param esExclusivaCarrera true si es exclusiva de la carrera
     * @param materialApoyo     Lista de materiales requeridos (software, guías, herramientas)
     * @param sistemaOperativo  Sistema operativo necesario para el laboratorio
     */
    public AsignaturaPractica(String nombre, int creditos, boolean esExclusivaCarrera,
                               LinkedList<String> materialApoyo, String sistemaOperativo) {
        super(nombre, creditos, esExclusivaCarrera);
        this.materialApoyo = materialApoyo;
        this.sistemaOperativo = sistemaOperativo;
    }

    public LinkedList<String> getMaterialApoyo()             { return materialApoyo; }
    public String getSistemaOperativo()                      { return sistemaOperativo; }
    public void setSistemaOperativo(String sistemaOperativo) { this.sistemaOperativo = sistemaOperativo; }

    /**
     * Agrega un material a la lista de apoyo del curso.
     * @param material Nombre o descripción del material a agregar
     */
    public void agregarMaterial(String material) { materialApoyo.add(material); }

    @Override
    public void obtenerDetalles() {
        System.out.println("modelo.academico.Asignatura Práctica: " + getNombre());
        System.out.println("  Créditos: " + getCreditos() + " (" + getHorasSemanales() + " hrs/semana)");
        System.out.println("  Exclusiva carrera: " + isExclusivaCarrera());
        System.out.println("  Sistema Operativo: " + sistemaOperativo);
        System.out.println("  Materiales: " + materialApoyo);
    }

    @Override
    public String obtenerTipo() { return "Práctica"; }
}
