package be.pxl.researchproject.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.time.LocalDate;
import java.util.*;

import be.pxl.researchproject.repository.HorseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import be.pxl.researchproject.exception.FoalNotFoundException;
import be.pxl.researchproject.repository.FoalRepository;
import be.pxl.researchproject.service.Impl.FoalServiceImpl;
import be.pxl.researchproject.api.request.*;
import be.pxl.researchproject.api.response.FoalDTO;
import be.pxl.researchproject.domain.Foal;

@ExtendWith(MockitoExtension.class)
class FoalServiceTest {

    @Mock
    private FoalRepository foalRepository;

    @Mock
    private HorseRepository horseRepository;

    @InjectMocks
    private FoalServiceImpl foalServiceImpl;

    @Test
    void testCreateFoalReturnsFoalId() {
        CreateFoalRequest request = new CreateFoalRequest("Ember Sprite", 1L, LocalDate.now());
        Foal foal = new Foal(request.name(), request.birthDate(), null);
        foal.setId(1L);
        when(foalRepository.save(any(Foal.class))).thenReturn(foal);
        Long id = foalServiceImpl.createFoal(request);
        assertEquals(1L, id);
    }

    @Test
    void testGetFoalByIdWithValidIdReturnsFoalDTO() {
        Foal foal = new Foal("Ember Sprite", LocalDate.now(), null);
        foal.setId(1L);
        when(foalRepository.findById(1L)).thenReturn(Optional.of(foal));
        FoalDTO foalDTO = foalServiceImpl.getFoalById(1L);
        assertEquals(foal.getName(), foalDTO.name());
        assertEquals(foal.getDewormingSchedule(), foalDTO.dewormingSchedule());
    }

    @Test
    void testGetFoalByIdWithInvalidIdThrowsFoalNotFoundException() {
        when(foalRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(FoalNotFoundException.class, () -> foalServiceImpl.getFoalById(1L));
    }

    @Test
    void testGetAllFoalsReturnsListOfFoalDTOs() {
        List<Foal> foals = Arrays.asList(
                new Foal("Ember Sprite", LocalDate.now(), null),
                new Foal("Stardust Whisper", LocalDate.now(), null));
        foals.get(0).setId(1L);
        foals.get(1).setId(2L);
        when(foalRepository.findAll()).thenReturn(foals);
        List<FoalDTO> foalDTOs = foalServiceImpl.getAllFoals();
        assertEquals(2, foalDTOs.size());
        assertTrue(foalDTOs.stream().anyMatch(dto -> dto.name().equals("Ember Sprite")));
        assertTrue(foalDTOs.stream().anyMatch(dto -> dto.name().equals("Stardust Whisper")));
    }

    @Test
    void testUpdateFoalWithValidIdUpdatesFoalAndReturnsFoalDTO() {
        UpdateFoalRequest request = new UpdateFoalRequest("Moonlit Sprite", LocalDate.now());
        Foal foal = new Foal("Wildfire Sparkle", LocalDate.now(), null);
        foal.setId(1L);
        when(foalRepository.findById(1L)).thenReturn(Optional.of(foal));
        when(foalRepository.save(foal)).thenReturn(foal);
        FoalDTO updatedFoalDTO = foalServiceImpl.updateFoal(1L, request);
        assertEquals(request.name(), updatedFoalDTO.name());
        assertEquals(request.birthDate(), foal.getBirthDate());
        verify(foalRepository, times(1)).findById(1L);
        verify(foalRepository, times(1)).save(any(Foal.class));
    }

    @Test
    void testUpdateFoalWithInvalidIdThrowsFoalNotFoundException() {
        UpdateFoalRequest request = new UpdateFoalRequest("Moonlit Sprite", LocalDate.now());
        when(foalRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(FoalNotFoundException.class, () -> foalServiceImpl.updateFoal(1L, request));
        verify(foalRepository, times(1)).findById(1L);
        verify(foalRepository, never()).save(any(Foal.class));
    }

    @Test
    void testDeleteFoalWithValidIdDeletesFoalAndReturnsTrue() {
        long foalId = 1L;
        when(foalRepository.findById(foalId)).thenReturn(Optional.of(new Foal()));
        boolean deleted = foalServiceImpl.deleteFoal(foalId);
        assertTrue(deleted);
        verify(foalRepository, times(1)).findById(foalId);
        verify(foalRepository, times(1)).delete(any(Foal.class));
    }

    @Test
    void testDeleteFoalWithInvalidIdThrowsFoalNotFoundException() {
        long foalId = 1L;
        when(foalRepository.findById(foalId)).thenReturn(Optional.empty());
        FoalNotFoundException exception = assertThrows(FoalNotFoundException.class, () -> {
            foalServiceImpl.deleteFoal(foalId);
        });
        assertEquals("Foal not found with id " + foalId, exception.getMessage());
        verify(foalRepository, times(1)).findById(foalId);
        verify(foalRepository, never()).delete(any(Foal.class));
    }

}
