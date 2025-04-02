package com.EngageEd.EngageEd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.EngageEd.EngageEd.dto.user.UserRegistrationDTO;
import com.EngageEd.EngageEd.dto.user.UserResponseDTO;
import com.EngageEd.EngageEd.model.UserRole;
import com.EngageEd.EngageEd.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Register a new user
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserRegistrationDTO registrationDTO) {
        UserResponseDTO createdUser = userService.createUser(registrationDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    /**
     * Legacy registration endpoint (for backward compatibility)
     */
    @PostMapping("/register-legacy")
    public ResponseEntity<String> registerUserLegacy(
            @RequestParam("email") String email,
            @RequestParam("fullName") String fullName,
            @RequestParam("password") String password,
            @RequestParam("role") UserRole role) {
        
        userService.createUser(email, fullName, password, role);
        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }
    
    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return new ResponseEntity<>("User service is up and running", HttpStatus.OK);
    }
    
    /**
     * Error handler for validation failures
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleExceptions(Exception ex) {
        if (ex instanceof IllegalArgumentException) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("An error occurred: " + ex.getMessage(), 
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}