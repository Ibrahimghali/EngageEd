package com.EngageEd.EngageEd.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.EngageEd.EngageEd.dto.UserDTOs;
import com.EngageEd.EngageEd.model.User;
import com.EngageEd.EngageEd.model.UserRole;

public interface UserService {
    User createUser(UserDTOs.UserRegistrationRequest registrationRequest);
    
    UserDTOs.UserResponse getUserById(UUID id);
    
    UserDTOs.UserResponse getUserByFirebaseUid(String firebaseUid);
    
    User getUserEntityById(UUID id);
    
    boolean existsByEmail(String email);
    
    UserDTOs.UserResponse getUserByEmail(String email);
    
    Optional<User> findUserByFirebaseUid(String firebaseUid);
    
    List<UserDTOs.UserResponse> getAllUsers();
    
    List<UserDTOs.UserResponse> getUsersByRole(UserRole role);
    
    UserDTOs.UserResponse updateUser(UUID id, UserDTOs.UserUpdateRequest updateRequest);
    
    void deleteUser(UUID id);
    
    User updateFirebaseUid(UUID id, String firebaseUid);
}
