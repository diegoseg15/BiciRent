package es.uv.etse.twcam.backend.business;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Rental {
    private String usuarioId;
    private String bicicletaId;
    private String fechaHoraRecogida;

    // Constructor por defecto
    public Rental() {
        // Inicializar cualquier campo si es necesario
    }

    // Constructor
    public Rental(String usuarioId, String bicicletaId, String fechaHoraRecogida) {
        this.usuarioId = usuarioId;
        this.bicicletaId = bicicletaId;
        // Validar y formatear la fecha en el formato adecuado
        if (fechaHoraRecogida != null && !fechaHoraRecogida.isEmpty()) {
            this.fechaHoraRecogida = fechaHoraRecogida;
        } else {
            // Asignar un valor predeterminado si no se proporciona una fecha válida
            this.fechaHoraRecogida = "2000-01-01 00:00"; // Fecha predeterminada
        }
    }

    // Getters y setters
    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getBicicletaId() {
        return bicicletaId;
    }

    public void setBicicletaId(String bicicletaId) {
        this.bicicletaId = bicicletaId;
    }

    public String getFechaHoraRecogida() {
        return fechaHoraRecogida;
    }

    public void setFechaHoraRecogida(String fechaHoraRecogida) {
        // Validar y formatear la fecha en el formato adecuado
        if (fechaHoraRecogida != null && !fechaHoraRecogida.isEmpty()) {
            this.fechaHoraRecogida = fechaHoraRecogida;
        } else {
            this.fechaHoraRecogida = "2000-01-01 00:00"; // Fecha predeterminada
        }
    }

    // Método para formatear la fecha
    public String obtenerFechaFormateada() {
        // Verifica que la fecha esté en el formato esperado
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime.parse(fechaHoraRecogida, formatter); // Valida el formato de la fecha
            return fechaHoraRecogida;
        } catch (Exception e) {
            // Si no es válida, se devuelve una fecha predeterminada o una cadena vacía
            return "Fecha inválida";
        }
    }
}
