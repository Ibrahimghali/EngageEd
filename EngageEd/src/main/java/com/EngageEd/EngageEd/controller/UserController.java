package com.EngageEd.EngageEd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.EngageEd.EngageEd.dto.UserCreationRequest;
import com.EngageEd.EngageEd.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserCreationRequest request) {
        try {
            userService.createUser(
                    request.getEmail(),
                    request.getFullName(),
                    request.getPassword(),
                    request.getRole()
            );
            return new ResponseEntity<>("User created successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create user: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}