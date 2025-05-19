package be.pxl.researchproject.api.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record UpdateHorseRequest(
       @NotNull String name,
       @NotNull @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy") LocalDate birthDate,
       @NotNull double height,
       @NotNull String studHorse,
       @NotNull boolean isPregnant,
       @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy") LocalDate datePregnant) {
}
