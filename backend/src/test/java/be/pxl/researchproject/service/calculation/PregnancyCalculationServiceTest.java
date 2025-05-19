package be.pxl.researchproject.service.calculation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class PregnancyCalculationServiceTest {
    
    private PregnancyCalculationService pregnancyCalculationService;
    
    @BeforeEach
    void setUp() {
        pregnancyCalculationService = new PregnancyCalculationService();
    }
    
    @Test
    void calculateExpectedBirthDate_WithNullDate_ReturnsNull() {
        assertNull(pregnancyCalculationService.calculateExpectedBirthDate(null));
    }
    
    @Test
    void calculateExpectedBirthDate_WithValidDate_Returns320DaysLater() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate expectedDate = LocalDate.of(2024, 11, 16);
        assertEquals(expectedDate, pregnancyCalculationService.calculateExpectedBirthDate(startDate));
    }
    
    @Test
    void calculateDaysPregnant_WithNullDate_ReturnsNegativeOne() {
        assertEquals(-1, pregnancyCalculationService.calculateDaysPregnant(null));
    }
    
    @Test
    void calculateRemainingDays_WithNullDate_ReturnsNegativeOne() {
        assertEquals(-1, pregnancyCalculationService.calculateRemainingDays(null));
    }
}