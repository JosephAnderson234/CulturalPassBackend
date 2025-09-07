package com.culturalpass.culturalpass.Mail.listeners;

import com.culturalpass.culturalpass.Mail.domain.MailService;
import com.culturalpass.culturalpass.Mail.domain.QRCodeGenerator;
import com.culturalpass.culturalpass.Mail.domain.TicketGeneratorService;
import com.culturalpass.culturalpass.Mail.events.UserEnrolledEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class UserEnrolledListener {

    @Autowired
    private MailService mailService;

    @Autowired
    private TicketGeneratorService ticketGeneratorService;

    @EventListener
    public void handleUserEnrolledEvent(UserEnrolledEvent event) {
        if (!isValidEvent(event)) {
            return;
        }

        Map<String, Object> variables = prepareEmailVariables(event);
        String subject = "Tu boleto para " + event.getEvent().getTitle();

        try {
            byte[] qrCodeImage = QRCodeGenerator.generateQRCodeImage(event.getToken(), 200, 200);

            try {
                sendEmailWithPDFAndQR(event, variables, subject, qrCodeImage);
                log.info("Level 1: Email with PDF sent successfully to {}", event.getUser().getEmail());

            } catch (Exception pdfException) {
                log.warn("Level 1 failed - PDF generation error for user {}: {}",
                        event.getUser().getEmail(), pdfException.getMessage());

                try {
                    mailService.sendHtmlMailWithQR(
                            event.getUser().getEmail(),
                            subject,
                            "Rolled",
                            variables,
                            qrCodeImage
                    );
                    log.info("Level 2: Email with QR only sent successfully to {}", event.getUser().getEmail());

                } catch (Exception qrEmailException) {
                    log.error("Level 2 failed - QR email error for user {}: {}",
                            event.getUser().getEmail(), qrEmailException.getMessage());
                    sendSimpleFallbackEmail(event, variables, subject);
                }
            }

        } catch (Exception qrGenerationException) {
            log.error("QR generation failed for user {}: {}",
                    event.getUser().getEmail(), qrGenerationException.getMessage());
            sendSimpleFallbackEmail(event, variables, subject);
        }
    }

    private void sendEmailWithPDFAndQR(UserEnrolledEvent event, Map<String, Object> variables,
                                       String subject, byte[] qrCodeImage) throws Exception {
        byte[] ticketPDF = ticketGeneratorService.generateTicketPDF(
                event.getUser().getFirstName(),
                event.getEvent().getTitle(),
                (String) variables.get("eventDate"),
                qrCodeImage
        );

        String pdfFileName = generateAttachmentName(event);

        mailService.sendHtmlMailWithQRAndPDF(
                event.getUser().getEmail(),
                subject,
                "Rolled",
                variables,
                qrCodeImage,
                ticketPDF,
                pdfFileName
        );
    }

    private void sendSimpleFallbackEmail(UserEnrolledEvent event, Map<String, Object> variables, String subject) {
        try {
            mailService.sendSimpleHtmlMail(
                    event.getUser().getEmail(),
                    subject,
                    "SimpleRolled",
                    variables
            );
            log.info("Level 3: Simple fallback email sent successfully to {}", event.getUser().getEmail());

        } catch (Exception fallbackException) {
            log.error("CRITICAL: All email levels failed for user {} and event {}",
                    event.getUser().getEmail(), event.getEvent().getId(), fallbackException);
        }
    }

    private boolean isValidEvent(UserEnrolledEvent event) {
        if (event == null || event.getUser() == null || event.getEvent() == null ||
                event.getToken() == null || event.getToken().trim().isEmpty()) {
            log.error("Invalid UserEnrolledEvent received: {}", event);
            return false;
        }

        if (event.getUser().getEmail() == null || event.getUser().getEmail().trim().isEmpty()) {
            log.error("User {} has no email address", event.getUser().getId());
            return false;
        }

        return true;
    }

    private Map<String, Object> prepareEmailVariables(UserEnrolledEvent event) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("name", event.getUser().getFirstName());
        variables.put("eventTitle", event.getEvent().getTitle());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = event.getEvent().getStartDate().format(formatter);
        variables.put("eventDate", formattedDate);

        return variables;
    }

    private String generateAttachmentName(UserEnrolledEvent event) {
        return String.format("boleto-%s-%d.pdf",
                event.getEvent().getTitle().replaceAll("[^a-zA-Z0-9]", "-"),
                System.currentTimeMillis());
    }
}