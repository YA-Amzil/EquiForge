package be.pxl.researchproject.api.response;

import be.pxl.researchproject.domain.ResetPassword;

public record ResetPasswordDTO(String token) {

    public ResetPasswordDTO(ResetPassword resetPasswordToken) {
        this(resetPasswordToken.getToken());
    }
}
