package com.project.ProjectManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendEmailWithToken(String email, String link) throws Exception {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage(); 
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        String subject = "Join project team invitation";
        String text = "Click the link to join the project team";

        helper.setSubject(subject);
        helper.setText(text, true);
        helper.setTo(email);

        try {
            javaMailSender.send(mimeMessage);
        }
        catch(Exception e)
        {
            throw new Exception("Failed to send mail");
        }
    }
}
