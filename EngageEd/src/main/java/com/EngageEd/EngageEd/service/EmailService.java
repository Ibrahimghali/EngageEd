package com.EngageEd.EngageEd.service;

import java.util.Map;

public interface EmailService {
    
    void sendEmail(String to, String subject, String templateName, Map<String, Object> variables);
    
    void sendInvitationEmail(String to, String name, String role, String inviteLink);
    
    void sendEnrollmentConfirmation(String to, String studentName, String subjectName, String subjectCode);
    
    void sendPasswordResetEmail(String to, String name, String resetLink);
    
    void sendProfessorInvitation(String email, String professorName, String departmentChiefName, 
                            String departmentName, String invitationLink);
}
