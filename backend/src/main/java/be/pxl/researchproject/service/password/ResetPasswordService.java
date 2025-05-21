package be.pxl.researchproject.service.password;

import be.pxl.researchproject.api.request.ResetPasswordRequest;
import be.pxl.researchproject.api.request.UpdatePasswordRequest;
import be.pxl.researchproject.api.response.ResetPasswordDTO;

public interface ResetPasswordService {
    void createPasswordResetTokenForUser(ResetPasswordRequest resetPasswordRequest);
    ResetPasswordDTO getResetToken();
    void updatePassword(UpdatePasswordRequest updatePasswordRequest);
}