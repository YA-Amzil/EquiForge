package be.pxl.researchproject.service.calculation;

import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Period;

@Service
public class AgeCalculationService {
    
    public String calculateAge(LocalDate birthDate) {
        if (birthDate == null) {
            return "Unknown";
        }
        
        LocalDate now = LocalDate.now();
        Period period = Period.between(birthDate, now);
        
        return String.format("%d j %d m %d d", 
            period.getYears(), 
            period.getMonths(), 
            period.getDays());
    }
    
    public int calculateDaysInMonth(int year, int month) {
        if (month < 0) {
            month = 11;
        }
        LocalDate date = LocalDate.of(year, month + 1, 1);
        return date.lengthOfMonth();
    }
}