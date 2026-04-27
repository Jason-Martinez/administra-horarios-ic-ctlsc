public abstract class Usuario {
    private String nombre;
    private String cedula;
    private String login;
    private String password;

    public Usuario(String nombre, String cedula, String login, String password) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.login = login;
        this.password = password;
    }

    public void iniciarSesion(){

    }

    public void consultarHorario(){

    }

    public void obtenerRol(){

    }
}
