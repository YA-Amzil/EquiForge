package be.pxl.researchproject.api.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record HorseListItemDTO(
        Long id,
        String name,
        boolean pregnant,
        long daysPregnant,
        long expectedPregnancyDays,
        String studHorse,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy") LocalDate expectedPregnancyDate) {
}
