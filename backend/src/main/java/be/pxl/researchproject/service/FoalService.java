package be.pxl.researchproject.service;

import be.pxl.researchproject.api.response.FoalDTO;
import be.pxl.researchproject.domain.Foal;
import be.pxl.researchproject.domain.Horse;
import be.pxl.researchproject.exception.FoalNotFoundException;
import be.pxl.researchproject.repository.FoalRepository;
import be.pxl.researchproject.repository.HorseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class FoalService {

    private final FoalRepository foalRepository;
    private final HorseRepository horseRepository;
    private final DewormingSchedulerService dewormingSchedulerService;

    public FoalService(FoalRepository foalRepository, HorseRepository horseRepository, DewormingSchedulerService dewormingSchedulerService) {
        this.foalRepository = foalRepository;
        this.horseRepository = horseRepository;
        this.dewormingSchedulerService = dewormingSchedulerService;
    }

    public Long createFoal(String name, LocalDate birthDate, Long motherHorseId) {
        Horse motherHorse = horseRepository.findById(motherHorseId)
                .orElseThrow(() -> new FoalNotFoundException("Horse not found"));

        Foal foal = new Foal(name, birthDate, motherHorse);
        foal.setDewormingSchedule(dewormingSchedulerService.generateDewormingSchedule(birthDate));

        return foalRepository.save(foal).getId();
    }

    public FoalDTO getFoalById(Long id) {
        return foalRepository.findById(id)
                .map(FoalDTO::new)
                .orElseThrow(() -> new FoalNotFoundException("Foal not found"));
    }

    public List<FoalDTO> getAllFoals() {
        return foalRepository.findAll().stream().map(FoalDTO::new).toList();
    }

    public void deleteFoal(Long id) {
        Foal foal = foalRepository.findById(id).orElseThrow(() -> new FoalNotFoundException("Foal not found"));
        foalRepository.delete(foal);
    }
}

