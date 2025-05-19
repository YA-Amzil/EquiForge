package be.pxl.researchproject.api.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;

public record UpdateFoalRequest(
        @NotNull String name,
        @NotNull @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy") LocalDate birthDate) {
}
