/**
 * Clase abstracta que representa a un usuario del sistema de administración de horarios.
 * Define los atributos y comportamientos comunes para Estudiante, Profesor y Coordinador.
 */
public abstract class Usuario {
    private String nombre;
    private String cedula;
    private String login;
    private String password;

    /**
     * Crea un usuario con sus credenciales básicas.
     * @param nombre   Nombre completo del usuario
     * @param cedula   Cédula de identidad
     * @param login    Identificador de acceso con formato rol_identificador
     * @param password Contraseña de acceso
     */
    public Usuario(String nombre, String cedula, String login, String password) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.login = login;
        this.password = password;
    }

    public String getNombre()  { return nombre; }
    public String getCedula()  { return cedula; }
    public String getLogin()   { return login; }

    public void setNombre(String nombre)     { this.nombre = nombre; }
    public void setCedula(String cedula)     { this.cedula = cedula; }
    public void setLogin(String login)       { this.login = login; }
    public void setPassword(String password) { this.password = password; }

    /**
     * Muestra un mensaje de bienvenida e identifica el rol del usuario en consola.
     * No requiere entrada del usuario ya que los datos son quemados en el Main.
     */
    public void iniciarSesion() {
        if (this.login != null && this.password != null) {
            System.out.println("Bienvenido/a, " + this.nombre);
            if (this instanceof Profesor) {
                System.out.println("Ingreso como: Profesor");
            } else if (this instanceof Estudiante) {
                System.out.println("Ingreso como: Estudiante");
            } else if (this instanceof Coordinador) {
                System.out.println("Ingreso como: Coordinador");
            }
        } else {
            System.out.println("Credenciales incorrectas.");
        }
    }

    /**
     * Muestra las asignaciones del semestre según la perspectiva del rol.
     * @param semestre Semestre cuyo horario se desea consultar
     */
    public abstract void consultarHorario(Semestre semestre);

    /**
     * Retorna una cadena que describe el rol del usuario en el sistema.
     * @return Rol del usuario (ej: "Rol: Estudiante")
     */
    public abstract String obtenerRol();
}
