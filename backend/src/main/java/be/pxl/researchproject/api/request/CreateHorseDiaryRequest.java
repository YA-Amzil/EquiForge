package be.pxl.researchproject.api.request;

import jakarta.validation.constraints.NotNull;

public record CreateHorseDiaryRequest(
        @NotNull
        String name
) {
}
