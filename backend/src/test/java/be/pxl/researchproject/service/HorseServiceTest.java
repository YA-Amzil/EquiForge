package be.pxl.researchproject.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import be.pxl.researchproject.api.request.*;
import be.pxl.researchproject.api.response.*;
import be.pxl.researchproject.domain.*;
import be.pxl.researchproject.exception.HorseNotFoundException;
import be.pxl.researchproject.repository.*;
import be.pxl.researchproject.service.Impl.HorseServiceImpl;

@ExtendWith(MockitoExtension.class)
class HorseServiceTest {

    @Mock
    private HorseRepository horseRepository;

    @Mock
    private FoalRepository foalRepository;

    @InjectMocks
    private HorseServiceImpl horseServiceImpl;

    @Test
    void testAddFoalShouldAddFoalToListOfFoals() {
        Horse mockedHorse = Mockito.mock(Horse.class);
        Foal mockedFoal = Mockito.mock(Foal.class);
        horseServiceImpl.addFoal(mockedHorse, mockedFoal);
        Mockito.verify(mockedHorse, Mockito.times(1)).addFoal(mockedFoal);
    }

    @Test
    void testGetHorses() {
        when(horseRepository.findAll()).thenReturn(Arrays.asList(
                new Horse("Mystic Mirage", LocalDate.now(), 160.0, false, "Sunlit Serenade", LocalDate.now()),
                new Horse("Shadow Whisperer", LocalDate.now(), 150.0, true, "Stardust Whisper", LocalDate.now())));
        List<HorseListItemDTO> horseList = horseServiceImpl.getHorses();
        assertEquals(2, horseList.size());
        assertEquals("Shadow Whisperer", horseList.get(0).name());
        assertEquals("Mystic Mirage", horseList.get(1).name());
    }

    @Test
    void testCreateHorseReturnsHorseId() {
        CreateHorseRequest request = new CreateHorseRequest("Whispering Guardian", LocalDate.now(), 1.7, "Aurora Echo",
                true,
                LocalDate.now());
        Horse horse = new Horse(request.name(), request.birthDate(), request.height(), request.isPregnant(),
                request.studHorse(), request.datePregnant());
        horse.setId(1L);
        when(horseRepository.save(any(Horse.class))).thenReturn(horse);
        Long id = horseServiceImpl.createHorse(request);
        assertEquals(1L, id);
    }

    @Test
    void testGetHorseById() {
        Long id = 1L;
        when(horseRepository.findById(id))
                .thenReturn(Optional.of(
                        new Horse("Mystic Mirage", LocalDate.now(), 160.0, false, "Sunlit Serenade", LocalDate.now())));
        HorseDTO horseDTO = horseServiceImpl.getHorseById(id);
        assertNotNull(horseDTO);
        assertEquals("Mystic Mirage", horseDTO.name());
        assertEquals(LocalDate.now(), horseDTO.birthDate());
        assertEquals(160.0, horseDTO.height());
        assertFalse(horseDTO.isPregnant());
        assertEquals("Sunlit Serenade", horseDTO.studHorse());
    }

    @Test
    void testGetHorseByIdWithInvalidIdThrowsFoalNotFoundException() {
        when(horseRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(HorseNotFoundException.class, () -> horseServiceImpl.getHorseById(1L));
        assertThrows(HorseNotFoundException.class, () -> horseServiceImpl.getHorseById(1L));
    }

    @Test
    void testUpdateHorseWithValidIdUpdatesHorse() {
        UpdateHorseRequest request = new UpdateHorseRequest("Aurora Echo", LocalDate.now(), 1.8, "Stardust Voyager",
                true, LocalDate.of(2023, 5, 23));
        Horse horse = new Horse(request.name(), request.birthDate(),
                request.height(), request.isPregnant(), request.studHorse(),
                request.datePregnant());
        when(horseRepository.findById(1L)).thenReturn(Optional.of(horse));
        horseServiceImpl.updateHorse(1L, request);
        assertEquals(request.name(), horse.getName());
        assertEquals(request.birthDate(), horse.getBirthDate());
        assertEquals(request.height(), horse.getHeight());
        assertEquals(request.isPregnant(), horse.isPregnant());
        assertEquals(request.studHorse(), horse.getStudHorse());
        verify(horseRepository, times(1)).findById(1L);
        verify(horseRepository, times(1)).save(any(Horse.class));
    }

    @Test
    void testUpdateHorseWithInvalidIdThrowsHorseIdNotFoundException() {
        UpdateHorseRequest request = new UpdateHorseRequest("Aurora Echo", LocalDate.now(), 1.8, "Stardust Voyager",
                true, LocalDate.of(2023, 5, 23));
        Long horseId = 1L;
        when(horseRepository.findById(horseId)).thenReturn(Optional.empty());
        assertThrows(HorseNotFoundException.class, () -> horseServiceImpl.updateHorse(horseId, request));
        verify(horseRepository, times(1)).findById(horseId);
        verify(horseRepository, never()).save(any(Horse.class));
    }

    @Test
    void testDeleteHorseByIdWithValidIdDeletesHorse() {
        Long id = 1L;
        Horse horse = new Horse("Mystic Mirage", LocalDate.now(), 160.0, false, "Sunlit Serenade", LocalDate.now());
        when(horseRepository.findById(id)).thenReturn(Optional.of(horse));
        horseServiceImpl.deleteHorseById(id);
        verify(horseRepository, times(1)).findById(id);
    }

    @Test
    void testDeleteHorseByIdWithInvalidIdThrowsHorseIdNotFoundException() {
        Long invalidId = 2L;
        when(horseRepository.findById(invalidId)).thenReturn(Optional.empty());
        assertThrows(HorseNotFoundException.class, () -> horseServiceImpl.deleteHorseById(invalidId));
        verify(horseRepository, times(1)).findById(invalidId);
        verify(horseRepository, never()).delete(any());
    }

    @Test
    void testCreateFertilizationDateWithValidIdAddsDate() {
        Long id = 1L;
        LocalDate date = LocalDate.of(2024, 5, 16);
        Horse horse = new Horse("Mystic Mirage", LocalDate.now(), 160.0, false, "Sunlit Serenade", LocalDate.now());
        when(horseRepository.findById(id)).thenReturn(Optional.of(horse));
        horseServiceImpl.createFertilizationDate(id, date);
        assertEquals(1, horse.getPlannedDates().size());
        verify(horseRepository, times(1)).findById(id);
        verify(horseRepository, times(1)).save(horse);
    }

    @Test
    void testCreateFertilizationDateWithInvalidIdThrowsHorseNotFoundException() {
        Long invalidId = 2L;
        LocalDate date = LocalDate.of(2024, 5, 16);
        when(horseRepository.findById(invalidId)).thenReturn(Optional.empty());
        assertThrows(HorseNotFoundException.class, () -> horseServiceImpl.createFertilizationDate(invalidId, date));
        verify(horseRepository, times(1)).findById(invalidId);
        verify(horseRepository, never()).save(any());
    }

    @Test
    void testGetPlannedDatesByValidHorseIdReturnsDates() {
        Long id = 1L;
        Horse horse = new Horse("Mystic Mirage", LocalDate.now(), 160.0, false, "Sunlit Serenade", LocalDate.now());
        horse.addPlannedDate(LocalDate.of(2024, 5, 16));
        when(horseRepository.findById(id)).thenReturn(Optional.of(horse));
        List<LocalDate> dates = horseServiceImpl.getPlannedDatesByHorseId(id);
        assertEquals(1, dates.size());
        assertEquals(LocalDate.of(2024, 5, 16), dates.get(0));
        verify(horseRepository, times(1)).findById(id);
    }

    @Test
    void testGetPlannedDatesByInvalidHorseIdThrowsHorseNotFoundException() {
        Long invalidId = 2L;
        when(horseRepository.findById(invalidId)).thenReturn(Optional.empty());
        assertThrows(HorseNotFoundException.class, () -> horseServiceImpl.getPlannedDatesByHorseId(invalidId));
        verify(horseRepository, times(1)).findById(invalidId);
    }

    @Test
    void testGetBirthDiariesByValidHorseIdReturnsDiaries() {
        Long id = 1L;
        Horse horse = new Horse("Mystic Mirage", LocalDate.now(), 160.0, false, "Sunlit Serenade", LocalDate.now());
        horse.createDiary("First Diary");
        horse.updateDiary("First Diary", "This is the first diary entry.");
        when(horseRepository.findById(id)).thenReturn(Optional.of(horse));
        Map<String, String> diaries = horseServiceImpl.getBirthDiariesByHorseId(id);
        assertEquals(1, diaries.size());
        assertEquals("This is the first diary entry.", diaries.get("First Diary"));
        verify(horseRepository, times(1)).findById(id);
    }

    @Test
    void testGetBirthDiariesByInvalidHorseIdThrowsHorseNotFoundException() {
        Long invalidId = 2L;
        when(horseRepository.findById(invalidId)).thenReturn(Optional.empty());
        assertThrows(HorseNotFoundException.class, () -> horseServiceImpl.getBirthDiariesByHorseId(invalidId));
        verify(horseRepository, times(1)).findById(invalidId);
    }

}
