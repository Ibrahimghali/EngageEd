package com.EngageEd.EngageEd.service;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.EngageEd.EngageEd.dto.UserDTOs;
import com.EngageEd.EngageEd.model.User;
import com.EngageEd.EngageEd.model.UserRole;

/**
 * Service interface for common user operations
 */
public interface UserService {
    User createUser(UserDTOs.UserRegistrationRequest registrationRequest);
    
    /**
     * Find a user by their ID
     * 
     * @param id The user's UUID
     * @return The user response DTO
     */
    UserDTOs.UserResponse getUserById(UUID id);
    
    /**
     * Find a user by their Firebase UID
     * 
     * @param firebaseUid The Firebase UID
     * @return The user response DTO
     */
    UserDTOs.UserResponse getUserByFirebaseUid(String firebaseUid);
    
    /**
     * Get the actual User entity by ID
     * 
     * @param id The user's UUID
     * @return The User entity
     */
    User getUserEntityById(UUID id);
    
    /**
     * Check if a user with the given email exists
     * 
     * @param email The email to check
     * @return True if a user with this email exists
     */
    boolean existsByEmail(String email);
    
    UserDTOs.UserResponse getUserByEmail(String email);
    
    Optional<User> findUserByFirebaseUid(String firebaseUid);
    
    List<UserDTOs.UserResponse> getAllUsers();
    
    List<UserDTOs.UserResponse> getUsersByRole(UserRole role);
    
    UserDTOs.UserResponse updateUser(UUID id, UserDTOs.UserUpdateRequest updateRequest);
    
    void deleteUser(UUID id);
    
    /**
     * Update a user's Firebase UID (used during account activation)
     * 
     * @param id The user's UUID
     * @param firebaseUid The Firebase UID to set
     * @return The updated user
     */
    User updateFirebaseUid(UUID id, String firebaseUid);
}