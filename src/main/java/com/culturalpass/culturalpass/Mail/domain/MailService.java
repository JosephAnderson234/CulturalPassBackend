package com.culturalpass.culturalpass.Mail.domain;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Async
    public void sendHtmlMail(String to, String subject, String templateName, Map<String, Object> variables) throws MessagingException {
        Context context = new Context();
        context.setVariables(variables);
        String htmlContent = templateEngine.process(templateName + ".html", context);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        mailSender.send(message);
    }

    @Async
    public void sendHtmlMailWithAttachment(String to, String subject, String templateName, Map<String, Object> variables, byte[] qrCodeImage) throws MessagingException {
        Context context = new Context();
        context.setVariables(variables);
        String htmlContent = templateEngine.process("Rolled", context);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        helper.addAttachment("qr.png", new ByteArrayResource(qrCodeImage));
        mailSender.send(message);
    }
}