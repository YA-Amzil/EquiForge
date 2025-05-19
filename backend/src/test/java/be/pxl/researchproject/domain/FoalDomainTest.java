package be.pxl.researchproject.domain;

import static org.junit.jupiter.api.Assertions.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Map;
import org.junit.jupiter.api.*;
import be.pxl.researchproject.exception.InvalidDewormingScheduleException;

class FoalDomainTest {

    private Foal foal;

    @BeforeEach
    void setup() {
        foal = new Foal("Daisy", LocalDate.now(), new Horse());
    }

    @Test
    void testCalculatedewormingScheduleShouldAddValidSchedule() {
        foal.calculatedewormingSchedule();
        Map<Date, String> dewormingSchedule = foal.getDewormingSchedule();
        assertFalse(dewormingSchedule.isEmpty());
        assertEquals(4, dewormingSchedule.size());
    }

    @Test
    void testCalculatedewormingScheduleShouldThrowExceptionWhenDateOrNameNotSet() {
        foal.setName(null);
        foal.setBirthDate(null);
        assertThrows(InvalidDewormingScheduleException.class, () -> foal.calculatedewormingSchedule());
    }

}
