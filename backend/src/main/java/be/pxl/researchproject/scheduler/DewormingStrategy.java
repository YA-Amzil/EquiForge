package be.pxl.researchproject.scheduler;

import java.time.LocalDate;
import java.util.Map;

public interface DewormingStrategy {
    Map<LocalDate, String> generateDewormingSchedule(LocalDate birthDate);
}

