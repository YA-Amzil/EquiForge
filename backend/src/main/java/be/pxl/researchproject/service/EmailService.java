package be.pxl.researchproject.service;

public interface EmailService {
    void sendEmail(String to, String subject, String text, boolean isHtml);
}
