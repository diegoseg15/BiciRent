package es.uv.etse.twcam.backend.business.Services;

import es.uv.etse.twcam.backend.business.Bike;
import org.jmock.Mockery;
import org.jmock.Expectations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BikeServiceTest {

    private Mockery mockery; // Inicializamos el mockery
    private BikeService bikeService;

    @BeforeEach
    void setUp() {
        // Inicializamos el mockery antes de su uso
        mockery = new Mockery();

        // Creamos la instancia del servicio
        bikeService = new BikeService() {
            // Aquí puedes simular la carga de bicicletas
            @Override
            public void loadBikesFromFile() {
                List<Bike> bikes = new ArrayList<>();
                bikes.add(new Bike("1", "Bicicleta1", 100, "Estacion1"));
                bikes.add(new Bike("2", "Bicicleta2", 150, "Estacion2"));
                bikes.add(new Bike("3", "Bicicleta3", 200, "Estacion1"));
                this.bikes = bikes;  // Asignamos las bicicletas a la lista interna
            }

            @Override
            public void saveBikesToFile() {
                // No se realiza ninguna operación real en los tests
            }
        };
    }

    @Test
    void testGetBikesByEstacionId() {
        // Cargar las bicicletas simuladas
        bikeService.loadBikesFromFile();

        List<Bike> result = bikeService.getBikesByEstacionId("Estacion1");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(bike -> bike.getEstacionId().equals("Estacion1")));
    }

    @Test
    void testGetAllBikes() {
        // Cargar las bicicletas simuladas
        bikeService.loadBikesFromFile();

        List<Bike> result = bikeService.getAllBikes();

        assertNotNull(result);
        assertEquals(3, result.size());
    }
}
