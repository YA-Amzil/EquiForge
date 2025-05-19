package be.pxl.researchproject.service.Impl;

import java.time.Instant;
import java.util.Date;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import be.pxl.researchproject.api.request.*;
import be.pxl.researchproject.api.response.ResetPasswordDTO;
import be.pxl.researchproject.domain.*;
import be.pxl.researchproject.exception.*;
import be.pxl.researchproject.repository.*;
import be.pxl.researchproject.service.ResetPasswordService;

@Service
@Transactional
public class ResetPasswordServiceImpl implements ResetPasswordService {

    private final UserRepository userRepository;
    private final ResetPasswordRepository resetPasswordRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailServiceImpl emailServiceImpl;

    public ResetPasswordServiceImpl(UserRepository userRepository,
            ResetPasswordRepository resetPasswordRepository, PasswordEncoder passwordEncoder,
            EmailServiceImpl emailServiceImpl) {
        this.userRepository = userRepository;
        this.resetPasswordRepository = resetPasswordRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailServiceImpl = emailServiceImpl;
    }

    @Override
    public void createPasswordResetTokenForUser(ResetPasswordRequest resetPasswordRequest) {
        userRepository.findByEmail(resetPasswordRequest.email()).ifPresentOrElse(user -> {
            String token = RandomStringUtils.randomAlphanumeric(25);
            ResetPassword passwordResetToken = new ResetPassword();
            passwordResetToken.setToken(token);
            passwordResetToken.setUser(user);
            passwordResetToken.setExpiryDate(Date.from(Instant.now().plusSeconds(86400)));
            resetPasswordRepository.save(passwordResetToken);

            String resetUrl = "http://localhost:3000/reset-password?token=" + token;
            String subject = "Wachtwoord Herstel Verzoek";
            String emailContent = String.format(
                    "Beste %s,<br><br>" +
                            "We hebben een verzoek ontvangen om het wachtwoord voor je account te resetten. " +
                            "Als je dit verzoek hebt gedaan, klik dan op de onderstaande link om je wachtwoord opnieuw in te stellen:<br>"
                            +
                            "<a href=\"%s\">Wachtwoord Resetten</a><br><br>" +
                            "Als je geen verzoek hebt gedaan om je wachtwoord te resetten, kun je deze e-mail negeren en zal je wachtwoord ongewijzigd blijven.<br><br>"
                            +
                            "Voor je veiligheid verloopt deze link in 24 uur. Als je hulp nodig hebt, neem dan contact op met onze klantenservice via info@teamaon3.be of bel ons op +32 123 45 67 89.<br><br>"
                            +
                            "Met vriendelijke groet,<br>" +
                            "AON3 Team",
                    user.getUsername(), resetUrl);

            emailServiceImpl.sendEmail(
                    resetPasswordRequest.email(),
                    subject,
                    emailContent,
                    true);
        }, () -> {
            throw new UserNotFoundException(resetPasswordRequest.email());
        });
    }

    @Override
    public void updatePassword(UpdatePasswordRequest updatePasswordRequest) {
        resetPasswordRepository.findByToken(updatePasswordRequest.token())
                .filter(token -> token.getExpiryDate().toInstant().isAfter(Instant.now()))
                .ifPresentOrElse(passwordResetToken -> {
                    User user = passwordResetToken.getUser();
                    user.setPassword(passwordEncoder.encode(updatePasswordRequest.newPassword()));
                    userRepository.save(user);
                    resetPasswordRepository.delete(passwordResetToken);
                }, () -> {
                    throw new TokenNotFoundException("Token is invalid or expired");
                });
    }

    @Override
    public ResetPasswordDTO getResetToken() {
        return resetPasswordRepository.findAll().stream()
                .map(resetPassword -> new ResetPasswordDTO(resetPassword.getToken()))
                .findFirst().orElseThrow(() -> new TokenNotFoundException("No reset token found"));
    }
}
