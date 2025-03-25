package com.EngageEd.EngageEd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EngageEd.EngageEd.entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    List <User> findByEmail(String email);
}