package com.EngageEd.EngageEd.serviceImpl;

import com.EngageEd.EngageEd.dto.UserDTOs;
import com.EngageEd.EngageEd.exception.ResourceNotFoundException;
import com.EngageEd.EngageEd.exception.ResourceAlreadyExistsException;
import com.EngageEd.EngageEd.model.User;
import com.EngageEd.EngageEd.model.UserRole;
import com.EngageEd.EngageEd.repository.UserRepository;
import com.EngageEd.EngageEd.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;

    @Override
    @Transactional
    public User createUser(UserDTOs.UserRegistrationRequest registrationRequest) {
        if (existsByEmail(registrationRequest.getEmail())) {
            throw new ResourceAlreadyExistsException("User already exists with email: " + registrationRequest.getEmail());
        }
        
        User user = User.builder()
                .email(registrationRequest.getEmail())
                .fullName(registrationRequest.getFullName())
                .firebaseUid(registrationRequest.getFirebaseUid())
                .role(registrationRequest.getRole())
                .active(true)
                .build();
        
        return userRepository.save(user);
    }

    @Override
    public UserDTOs.UserResponse getUserById(UUID id) {
        User user = getUserEntityById(id);
        return mapToUserResponse(user);
    }
    
    @Override
    public User getUserEntityById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Override
    public UserDTOs.UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return mapToUserResponse(user);
    }

    @Override
    public UserDTOs.UserResponse getUserByFirebaseUid(String firebaseUid) {
        User user = userRepository.findByFirebaseUid(firebaseUid)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with firebase UID: " + firebaseUid));
        return mapToUserResponse(user);
    }
    
    @Override
    public Optional<User> findUserByFirebaseUid(String firebaseUid) {
        return userRepository.findByFirebaseUid(firebaseUid);
    }

    @Override
    public List<UserDTOs.UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTOs.UserResponse> getUsersByRole(UserRole role) {
        return userRepository.findByRole(role).stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDTOs.UserResponse updateUser(UUID id, UserDTOs.UserUpdateRequest updateRequest) {
        User user = getUserEntityById(id);
        
        if (updateRequest.getFullName() != null) {
            user.setFullName(updateRequest.getFullName());
        }
        
        if (updateRequest.getActive() != null) {
            user.setActive(updateRequest.getActive());
        }
        
        User savedUser = userRepository.save(user);
        return mapToUserResponse(savedUser);
    }

    @Override
    @Transactional
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    @Override
    @Transactional
    public User updateFirebaseUid(UUID userId, String firebaseUid) {
        log.info("Updating Firebase UID for user with ID: {}", userId);
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        
        user.setFirebaseUid(firebaseUid);
        return userRepository.save(user);
    }

    // Helper method to map User entity to UserResponse DTO
    private UserDTOs.UserResponse mapToUserResponse(User user) {
        return UserDTOs.UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .active(user.isActive())
                .build();
    }
}