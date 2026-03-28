package com.monnouveauprojet.monappli.Service;

import jakarta.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String content) {
       try {
           MimeMessage message = mailSender.createMimeMessage();
           MimeMessageHelper helper = new MimeMessageHelper(message,true);

          helper.setTo(to);
           helper.setSubject(subject);
           helper.setText(content, true);

           mailSender.send(message);
       }
        catch (Exception e) {
           e.printStackTrace();

        }
}
}




