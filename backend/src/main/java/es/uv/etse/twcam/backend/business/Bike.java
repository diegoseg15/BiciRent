package es.uv.etse.twcam.backend.business;

public class Bike {
    private String id;
    private String nombre;
    private Number costo;
    private String estacionId;

    // Constructor sin parámetros (por defecto)
    public Bike() {
    }

    // Constructor con todos los parámetros
    public Bike(String id, String nombre, Number costo, String estacionId) {
        this.id = id;
        this.nombre = nombre;
        this.costo = costo;
        this.estacionId = estacionId;
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

    public Number getCosto() {
        return costo;
    }

    public void setCosto(Number costo) {
        this.costo = costo;
    }

    public String getEstacionId() {
        return estacionId;
    }

    public void setEstacionId(String estacionId) {
        this.estacionId = estacionId;
    }

}
