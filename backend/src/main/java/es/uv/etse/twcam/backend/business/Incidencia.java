package es.uv.etse.twcam.backend.business;

public class Incidencia {
    private String fecha;
    private String hora;
    private String ubicacion;
    private String descripcion;
    private String estado; // "reportada", "en proceso", "esperandoRepuestos" , "finalizada"
    private String situacion; // "bloqueada en una estacion" ó "en tránsito"
    private String id;
    private String tecnico;
    private String dni;
    private String bicicleta;

    // Constructor por defecto necesario para Gson
    public Incidencia() {
    }

    public Incidencia(String fecha, String hora, String ubicacion, String descripcion,
            String estado, String situacion, String id, String tecnico, String dni, String bicicleta) {
        this.fecha = fecha;
        this.hora = hora;
        this.ubicacion = ubicacion;
        this.descripcion = descripcion;
        this.estado = estado;
        this.situacion = situacion;
        this.id = id;
        this.tecnico = tecnico;
        this.dni = dni;
        this.bicicleta = bicicleta;
    }

    @Override
    public String toString() {
        return "Incidencia{" +
                "fecha='" + fecha + '\'' +
                ", hora='" + hora + '\'' +
                ", ubicacion='" + ubicacion + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", estado='" + estado + '\'' +
                ", situacion='" + situacion + '\'' +
                ", id='" + id + '\'' +
                ", tecnico='" + tecnico + '\'' +
                ", dni='" + dni + '\'' +
                ", bicicleta='" + bicicleta + '\'' +
                '}';
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getSituacion() {
        return situacion;
    }

    public void setSituacion(String situacion) {
        this.situacion = situacion;
    }

    public void setIncidenciaId(String id) {
        this.id = id;
    }

    public String getIncidenciaId() {
        return id;
    }

    public void setTecnico(String tecnico) {
        this.tecnico = tecnico;
    }

    public String getTecnico() {
        return tecnico;
    }
    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getDni() {
        return dni;
    }

    public void setBicicleta(String bicicleta) {
        this.bicicleta = bicicleta;
    }

    public String getBicicleta() {
        return bicicleta;
    }

}
