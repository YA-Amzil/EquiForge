package be.pxl.researchproject.service.password.impl;

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
import be.pxl.researchproject.service.email.EmailService;
import be.pxl.researchproject.service.password.ResetPasswordService;

@Service
@Transactional
public class ResetPasswordServiceImpl implements ResetPasswordService {
    private final UserRepository userRepository;
    private final ResetPasswordRepository resetPasswordRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private static final int TOKEN_EXPIRY_HOURS = 24;

    public ResetPasswordServiceImpl(
            UserRepository userRepository,
            ResetPasswordRepository resetPasswordRepository, 
            PasswordEncoder passwordEncoder,
            EmailService emailService) {
        this.userRepository = userRepository;
        this.resetPasswordRepository = resetPasswordRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Override
    public void createPasswordResetTokenForUser(ResetPasswordRequest request) {
        userRepository.findByEmail(request.email())
            .ifPresentOrElse(user -> {
                String token = generateResetToken();
                saveResetToken(user, token);
                sendResetEmail(user, token);
            }, () -> {
                throw new UserNotFoundException(request.email());
            });
    }

    @Override
    public void updatePassword(UpdatePasswordRequest request) {
        resetPasswordRepository.findByToken(request.token())
            .filter(token -> token.getExpiryDate().toInstant().isAfter(Instant.now()))
            .ifPresentOrElse(passwordResetToken -> {
                updateUserPassword(passwordResetToken.getUser(), request.newPassword());
                resetPasswordRepository.delete(passwordResetToken);
            }, () -> {
                throw new TokenNotFoundException("Token is invalid or expired");
            });
    }

    @Override
    public ResetPasswordDTO getResetToken() {
        return resetPasswordRepository.findAll().stream()
                .map(ResetPasswordDTO::new)
                .findFirst()
                .orElseThrow(() -> new TokenNotFoundException("No reset token found"));
    }

    private String generateResetToken() {
        return RandomStringUtils.randomAlphanumeric(25);
    }

    private void saveResetToken(User user, String token) {
        ResetPassword resetToken = new ResetPassword();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(Date.from(Instant.now().plusSeconds(TOKEN_EXPIRY_HOURS * 3600)));
        resetPasswordRepository.save(resetToken);
    }

    private void sendResetEmail(User user, String token) {
        String resetUrl = "http://localhost:3000/reset-password?token=" + token;
        String subject = "Wachtwoord Herstel Verzoek";
        String emailContent = createEmailContent(user.getUsername(), resetUrl);
        emailService.sendEmail(user.getEmail(), subject, emailContent, true);
    }

    private void updateUserPassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    private String createEmailContent(String username, String resetUrl) {
        return String.format(
            "Beste %s,<br><br>" +
            "We hebben een verzoek ontvangen om het wachtwoord voor je account te resetten. " +
            "Als je dit verzoek hebt gedaan, klik dan op de onderstaande link om je wachtwoord opnieuw in te stellen:<br>" +
            "<a href=\"%s\">Wachtwoord Resetten</a><br><br>" +
            "Als je geen verzoek hebt gedaan om je wachtwoord te resetten, kun je deze e-mail negeren.<br><br>" +
            "Voor je veiligheid verloopt deze link in 24 uur.<br><br>" +
            "Met vriendelijke groet,<br>" +
            "Het Team",
            username, resetUrl);
    }
}