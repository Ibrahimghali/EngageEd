package com.EngageEd.EngageEd.service;

import java.util.Map;

/**
 * Service interface for email operations
 */
public interface EmailService {
    
    /**
     * Send an email using a template
     */
    void sendEmail(String to, String subject, String templateName, Map<String, Object> variables);
    
    /**
     * Send a general invitation email
     */
    void sendInvitationEmail(String to, String name, String role, String inviteLink);
    
    /**
     * Send enrollment confirmation
     */
    void sendEnrollmentConfirmation(String to, String studentName, String subjectName, String subjectCode);
    
    /**
     * Send password reset email
     */
    void sendPasswordResetEmail(String to, String name, String resetLink);
    
    /**
     * Send an invitation email to a professor
     */
    void sendProfessorInvitation(String email, String professorName, String departmentChiefName, 
                            String departmentName, String invitationLink);
}
