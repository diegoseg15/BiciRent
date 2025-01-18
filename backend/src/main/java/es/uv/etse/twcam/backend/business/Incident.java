package es.uv.etse.twcam.backend.business;

public class Incident {
    private String id;
    private String descripcion;
    private String ubicacion;
    private String situacion;
    private String estado; // e.g., "asignada", "en proceso"
    private String tecnico;

    // Nuevos atributos para los detalles de la reparación
    private String accionesRealizadas;
    private String piezasReemplazadas;
    private String detallesAdicionales;

    // Constructor sin argumentos (necesario para Gson)
    public Incident() {
    }

    // Constructor completo
    public Incident(String id, String descripcion, String ubicacion, String situacion, String estado, String tecnico) {
        this.id = id;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.situacion = situacion;
        this.estado = estado;
        this.tecnico = tecnico;
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
}
