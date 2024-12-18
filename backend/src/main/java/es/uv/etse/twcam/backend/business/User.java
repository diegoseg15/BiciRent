package es.uv.etse.twcam.backend.business;

public class User {
    private String dni;
    private String nombre;
    private String apellido;
    private String telefono;
    private String correo;
    private String password;
    private String rol;

    public User(String dni, String nombre, String apellido, String telefono, String correo, String password, String rol) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.correo = correo;
        this.password = password;
        this.rol = rol;
    }

    public String getDni() { return dni; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getTelefono() { return telefono; }
    public String getCorreo() { return correo; }
    public String getPassword() { return password; }
    public String getRol() { return rol; }
}