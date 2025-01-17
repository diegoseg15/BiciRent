package es.uv.etse.twcam.backend.business;

import java.util.HashMap;
import java.util.Map;

public class BikeStation {
    private String name;
    private String location;
    private int capacity;
    private Map<String, Bike> bikes = new HashMap<>(); // ID de bicicleta -> objeto Bike

    public BikeStation(String name, String location, int capacity) {
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        // Inicializar algunas bicicletas por estación
        bikes.put("bike1", new Bike("bike1", "disponible"));
        bikes.put("bike2", new Bike("bike2", "disponible"));
    }

    public String getName() {
        return name;
    }

    public boolean rentBike(String bikeId, String userId, String rentalDate) {
        Bike bike = bikes.get(bikeId);
        if (bike != null && bike.getStatus().equals("disponible")) {
            bike.setStatus("alquilada");
            // Registrar el alquiler en la base de datos o archivo
            // Aquí podrías agregar la lógica de persistencia
            return true;
        }
        return false; // Si no está disponible
    }
}

class Bike {
    private String id;
    private String status; // "disponible" o "alquilada"

    public Bike(String id, String status) {
        this.id = id;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
