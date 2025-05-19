package be.pxl.researchproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import be.pxl.researchproject.api.request.*;
import be.pxl.researchproject.api.response.ResetPasswordDTO;
import be.pxl.researchproject.service.ResetPasswordService;

@RestController
@RequestMapping(path = "/password")
public class ResetPasswordController {

    private final ResetPasswordService resetPasswordService;

    @Autowired
    public ResetPasswordController(ResetPasswordService resetPasswordService) {
        this.resetPasswordService = resetPasswordService;
    }

    @PostMapping(path = "/reset-request")
    public ResponseEntity<Void> requestPasswordReset(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        resetPasswordService.createPasswordResetTokenForUser(resetPasswordRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/validate-token")
    public ResponseEntity<ResetPasswordDTO> validateResetToken() {
        return new ResponseEntity<>(resetPasswordService.getResetToken(), HttpStatus.OK);
    }

    @PostMapping(path = "/update-password")
    public ResponseEntity<Void> resetPassword(@RequestBody UpdatePasswordRequest updatePasswordRequest) {
        resetPasswordService.updatePassword(updatePasswordRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
