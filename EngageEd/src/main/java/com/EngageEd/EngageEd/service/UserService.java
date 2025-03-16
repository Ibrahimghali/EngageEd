package com.EngageEd.EngageEd.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.EngageEd.EngageEd.repository.UserRepo;

@Service
public class UserService {
    private final UserRepo userRepository;

    @Autowired
    public UserService(UserRepo userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserRepo> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<UserRepo> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public UserService getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public UserService createUser(UserService user) {
        return userRepository.save(user);
    }
}

