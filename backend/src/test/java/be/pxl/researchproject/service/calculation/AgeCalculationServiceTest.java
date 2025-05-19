package be.pxl.researchproject.service.calculation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class AgeCalculationServiceTest {
    
    private AgeCalculationService ageCalculationService;
    
    @BeforeEach
    void setUp() {
        ageCalculationService = new AgeCalculationService();
    }
    
    @Test
    void calculateAge_WithNullDate_ReturnsUnknown() {
        assertEquals("Unknown", ageCalculationService.calculateAge(null));
    }
    
    @Test
    void calculateAge_WithValidDate_ReturnsCorrectFormat() {
        LocalDate birthDate = LocalDate.now().minusYears(2).minusMonths(3).minusDays(15);
        String age = ageCalculationService.calculateAge(birthDate);
        assertTrue(age.matches("\\d+ j \\d+ m \\d+ d"));
    }
    
    @Test
    void calculateDaysInMonth_February_NonLeapYear() {
        assertEquals(28, ageCalculationService.calculateDaysInMonth(2023, 1));
    }
    
    @Test
    void calculateDaysInMonth_February_LeapYear() {
        assertEquals(29, ageCalculationService.calculateDaysInMonth(2024, 1));
    }
}