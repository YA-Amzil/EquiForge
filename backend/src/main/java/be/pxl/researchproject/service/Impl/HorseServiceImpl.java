package be.pxl.researchproject.service.Impl;

import be.pxl.researchproject.api.request.*;
import be.pxl.researchproject.api.response.*;
import be.pxl.researchproject.domain.*;
import be.pxl.researchproject.exception.HorseNotFoundException;
import be.pxl.researchproject.repository.*;
import be.pxl.researchproject.service.HorseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
public class HorseServiceImpl implements HorseService {
    private final HorseRepository horseRepository;
    private final FoalRepository foalRepository;

    @Autowired
    public HorseServiceImpl(HorseRepository horseRepository, FoalRepository foalRepository) {
        this.horseRepository = horseRepository;
        this.foalRepository = foalRepository;
    }

    @Override
    public List<HorseListItemDTO> getHorses() {
        return horseRepository.findAll()
                .stream()
                .map(horse -> new HorseListItemDTO(horse.getId(), horse.getName(), horse.isPregnant(),
                        horse.getDaysThatHorseIsPregnant(), horse.getDaysBetweenNowAndPregnancyDate(),
                        horse.getStudHorse(), horse.getExpectedPregnancyDate()))
                .sorted(Comparator.comparing(HorseListItemDTO::daysPregnant).reversed()
                        .thenComparing(HorseListItemDTO::name))
                .toList();
    }

    @Override
    public Long createHorse(CreateHorseRequest createHorseRequest) {
        return horseRepository.save(new Horse(createHorseRequest.name(), createHorseRequest.birthDate(),
                createHorseRequest.height(), createHorseRequest.isPregnant(), createHorseRequest.studHorse(),
                createHorseRequest.datePregnant())).getId();
    }

    @Override
    public void createFertilizationDate(Long id, LocalDate date) {
        horseRepository.findById(id)
                .ifPresentOrElse(horse -> {
                    horse.addPlannedDate(date);
                    horseRepository.save(horse);
                }, () -> {
                    throw new HorseNotFoundException("Horse not found, do you have the right Id?");
                });
    }

    @Override
    public void setPregnancyDate(Long id, LocalDate date) {
        horseRepository.findById(id)
                .ifPresentOrElse(horse -> {
                    horse.setPregnant(true);
                    System.out.println("Pregstatus: " + horse.isPregnant());
                    horse.setDatePregnant(date);
                    System.out.println("Pregnant date: " + horse.getDatePregnant());
                    horseRepository.save(horse);
                }, () -> {
                    throw new HorseNotFoundException("Horse not found, do you have the right Id?");
                });
    }

    @Override
    public LocalDate getPregnancyDate(Long id) {
        return horseRepository.findById(id)
                .map(Horse::getDatePregnant)
                .orElseThrow(() -> new HorseNotFoundException("Horse Id not found, are you sure?"));
    }

    @Override
    public void deletePlannedDateForHorse(long id, LocalDate date) {
        horseRepository.findById(id)
                .ifPresentOrElse(horse -> horse.removePlannedDate(date),
                        () -> {
                            throw new HorseNotFoundException("Horse Id not found, are you sure?");
                        });
    }

    @Override
    public HorseDTO getHorseById(Long id) {
        return horseRepository.findById(id)
                .map(horse -> new HorseDTO(horse.getName(), horse.getBirthDate(), horse.getHeight(), horse.isPregnant(),
                        horse.getStudHorse()))
                .orElseThrow(() -> new HorseNotFoundException("Horse Id not found, are you sure?"));
    }

    @Override
    public void updateHorse(Long id, UpdateHorseRequest updateHorseRequest) {
        Horse horse = horseRepository.findById(id)
                .orElseThrow(() -> new HorseNotFoundException("Horse Id not found, are you sure?"));
        horse.setName(updateHorseRequest.name());
        horse.setBirthDate(updateHorseRequest.birthDate());
        horse.setHeight(updateHorseRequest.height());
        horse.setPregnant(updateHorseRequest.isPregnant());
        horse.setStudHorse(updateHorseRequest.studHorse());
        horse.setDatePregnant(updateHorseRequest.datePregnant());
        horseRepository.save(horse);
    }

    @Override
    public HorseUpdateDTO getUpdatableHorseById(Long id) {
        return horseRepository.findById(id)
                .map(horse -> new HorseUpdateDTO(horse.getName(), horse.getBirthDate(), horse.getHeight(), horse.isPregnant(),
                        horse.getStudHorse(), horse.getDatePregnant()))
                .orElseThrow(() -> new HorseNotFoundException("Horse Id not found, are you sure?"));
    }

    @Override
    public void deleteHorseById(Long id) {
        horseRepository.findById(id)
                .ifPresentOrElse(horse -> {
                    horse.getFoals().forEach(v -> {
                        v.setMotherHorse(null);
                    });
                    horseRepository.deleteById(id);
                        },
                        () -> {
                            throw new HorseNotFoundException("Horse Id not found, are you sure?");
                        });
    }

    @Override
    public List<LocalDate> getPlannedDatesByHorseId(long id) {
        return horseRepository.findById(id)
                .map(Horse::getPlannedDates)
                .orElseThrow(() -> new HorseNotFoundException("Horse Id not found, are you sure?"));
    }

    @Override
    public Map<String, String> getBirthDiariesByHorseId(long id) {
        return horseRepository.findById(id)
                .map(Horse::getBirthDiaries)
                .orElseThrow(() -> new HorseNotFoundException("Horse Id not found, are you sure?"));
    }

    @Override
    public void updateHorseDiary(long id, UpdateHorseDiaryRequest updateHorseDiaryRequest) {
        horseRepository.findById(id)
                .ifPresentOrElse(horse -> {
                    horse.updateDiary(updateHorseDiaryRequest.name(), updateHorseDiaryRequest.text());
                    horseRepository.save(horse);
                }, () -> {
                    throw new HorseNotFoundException("Horse Id not found, are you sure?");
                });
    }

    @Override
    public void createHorseDiary(long id, CreateHorseDiaryRequest createHorseDiaryRequest) {
        horseRepository.findById(id)
                .ifPresentOrElse(horse -> {
                    horse.createDiary(createHorseDiaryRequest.name());
                    horseRepository.save(horse);
                }, () -> {
                    throw new HorseNotFoundException("Horse Id not found, are you sure?");
                });
    }

    @Override
    public List<Foal> getFoalsByHorseId(long id){
        return horseRepository.findById(id)
                .map(Horse::getFoals)
                .orElseThrow(() -> new HorseNotFoundException("Horse Id not found, are you sure?"));
    }

    @Override
    public void addFoal(Horse mockedHorse, Foal mockedFoal) {
        mockedHorse.addFoal(mockedFoal);
    }
}
