package be.pxl.researchproject.service.Impl;

import be.pxl.researchproject.api.response.HorseDetailDTO;
import be.pxl.researchproject.domain.Horse;
import be.pxl.researchproject.repository.HorseRepository;
import be.pxl.researchproject.service.HorseService;
import be.pxl.researchproject.service.calculation.AgeCalculationService;
import be.pxl.researchproject.service.calculation.PregnancyCalculationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class HorseServiceImpl implements HorseService {
    
    private final HorseRepository horseRepository;
    private final AgeCalculationService ageCalculationService;
    private final PregnancyCalculationService pregnancyCalculationService;
    
    public HorseServiceImpl(
            HorseRepository horseRepository,
            AgeCalculationService ageCalculationService,
            PregnancyCalculationService pregnancyCalculationService) {
        this.horseRepository = horseRepository;
        this.ageCalculationService = ageCalculationService;
        this.pregnancyCalculationService = pregnancyCalculationService;
    }
    
    @Override
    public HorseDetailDTO getHorseDetails(Long id) {
        Horse horse = horseRepository.findById(id)
            .orElseThrow(() -> new HorseNotFoundException("Horse not found with id: " + id));
            
        String age = ageCalculationService.calculateAge(horse.getBirthDate());
        LocalDate expectedBirthDate = null;
        long daysPregnant = -1;
        long remainingDays = -1;
        
        if (horse.isPregnant() && horse.getDatePregnant() != null) {
            expectedBirthDate = pregnancyCalculationService.calculateExpectedBirthDate(horse.getDatePregnant());
            daysPregnant = pregnancyCalculationService.calculateDaysPregnant(horse.getDatePregnant());
            remainingDays = pregnancyCalculationService.calculateRemainingDays(horse.getDatePregnant());
        }
        
        return new HorseDetailDTO(
            horse.getId(),
            horse.getName(),
            horse.getBirthDate(),
            age,
            horse.getHeight(),
            horse.isPregnant(),
            horse.getStudHorse(),
            horse.getDatePregnant(),
            expectedBirthDate,
            daysPregnant,
            remainingDays
        );
    }
    
    // Other existing methods...
}