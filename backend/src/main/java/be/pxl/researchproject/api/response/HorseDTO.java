package be.pxl.researchproject.api.response;

import java.time.LocalDate;
import java.util.List;
import be.pxl.researchproject.domain.Foal;

public record HorseDTO(
        String name,
        LocalDate birthDate,
        double height,
        boolean isPregnant,
        String studHorse) {
}
