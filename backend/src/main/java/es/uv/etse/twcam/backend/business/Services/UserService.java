package es.uv.etse.twcam.backend.business.Services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import es.uv.etse.twcam.backend.business.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private static final String FILE_PATH = System.getProperty("user.dir") + "/backend/data.json";
    private List<User> users = new ArrayList<>();
    private final Gson gson = new Gson();

    public UserService() {
        ensureFileAndFolderExists();
        loadUsersFromFile(); // Cargar usuarios desde el archivo JSON
    }

    // Leer usuarios desde el archivo JSON
    private void loadUsersFromFile() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            try (InputStreamReader reader = new InputStreamReader(new FileInputStream(FILE_PATH))) {
                // Leer el archivo JSON y convertirlo en lista de objetos User
                Type listType = new TypeToken<List<User>>() {
                }.getType();
                // Leer el archivo JSON y obtener la lista de usuarios
                users = gson.fromJson(reader, listType);

                if (users == null) {
                    users = new ArrayList<>();
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
                    // Inicializa el archivo con la estructura básica
                    try (FileWriter writer = new FileWriter(file)) {
                        // Inicia el archivo con una lista vacía de usuarios
                        writer.write("[]");
                    }
                }
            } catch (IOException e) {
                System.err.println("Error creando el archivo JSON: " + e.getMessage());
            }
        }
    }

    // Guardar usuarios en el archivo JSON
    private void saveUsersToFile() {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(users, writer);
            System.out.println("Archivo guardado en: " + FILE_PATH);
        } catch (IOException e) {
            System.err.println("Error guardando el archivo JSON en " + FILE_PATH + ": " + e.getMessage());
        }
    }

    // Método de login
    public User login(String correo, String password) {
        // Buscar el usuario por correo y contraseña

        System.out.println(users);

        return users.stream()
                .filter(user -> user.getCorreo().equals(correo) && user.getPassword().equals(password))
                .findFirst()
                .orElse(null); // Si no se encuentra, devuelve null
    }

    // Método de registro
    public boolean register(String dni, String nombre, String apellido, String telefono, String correo, String password,
            String rol) {
        // Verificar si el correo ya está registrado
        boolean userExists = users.stream()
                .anyMatch(user -> user.getCorreo().equals(correo));

        if (userExists) {
            return false; // El usuario ya existe
        }

        // Crear nuevo usuario y agregarlo a la lista
        User newUser = new User(dni, nombre, apellido, telefono, correo, password, rol);
        users.add(newUser);
        saveUsersToFile(); // Guardar cambios en el archivo JSON
        return true; // Registro exitoso
    }
}
