package es.uv.etse.twcam.backend.business.Services;

import es.uv.etse.twcam.backend.business.Station;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StationServiceTest {

    private StationService stationService;
    private Mockery mockery;

    @BeforeEach
    void setUp() {
        // Crear el mockery para las expectativas
        mockery = new Mockery();

        // Crear una instancia del servicio StationService
        stationService = new StationService() {
            @Override
            public void loadStationsFromFile() {
                // Simular el contenido de la lista de estaciones
                stations = new ArrayList<>();
                stations.add(new Station("S001", "Estación Central"));
                stations.add(new Station("S002", "Estación Norte"));
                stations.add(new Station("S003", "Estación Sur"));
            }

            @Override
            public void saveStationsToFile() {
                // No guardar en archivo durante los tests
            }
        };
    }

    @Test
    void testGetAllStations() {
        // Cargar estaciones simuladas
        stationService.loadStationsFromFile();

        // Llamar al método que estamos probando
        List<Station> result = stationService.getAllStations();

        // Asegurarnos de que el resultado no sea nulo y tenga las estaciones simuladas
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("S001", result.get(0).getId());
        assertEquals("Estación Central", result.get(0).getNombre());
        assertEquals("S002", result.get(1).getId());
        assertEquals("Estación Norte", result.get(1).getNombre());
        assertEquals("S003", result.get(2).getId());
        assertEquals("Estación Sur", result.get(2).getNombre());
    }
}
