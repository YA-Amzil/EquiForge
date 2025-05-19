package be.pxl.researchproject.api.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record HorseUpdateDTO(
        String name,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")LocalDate birthDate,
        double height,
        boolean isPregnant,
        String studHorse,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")LocalDate datePregnant
) {
}
