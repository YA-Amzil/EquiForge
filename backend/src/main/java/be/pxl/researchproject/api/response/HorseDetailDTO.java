package be.pxl.researchproject.api.response;

import java.time.LocalDate;

public record HorseDetailDTO(
    Long id,
    String name,
    LocalDate birthDate,
    String age,
    double height,
    boolean isPregnant,
    String studHorse,
    LocalDate datePregnant,
    LocalDate expectedBirthDate,
    long daysPregnant,
    long remainingDays
) {}