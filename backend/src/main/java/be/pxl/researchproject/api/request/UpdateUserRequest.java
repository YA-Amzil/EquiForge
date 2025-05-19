package be.pxl.researchproject.api.request;

import jakarta.validation.constraints.*;

public record UpdateUserRequest(
        @NotEmpty(message = "Username is required") String username,
        @Email(message = "Email is not valid") String email,
        @NotEmpty(message = "Password is required") String password,
        @NotEmpty(message = "Role is required") String role,
        @NotNull(message = "Enabled is required") boolean enabled) {
}
