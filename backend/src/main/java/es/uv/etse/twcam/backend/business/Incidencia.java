package es.uv.etse.twcam.backend.business;

public class Incidencia {
    private String fecha;
    private String hora;
    private String ubicacion;
    private String descripcion;
    private String estado;  //"reportada", "en proceso", "esperandoRepuestos" , "finalizada"
    private String situacion; //"bloqueada en una estacion" ó "en tránsito"
    private String incidenciaId;
    private String tecnico;
    
    // Constructor por defecto necesario para Gson
    public Incidencia() {
    }
    
    public Incidencia(String fecha, String hora, String ubicacion, String descripcion, 
                    String estado, String situacion, String incidenciaId, String tecnico) {
        this.fecha = fecha;
        this.hora = hora;
        this.ubicacion = ubicacion;
        this.descripcion = descripcion;
        this.estado = estado;
        this.situacion = situacion; 
        this.incidenciaId = incidenciaId;
        this.tecnico = tecnico;
    }

    public String getFecha() { return fecha; }
    public String getHora() { return hora; }
    public String getUbicacion() { return ubicacion; }
    public String getDescripcion() { return descripcion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getSituacion() { return situacion; }
    public void setSituacion(String situacion) { this.situacion = situacion; }
    public void setIncidenciaId(String incidenciaId) { this.incidenciaId = incidenciaId; }
    public String getIncidenciaId() { return incidenciaId; }
    public void setTecnico(String tecnico) { this.tecnico = tecnico; }
    public String getTecnico() { return tecnico; }

}
