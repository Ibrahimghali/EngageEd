package com.EngageEd.EngageEd.serviceImpl;

import java.util.List;
import java.util.Optional;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.EngageEd.EngageEd.entity.User;
import com.EngageEd.EngageEd.repository.UserRepo;

@Service
public class UserService {
    private final UserRepo userRepository;

    // @Autowired
    public UserService(UserRepo userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }
}

