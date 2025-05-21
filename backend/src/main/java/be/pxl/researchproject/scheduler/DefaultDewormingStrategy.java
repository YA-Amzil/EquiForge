package be.pxl.researchproject.scheduler;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultDewormingStrategy implements DewormingStrategy {

    @Override
    public Map<LocalDate, String> generateDewormingSchedule(LocalDate birthDate) {
        Map<LocalDate, String> schedule = new LinkedHashMap<>();

        DewormingMedication[] medications = {
                DewormingMedication.PANACUR,
                DewormingMedication.PYRANTEL,
                DewormingMedication.ERAQUELL_FUREXCEL,
                DewormingMedication.PANACUR
        };

        LocalDate nextDewormingDate = birthDate.plusDays(1);
        for (DewormingMedication medication : medications) {
            schedule.put(nextDewormingDate, medication.getName());
            nextDewormingDate = nextDewormingDate.plusMonths(1);
        }

        return schedule;
    }
}