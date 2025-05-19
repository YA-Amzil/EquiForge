package be.pxl.researchproject.service.calculation;

import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class PregnancyCalculationService {
    
    private static final int PREGNANCY_DURATION_DAYS = 320;
    
    public LocalDate calculateExpectedBirthDate(LocalDate pregnancyStartDate) {
        if (pregnancyStartDate == null) {
            return null;
        }
        return pregnancyStartDate.plusDays(PREGNANCY_DURATION_DAYS);
    }
    
    public long calculateDaysPregnant(LocalDate pregnancyStartDate) {
        if (pregnancyStartDate == null) {
            return -1;
        }
        return ChronoUnit.DAYS.between(pregnancyStartDate, LocalDate.now());
    }
    
    public long calculateRemainingDays(LocalDate pregnancyStartDate) {
        if (pregnancyStartDate == null) {
            return -1;
        }
        LocalDate expectedDate = calculateExpectedBirthDate(pregnancyStartDate);
        return ChronoUnit.DAYS.between(LocalDate.now(), expectedDate);
    }
}