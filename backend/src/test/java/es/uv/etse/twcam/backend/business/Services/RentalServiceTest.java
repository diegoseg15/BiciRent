package es.uv.etse.twcam.backend.business.Services;

import es.uv.etse.twcam.backend.business.Rental;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



class RentalServiceTest {

    private RentalService rentalService;

    @BeforeEach
    void setUp() {
        rentalService = new RentalService() {
            @Override
            public void loadRentalFromFile() {
                rentas = new ArrayList<>();
                rentas.add(new Rental("12345678A", "B001", "2023-01-01T10:00", 10, "activa"));
                rentas.add(new Rental("87654321B", "B002", "2023-01-02T11:00", 15, "devuelta"));
            }

            @Override
            public void saveRentasToFile() {
                // No guardar en archivo durante los tests
            }
        };
    }

    @Test
    void testRenta() {
        // Registro válido
        boolean result = rentalService.renta("55555555C", "B003", "2023-01-03T12:00", 20, "activa");
        assertTrue(result);

        // Registrar renta ya existente y devuelta
        result = rentalService.renta("87654321B", "B002", "2023-01-02T11:00", 15, "devuelta");
        assertFalse(result);
    }

    @Test
    void testGetRental() {
        // Obtener renta existente
        Rental rental = rentalService.getRental("12345678A", "B001", "2023-01-01T10:00");
        assertNotNull(rental);
        assertEquals("12345678A", rental.getUsuarioId());

        // Obtener renta no existente
        rental = rentalService.getRental("noexist", "B999", "2023-01-01T10:00");
        assertNull(rental);
    }

    @Test
    void testDevolucion() {
        // Devolución válida
        boolean result = rentalService.devolucion("12345678A", "B001", "2023-01-01T10:00", 12, "devuelta");
        assertTrue(result);

        // Devolución de renta no existente
        result = rentalService.devolucion("noexist", "B999", "2023-01-01T10:00", 12, "devuelta");
        assertFalse(result);
    }
}
