package be.pxl.researchproject.service.Impl;

import java.util.List;

import be.pxl.researchproject.repository.HorseRepository;
import io.swagger.v3.core.util.Json;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import be.pxl.researchproject.api.request.*;
import be.pxl.researchproject.api.response.FoalDTO;
import be.pxl.researchproject.domain.Foal;
import be.pxl.researchproject.exception.FoalNotFoundException;
import be.pxl.researchproject.repository.FoalRepository;
import be.pxl.researchproject.service.FoalService;
import be.pxl.researchproject.domain.Horse;

@Service
@Transactional
public class FoalServiceImpl implements FoalService {

    private final FoalRepository foalRepository;

    private final HorseRepository horseRepository;

    public FoalServiceImpl(FoalRepository foalRepository, HorseRepository horseRepository) {
        this.foalRepository = foalRepository;
        this. horseRepository = horseRepository;
    }

    @Override
    public Long createFoal(CreateFoalRequest createFoalRequest) {
        Horse tempHorse = horseRepository.findById(createFoalRequest.horseId()).orElse(null);
        Foal newFoal = new Foal(createFoalRequest.name(), createFoalRequest.birthDate(), tempHorse);
        newFoal.calculatedewormingSchedule();
        return foalRepository.save(newFoal).getId();
    }

    @Override
    public FoalDTO getFoalById(Long id) {
        return foalRepository
                .findById(id)
                .map(FoalDTO::new)
                .orElseThrow(() -> new FoalNotFoundException("Foal not found with id " + id));
    }

    @Override
    public List<FoalDTO> getAllFoals() {
        return foalRepository
                .findAll()
                .stream()
                .map(FoalDTO::new)
                .toList();
    }

    @Override
    public FoalDTO updateFoal(Long id, UpdateFoalRequest updateFoalRequest) {
        return foalRepository
                .findById(id)
                .map(foal -> {
                    foal.setName(updateFoalRequest.name());
                    foal.setBirthDate(updateFoalRequest.birthDate());
                    Foal updatedFoal = foalRepository.save(foal);
                    return new FoalDTO(updatedFoal);
                })
                .orElseThrow(() -> new FoalNotFoundException("Foal not found with id " + id));
    }

    @Override
    public boolean deleteFoal(Long id) {
        return foalRepository
                .findById(id)
                .map(foal -> {
                    if(foal.getMotherHorse() != null){
                        foal.getMotherHorse().removeFoalById(foal.getId());
                        foal.setMotherHorse(null);
                    }
                    foalRepository.delete(foal);
                    return true;
                })
                .orElseThrow(() -> new FoalNotFoundException("Foal not found with id " + id));
    }

    @Override
    public String getSchedule(Long id) {
        String schedule = foalRepository.findById(id)
                            .map(Foal::getSchedule)
                            .orElseThrow(() -> new FoalNotFoundException("foal with id: " + id + "not found"));

        return Json.pretty(schedule);
    }
    @Override
    public void updateFoalSchedule(long id, UpdateFoalScheduleRequest updateFoalscheduleRequest) {
        foalRepository.findById(id)
                .ifPresentOrElse(foal -> {
                    foal.updateSchedule(updateFoalscheduleRequest.schedule());
                    foalRepository.save(foal);
                }, () -> {
                    throw new FoalNotFoundException("Foal Id not found, are you sure?");
                });
    }

}
