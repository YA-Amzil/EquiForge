package be.pxl.researchproject.api.request;

import jakarta.validation.constraints.NotNull;

public record UpdateHorseDiaryRequest(
        @NotNull
        String name,
        @NotNull
        String text
) {
}
