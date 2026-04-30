/**
 * Representa un bloque de tiempo en un día específico de la semana.
 * Es usado por los profesores para registrar disponibilidad y por el
 * generador para asignar horarios sin choques.
 */
public class BloqueTiempo {
    private String diaSemana;
    private String horarioInicio;
    private String horaFin;

    /**
     * Crea un bloque de tiempo con día y rango horario.
     * @param diaSemana     Día de la semana (ej: "Lunes", "Martes")
     * @param horarioInicio Hora de inicio en formato HH:mm (ej: "07:00")
     * @param horaFin       Hora de fin en formato HH:mm (ej: "09:50")
     */
    public BloqueTiempo(String diaSemana, String horarioInicio, String horaFin) {
        this.diaSemana = diaSemana;
        this.horarioInicio = horarioInicio;
        this.horaFin = horaFin;
    }

    public String getDiaSemana()    { return diaSemana; }
    public String getHorarioInicio(){ return horarioInicio; }
    public String getHoraFin()      { return horaFin; }

    public void setDiaSemana(String diaSemana)         { this.diaSemana = diaSemana; }
    public void setHorarioInicio(String horarioInicio) { this.horarioInicio = horarioInicio; }
    public void setHoraFin(String horaFin)             { this.horaFin = horaFin; }

    @Override
    public String toString() {
        return diaSemana + ": " + horarioInicio + "-" + horaFin;
    }
}
