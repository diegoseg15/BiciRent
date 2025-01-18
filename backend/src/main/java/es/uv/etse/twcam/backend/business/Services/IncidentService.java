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
    private static final String FILE_PATH = System.getProperty("user.dir") + "/backend/incidents.json";
    private static List<Incident> incidents = new ArrayList<>();
    private final Gson gson = new Gson();

    public IncidentService() {
        ensureFileAndFolderExists();
        loadIncidentsFromFile(); // Cargar incidencias desde el archivo JSON
    }

    // Leer incidencias desde el archivo JSON
    private void loadIncidentsFromFile() {
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(FILE_PATH))) {
            // Leer el archivo JSON y convertirlo en lista de objetos Incident
            Type listType = new TypeToken<List<Incident>>() {
            }.getType();
            incidents = gson.fromJson(reader, listType);

            if (incidents == null) {
                incidents = new ArrayList<>();
            }

            System.out.println("Archivo leído desde: " + FILE_PATH);
        } catch (IOException e) {
            System.err.println("Error leyendo el archivo JSON desde " + FILE_PATH + ": " + e.getMessage());
        }
    }

    // Verificar que la carpeta y el archivo existen
    private void ensureFileAndFolderExists() {
        File backendFolder = new File(System.getProperty("user.dir") + "/backend");
        if (!backendFolder.exists()) {
            backendFolder.mkdir(); // Crear la carpeta backend si no existe
        }

        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    System.out.println("Archivo creado en: " + FILE_PATH);
                    // Inicializa el archivo con una lista vacía
                    try (FileWriter writer = new FileWriter(file)) {
                        writer.write("[]");
                    }
                }
            } catch (IOException e) {
                System.err.println("Error creando el archivo JSON: " + e.getMessage());
            }
        }
    }

    // Guardar incidencias en el archivo JSON
    private void saveIncidentsToFile() {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(incidents, writer);
            System.out.println("Archivo guardado en: " + FILE_PATH);
        } catch (IOException e) {
            System.err.println("Error guardando el archivo JSON en " + FILE_PATH + ": " + e.getMessage());
        }
    }

    public List<Incident> getAllIncidents() {
        return incidents;
    }

    public Incident getIncidentById(String id) {
        return incidents.stream()
                .filter(i -> i.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public boolean updateIncidentState(String id, String newState) {
        Incident incident = getIncidentById(id);
        if (incident == null) {
            return false; // Incidencia no encontrada
        }

        // Validar transiciones válidas
        if (!isValidTransition(incident.getEstado(), newState)) {
            return false; // Transición no válida
        }

        incident.setEstado(newState);
        saveIncidentsToFile(); // Guardar cambios en el archivo JSON
        return true;
    }

    private boolean isValidTransition(String currentState, String newState) {
        Map<String, List<String>> validTransitions = Map.of(
                "asignada", List.of("en proceso"),
                "en proceso", List.of("Esperando repuestos", "completada"),
                "Esperando repuestos", List.of("en proceso", "completada"));

        return validTransitions.getOrDefault(currentState, List.of()).contains(newState);
    }

    public boolean registerIncidentDetails(String id, String acciones, String piezas, String detalles) {
        Incident incident = getIncidentById(id);
        if (incident != null && "completada".equals(incident.getEstado())) {
            incident.setAccionesRealizadas(acciones);
            incident.setPiezasReemplazadas(piezas);
            incident.setDetallesAdicionales(detalles);
            saveIncidentsToFile(); // Guardar cambios en el archivo JSON
            return true;
        }
        return false;
    }
}
