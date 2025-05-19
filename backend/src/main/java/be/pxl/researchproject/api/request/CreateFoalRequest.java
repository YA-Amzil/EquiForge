package be.pxl.researchproject.api.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

public record CreateFoalRequest(
        @NotNull String name,
        @NotNull Long horseId,
        @NotNull @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy") LocalDate birthDate) {
}
