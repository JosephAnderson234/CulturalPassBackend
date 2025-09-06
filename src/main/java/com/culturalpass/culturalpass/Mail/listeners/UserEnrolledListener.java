package com.culturalpass.culturalpass.Mail.listeners;

import com.culturalpass.culturalpass.Mail.domain.MailService;
import com.culturalpass.culturalpass.Mail.domain.QRCodeGenerator;
import com.culturalpass.culturalpass.Mail.events.UserEnrolledEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserEnrolledListener {

    @Autowired
    private MailService mailService;

    @EventListener
    public void handleUserEnrolledEvent(UserEnrolledEvent event) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("name", event.getUser().getFirstName());
        variables.put("eventTitle", event.getEvent().getTitle());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = event.getEvent().getStartDate().format(formatter);
        variables.put("eventDate", formattedDate);

        try {
            String qrData = event.getUser().getEmail() + "-" + event.getEvent().getId();
            byte[] qrCodeImage = QRCodeGenerator.generateQRCodeImage(event.getToken(), 200, 200);

            mailService.sendHtmlMailWithAttachment(
                    event.getUser().getEmail(),
                    "Inscripción exitosa al evento",
                    "Rolled",
                    variables,
                    qrCodeImage
            );
        } catch (Exception e) {
            System.err.println("Error enviando correo de inscripción: " + e.getMessage());
            e.printStackTrace();
        }
    }

}