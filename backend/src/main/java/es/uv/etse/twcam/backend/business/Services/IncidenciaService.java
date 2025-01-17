package es.uv.etse.twcam.backend.business.Services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import es.uv.etse.twcam.backend.business.Incidencia;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class IncidenciaService {
    private static final String FILE_PATH = System.getProperty("user.dir") + "/backend/incidents.json";
    private final Gson gson = new Gson();

    public void IncidentService() {
        ensureFileAndFolderExists();
        leerIncidenciasDesdeArchivo(); // Cargar incidencias desde el archivo JSON
    }

    // Verificar que la carpeta y el archivo existen
    private void ensureFileAndFolderExists() {
        File backendFolder = new File(System.getProperty("user.dir") + "/backend/incidents.json");
        System.out.println("Ruta utilizada para incidents.json: " + FILE_PATH);
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

    protected List<Incidencia> leerIncidenciasDesdeArchivo() {
        try (Reader reader = new FileReader(FILE_PATH)) {
            Type listType = new TypeToken<List<Incidencia>>() {}.getType();
            List<Incidencia> incidencias = gson.fromJson(reader, listType);
            if (incidencias == null) {
                incidencias = new ArrayList<>();
            }
            System.out.println("Incidencias leídas: " + incidencias);
            return incidencias;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    protected void escribirIncidenciasEnArchivo(List<Incidencia> incidencias) {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(incidencias, writer);
            System.out.println("Incidencias guardadas: " + incidencias);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    public Incidencia registrarIncidencia(String fecha, String hora, String ubicacion,
            String descripcion, String estado, String situacion) {
        List<Incidencia> incidencias = leerIncidenciasDesdeArchivo();

        // Generar un ID único para la incidencia
        String incidenciaId = String.valueOf(incidencias.size() + 1);

        Incidencia nueva = new Incidencia(fecha, hora, ubicacion, descripcion, estado, situacion, incidenciaId, null);
        incidencias.add(nueva);
        escribirIncidenciasEnArchivo(incidencias);
        return nueva;
    }

    public List<Incidencia> obtenerPorEstado(String estado) {
        List<Incidencia> lista = leerIncidenciasDesdeArchivo().stream()
        .filter(incidencia -> incidencia.getEstado().equalsIgnoreCase(estado))
        .collect(Collectors.toList());
        System.out.println("lista: " + lista);

        return lista;
    }

    public void asignarIncidencia(String incidenciaId, String tecnico) {
        List<Incidencia> incidencias = leerIncidenciasDesdeArchivo();
        for (Incidencia incidencia : incidencias) {
            if (incidencia.getIncidenciaId().equals(incidenciaId)
                    && "reportada".equalsIgnoreCase(incidencia.getEstado())) {
                incidencia.setEstado("asignada");
                incidencia.setTecnico(tecnico);
                //System.out.println("Incidencia actualizada: " + incidencia);
                break;
            }
        }
        escribirIncidenciasEnArchivo(incidencias);
    }

    public List<Incidencia> obtenerTodas() {
        return leerIncidenciasDesdeArchivo();
    }

    public Incidencia obtenerPorId(String incidenciaId) {
        return leerIncidenciasDesdeArchivo().stream()
                .filter(incidencia -> incidencia.getIncidenciaId().equals(incidenciaId))
                .findFirst()
                .orElse(null);
    }
}
