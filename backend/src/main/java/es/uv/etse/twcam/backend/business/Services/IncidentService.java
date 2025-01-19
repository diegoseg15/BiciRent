package es.uv.etse.twcam.backend.business.Services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import es.uv.etse.twcam.backend.business.Incident;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IncidentService {
    // Ruta del archivo JSON donde se almacenan las incidencias
    private static final String FILE_PATH = System.getProperty("user.dir") + "/backend/incidents.json";

    // Ruta del archivo JSON para almacenar los detalles de reparaciones
    private static final String REPAIRS_FILE_PATH = System.getProperty("user.dir") + "/backend/reparaciones.json";

    // Lista en memoria de las incidencias cargadas desde el archivo JSON
    private static List<Incident> incidents = new ArrayList<>();

    // Objeto Gson para serialización y deserialización de JSON
    private final Gson gson = new Gson();

    public IncidentService() {
        ensureRepairsFileExists(); // Verifica si el archivo reparaciones.json existe, si no, lo crea
        loadIncidentsFromFile(); // Carga las incidencias desde el archivo JSON
    }

    // Método privado para leer incidencias desde el archivo JSON
    private void loadIncidentsFromFile() {
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(FILE_PATH))) {
            // Convierte el contenido del archivo JSON en una lista de Incident
            Type listType = new TypeToken<List<Incident>>() {}.getType();
            incidents = gson.fromJson(reader, listType);

            // Inicializa la lista si está vacía o es nula
            if (incidents == null) {
                incidents = new ArrayList<>();
            }

            System.out.println("Archivo leído desde: " + FILE_PATH);
        } catch (IOException e) {
            System.err.println("Error leyendo el archivo JSON desde " + FILE_PATH + ": " + e.getMessage());
        }
    }

    // Método para guardar incidencias en el archivo JSON, actualizando solo el estado
    private void saveIncidentsToFile(String id, String newState) {
        try {
            File incidentFile = new File(FILE_PATH);
            List<Map<String, Object>> incidentList;

            // Verifica si el archivo existe y tiene contenido
            if (incidentFile.exists() && incidentFile.length() > 0) {
                try (InputStreamReader reader = new InputStreamReader(new FileInputStream(incidentFile))) {
                    Type listType = new TypeToken<List<Map<String, Object>>>() {}.getType();
                    incidentList = gson.fromJson(reader, listType);
                }
            } else {
                System.err.println("Archivo JSON no existe o está vacío.");
                return;
            }

            // Busca la incidencia por ID y actualiza su estado
            boolean updated = false;
            for (Map<String, Object> incident : incidentList) {
                if (incident.get("id").equals(id)) {
                    incident.put("estado", newState); // Actualiza el estado
                    updated = true;
                    break;
                }
            }

            // Si se actualizó la incidencia, escribe los cambios en el archivo
            if (updated) {
                try (FileWriter writer = new FileWriter(incidentFile)) {
                    gson.toJson(incidentList, writer);
                    System.out.println("Estado actualizado en el archivo: " + FILE_PATH);
                }
            } else {
                System.err.println("Incidencia no encontrada con ID: " + id);
            }
        } catch (IOException e) {
            System.err.println("Error actualizando el estado en el archivo JSON: " + e.getMessage());
        }
    }

    // Devuelve todas las incidencias cargadas en memoria
    public List<Incident> getAllIncidents() {
        return incidents;
    }

    // Busca y devuelve una incidencia por su ID
    public Incident getIncidentById(String id) {
        return incidents.stream()
                .filter(i -> i.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // Actualiza el estado de una incidencia específica
    public boolean updateIncidentState(String id, String newState) {
        Incident incident = getIncidentById(id);
        if (incident == null) {
            return false; // Incidencia no encontrada
        }

        // Valida si la transición de estado es válida
        if (!isValidTransition(incident.getEstado(), newState)) {
            return false; // Transición no válida
        }

        incident.setEstado(newState); // Actualiza el estado en memoria
        saveIncidentsToFile(id, newState); // Guarda el cambio en el archivo JSON
        return true;
    }

    // Valida si una transición de estado es válida
    protected boolean isValidTransition(String currentState, String newState) {
        Map<String, List<String>> validTransitions = Map.of(
                "asignada", List.of("en proceso"),
                "en proceso", List.of("Esperando repuestos", "completada"),
                "Esperando repuestos", List.of("en proceso", "completada"));

        return validTransitions.getOrDefault(currentState, List.of()).contains(newState);
    }

    // Verifica que el archivo de reparaciones existe, y lo crea si no
    private void ensureRepairsFileExists() {
        File repairsFile = new File(REPAIRS_FILE_PATH);
        if (!repairsFile.exists()) {
            try {
                if (repairsFile.createNewFile()) {
                    System.out.println("Archivo de reparaciones creado en: " + REPAIRS_FILE_PATH);
                    try (FileWriter writer = new FileWriter(repairsFile)) {
                        writer.write("[]"); // Inicializa con un array vacío
                    }
                }
            } catch (IOException e) {
                System.err.println("Error creando el archivo de reparaciones: " + e.getMessage());
            }
        }
    }

    // Guarda los detalles de reparación en reparaciones.json
    public boolean saveRepairDetailsToFile(String id, String acciones, String piezas, String detalles, String bicicleta) {
        try {
            List<Map<String, String>> repairs;
            File repairsFile = new File(REPAIRS_FILE_PATH);

            // Lee los datos existentes del archivo
            if (repairsFile.exists() && repairsFile.length() > 0) {
                try (InputStreamReader reader = new InputStreamReader(new FileInputStream(repairsFile))) {
                    Type listType = new TypeToken<List<Map<String, String>>>() {}.getType();
                    repairs = gson.fromJson(reader, listType);
                }
            } else {
                repairs = new ArrayList<>();
            }

            // Agrega los nuevos detalles de reparación
            Map<String, String> repairDetails = Map.of(
                    "id", id,
                    "bicicleta", bicicleta,
                    "acciones", acciones,
                    "piezas", piezas != null ? piezas : "",
                    "detalles", detalles != null ? detalles : "");
            repairs.add(repairDetails);

            // Guarda los cambios en el archivo reparaciones.json
            try (FileWriter writer = new FileWriter(REPAIRS_FILE_PATH)) {
                gson.toJson(repairs, writer);
            }

            return true;
        } catch (IOException e) {
            System.err.println("Error guardando los detalles de reparación: " + e.getMessage());
            return false;
        }
    }

    // Registra los detalles de reparación para una incidencia completada
    public boolean registerIncidentDetails(String id, String acciones, String piezas, String detalles, String bicicleta) {
        Incident incident = getIncidentById(id);

        // Verifica que la incidencia exista y esté completada
        if (incident != null && "completada".equals(incident.getEstado())) {
            incident.setAccionesRealizadas(acciones);
            incident.setPiezasReemplazadas(piezas);
            incident.setDetallesAdicionales(detalles);
            incident.setDetallesBicicleta(bicicleta);
            return saveRepairDetailsToFile(id, acciones, piezas, detalles, bicicleta);
        }
        return false;
    }
}
