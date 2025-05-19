package be.pxl.researchproject.domain;

import org.junit.jupiter.api.*;
import be.pxl.researchproject.exception.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import java.time.LocalDate;

class HorseDomainTest {
    private Horse horse;

    @BeforeEach
    void setup() {
        horse = new Horse("Mystic Mirage", LocalDate.of(2024, 11, 23), 1.40, true, "Nicky",
                LocalDate.of(2024, 12, 24));
    }

    @Test
    void testAddFoalShouldAddFoalToListOfFoals() {
        Foal mockedFoal = mock(Foal.class);
        horse.addFoal(mockedFoal);
        assertEquals(mockedFoal, horse.getFoals().get(0));
    }

    @Test
    void testRemoveFoalShouldRemoveFromListOfFoals() {
        Foal mockedFoal = mock(Foal.class);
        horse.addFoal(mockedFoal);
        horse.removeFoalById(mockedFoal.getId());
        assertTrue(horse.getFoals().isEmpty());
    }

    @Test
    void testCreateDiaryShouldCreateNewDiary() {
        horse.createDiary("Health");
        assertTrue(horse.getBirthDiaries().containsKey("Health"));
    }

    @Test
    void testUpdateDiaryShouldUpdateExistingDiary() {
        horse.createDiary("Health");
        horse.updateDiary("Health", "Updated health status");
        assertEquals("Updated health status", horse.getBirthDiaries().get("Health"));
    }

    @Test
    void testSetPregnantDateShouldUpdatePlannedDate() {
        LocalDate now = LocalDate.now();
        horse.addPlannedDate(now);
        horse.setPregnantDate(now);
        assertTrue(horse.getPlannedDates().contains(now));
        assertTrue(horse.getPlannedDates().size() == 1);
    }

    @Test
    void testAddPlannedDateShouldAddDateToPlannedDates() {
        LocalDate now = LocalDate.now();
        horse.addPlannedDate(now);
        assertTrue(horse.getPlannedDates().contains(now));
    }

    @Test
    void testAddPlannedDateShouldThrowExceptionIfDateAlreadyExists() {
        LocalDate now = LocalDate.now();
        horse.addPlannedDate(now);
        assertThrows(DateAlreadyAddedException.class, () -> horse.addPlannedDate(now));
    }

    @Test
    void testSetPregnantDateShouldThrowExceptionIfDateNotFound() {
        assertThrows(DateNotFoundException.class, () -> horse.setPregnantDate(LocalDate.now()));
    }
}