package es.uv.etse.twcam.backend.business;

public class Station {
    private String id;
    private String nombre;

    // Constructor sin parámetros (por defecto)
    public Station() {
    }

    // Constructor con todos los parámetros
    public Station(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;

    }

    // Getters y setters
    public String getId() {
        return id;
    }

    public void setId(String dni) {
        this.id = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
