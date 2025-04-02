package com.EngageEd.EngageEd.service;

import com.EngageEd.EngageEd.dto.user.UserRegistrationDTO;
import com.EngageEd.EngageEd.dto.user.UserResponseDTO;
import com.EngageEd.EngageEd.model.UserRole;

public interface UserService {
    // Existing method kept for backward compatibility
    void createUser(String email, String fullname, String password, UserRole role);
    
    // New method using DTOs
    UserResponseDTO createUser(UserRegistrationDTO registrationDTO);
}
