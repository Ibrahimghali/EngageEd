package com.EngageEd.EngageEd.serviceImpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.EngageEd.EngageEd.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    
    @Value("${spring.mail.username}")
    private String sender;
    
    @Value("${app.base-url:http://localhost:3000}")
    private String baseUrl;

    @Override
    @Async
    public void sendEmail(String to, String subject, String templateName, Map<String, Object> variables) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            
            // Add common variables
            variables.put("baseUrl", baseUrl);
            
            Context context = new Context();
            context.setVariables(variables);
            String html = templateEngine.process(templateName, context);
            
            helper.setTo(to);
            helper.setFrom(sender);
            helper.setSubject(subject);
            helper.setText(html, true);
            
            mailSender.send(message);
            log.info("Email sent successfully to: {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send email to: {}", to, e);
        }
    }

    @Override
    @Async
    public void sendInvitationEmail(String to, String name, String role, String inviteLink) {
        Map<String, Object> variables = Map.of(
                "name", name,
                "role", role,
                "inviteLink", inviteLink
        );
        
        sendEmail(to, "Invitation to join EngageEd", "invitation", variables);
    }

    @Override
    @Async
    public void sendEnrollmentConfirmation(String to, String studentName, String subjectName, String subjectCode) {
        Map<String, Object> variables = Map.of(
                "studentName", studentName,
                "subjectName", subjectName,
                "subjectCode", subjectCode
        );
        
        sendEmail(to, "Enrollment Confirmation - " + subjectName, "enrollment-confirmation", variables);
    }

    @Override
    @Async
    public void sendPasswordResetEmail(String to, String name, String resetLink) {
        Map<String, Object> variables = Map.of(
                "name", name,
                "resetLink", resetLink
        );
        
        sendEmail(to, "Password Reset Request", "password-reset", variables);
    }

    @Override
    public void sendProfessorInvitation(String professorEmail, String professorName, String invitationLink, 
                                        String senderName, String additionalMessage) {
        log.info("Sending professor invitation to: {}", professorEmail);
        
        try {
            // Create the email content
            String subject = "Invitation to join EngageEd as a Professor";
            
            // You can use HTML for better formatting
            String htmlBody = String.format("""
                <html>
                <body>
                    <h2>Welcome to EngageEd!</h2>
                    <p>Dear %s,</p>
                    <p>You have been invited by %s to join EngageEd as a Professor.</p>
                    <p>%s</p>
                    <p>Please click the link below to complete your registration:</p>
                    <p><a href="%s">Complete Registration</a></p>
                    <p>If you have any questions, please contact us.</p>
                    <p>Best regards,<br>The EngageEd Team</p>
                </body>
                </html>
                """, professorName, senderName, additionalMessage, invitationLink);
            
            // Send the email using your configured email sender (JavaMailSender)
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(professorEmail);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // true indicates HTML content
            
            mailSender.send(message);
            log.info("Professor invitation sent successfully to: {}", professorEmail);
        } catch (Exception e) {
            log.error("Failed to send professor invitation", e);
            throw new MailSendException("Failed to send professor invitation: " + e.getMessage());
        }
    }
}