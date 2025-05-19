package be.pxl.researchproject.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record ResetPasswordRequest(
        @NotEmpty(message = "Email is required") @Email(message = "Email is not valid") String email) {
}