package be.pxl.researchproject.api.request;

import be.pxl.researchproject.api.response.ResetPasswordDTO;
import jakarta.validation.constraints.NotEmpty;

public record UpdatePasswordRequest(
                // @NotEmpty(message = "Reset token is required") ResetPasswordDTO
                // resetPasswordDTO,
                @NotEmpty(message = "Reset token is required") String token,
                @NotEmpty(message = "New password is required") String newPassword) {
}
