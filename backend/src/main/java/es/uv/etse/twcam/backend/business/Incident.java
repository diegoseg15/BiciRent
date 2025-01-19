package es.uv.etse.twcam.backend.business;

public class Incident {
    private String id;
    private String descripcion;
    private String ubicacion;
    private String situacion;
    private String estado; // e.g., "asignada", "en proceso"
    private String tecnico;
    private String bicicleta;

    // Nuevos atributos para los detalles de la reparación
    private String accionesRealizadas;
    private String piezasReemplazadas;
    private String detallesAdicionales;
    private String detallesBicicleta;

    // Constructor sin argumentos (necesario para Gson)
    public Incident() {
    }

    // Constructor completo
    public Incident(String id, String descripcion, String ubicacion, String situacion, String estado, String tecnico, String bicicleta) {
        this.id = id;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.situacion = situacion;
        this.estado = estado;
        this.tecnico = tecnico;
        this.bicicleta = bicicleta;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public String getSituacion() { return situacion; }
    public void setSituacion(String situacion) { this.situacion = situacion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getTecnico() { return tecnico; }
    public void setTecnico(String tecnico) { this.tecnico = tecnico; }

    public String getBicicleta() { return bicicleta; }
    public void setBicicleta(String bicicleta) { this.bicicleta = bicicleta; }

    // Getters y Setters para los detalles de la reparación
    public String getAccionesRealizadas() {
        return accionesRealizadas;
    }

    public void setAccionesRealizadas(String accionesRealizadas) {
        this.accionesRealizadas = accionesRealizadas;
    }

    public String getPiezasReemplazadas() {
        return piezasReemplazadas;
    }

    public void setPiezasReemplazadas(String piezasReemplazadas) {
        this.piezasReemplazadas = piezasReemplazadas;
    }

    public String getDetallesAdicionales() {
        return detallesAdicionales;
    }

    public void setDetallesAdicionales(String detallesAdicionales) {
        this.detallesAdicionales = detallesAdicionales;
    }

    public String getDetallesBicicleta() {
        return detallesBicicleta;
    }

    public void setDetallesBicicleta(String detallesBicicleta) {
        this.detallesBicicleta = detallesBicicleta;
    }

}
