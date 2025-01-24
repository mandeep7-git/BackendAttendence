package com.attendance.BackendAttendanceRT.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
   @Autowired
   private JavaMailSender emailSender;

   public EmailService() {
   }

   public void sendVerificationEmail(String toEmail, String subject, String body) {
    System.out.println("Sending email to: " + toEmail);

    if (toEmail == null || toEmail.isEmpty()) {

        System.out.println("Sending email to: " + toEmail);

        throw new IllegalArgumentException("To address must not be null or empty");
    }
    
    // Send the email using MimeMessageHelper
    MimeMessage message = emailSender.createMimeMessage(); // Corrected here
    MimeMessageHelper helper = new MimeMessageHelper(message);
    
    try {
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(body, true);  // If the body is HTML, set this to true
        emailSender.send(message); // Corrected here
    } catch (MessagingException e) {
        // Handle exception
        e.printStackTrace();
    }
}

}
