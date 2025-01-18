package es.uv.etse.twcam.backend.business.Services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import es.uv.etse.twcam.backend.business.Station;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StationService {
    private static final String FILE_PATH = System.getProperty("user.dir") + "/backend/stations.json"; // Corregir ruta del
                                                                                                    // archivo
    public List<Station> stations = new ArrayList<>();
    private final Gson gson = new Gson();

    public StationService() {
        ensureFileAndFolderExists();
        loadStationsFromFile(); // Cargar bicicletas desde el archivo JSON
    }

    // Leer las bicicletas desde el archivo JSON
    public void loadStationsFromFile() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            try (InputStreamReader reader = new InputStreamReader(new FileInputStream(FILE_PATH))) {
                // Leer el archivo JSON y convertirlo en lista de objetos Bike
                Type listType = new TypeToken<List<Station>>() {
                }.getType();
                stations = gson.fromJson(reader, listType);

                if (stations == null) {
                    stations = new ArrayList<>();
                }

                System.out.println("Archivo leído desde: " + FILE_PATH);
            } catch (IOException e) {
                System.err.println("Error leyendo el archivo JSON desde " + FILE_PATH + ": " + e.getMessage());
            } catch (com.google.gson.JsonSyntaxException e) {
                System.err.println("Error al parsear el archivo JSON, el formato no es válido: " + e.getMessage());
            }
        }
    }

    // Verificar que la carpeta y el archivo existen
    public void ensureFileAndFolderExists() {
        File backendFolder = new File(System.getProperty("user.dir") + "/backend");
        if (!backendFolder.exists()) {
            backendFolder.mkdir(); // Crear la carpeta backend si no existe
        }

        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    System.out.println("Archivo creado en: " + FILE_PATH);
                    // Inicializa el archivo con la estructura básica
                    try (FileWriter writer = new FileWriter(file)) {
                        // Inicia el archivo con una lista vacía de bicicletas
                        writer.write("[]");
                    }
                }
            } catch (IOException e) {
                System.err.println("Error creando el archivo JSON: " + e.getMessage());
            }
        }
    }

    // Guardar bicicletas en el archivo JSON
    public void saveStationsToFile() {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(stations, writer);
            System.out.println("Archivo guardado en: " + FILE_PATH);
        } catch (IOException e) {
            System.err.println("Error guardando el archivo JSON en " + FILE_PATH + ": " + e.getMessage());
        }
    }

    public List<Station> getAllStations() {
        return stations; // Asumiendo que 'bikes' es la lista de todas las bicicletas
    }
    

}
