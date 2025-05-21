package be.pxl.researchproject.service.email;

public interface EmailService {
    void sendEmail(String to, String subject, String text, boolean isHtml);
}