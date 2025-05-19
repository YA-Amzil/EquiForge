package be.pxl.researchproject.service;

import be.pxl.researchproject.api.request.*;
import be.pxl.researchproject.api.response.*;
import java.time.LocalDate;
import be.pxl.researchproject.domain.*;
import java.util.*;

public interface HorseService {
    Long createHorse(CreateHorseRequest createHorseRequest);

    void createFertilizationDate(Long id, LocalDate date);

    void setPregnancyDate(Long id, LocalDate date);

    LocalDate getPregnancyDate(Long id);

    void updateHorse(Long id, UpdateHorseRequest updateHorseRequest);

    List<HorseListItemDTO> getHorses();

    HorseDTO getHorseById(Long id);

    HorseUpdateDTO getUpdatableHorseById(Long id);

    void deleteHorseById(Long id);

    List<LocalDate> getPlannedDatesByHorseId(long id);

    void deletePlannedDateForHorse(long id, LocalDate date);

    Map<String, String> getBirthDiariesByHorseId(long id);

    void addFoal(Horse mockedHorse, Foal mockedFoal);

    void updateHorseDiary(long id, UpdateHorseDiaryRequest updateHorseDiaryRequest);

    void createHorseDiary(long id, CreateHorseDiaryRequest createHorseDiaryRequest);

    List<Foal> getFoalsByHorseId(long id);
}
