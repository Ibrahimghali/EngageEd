package com.EngageEd.EngageEd.service;

import java.util.List;

public interface UserService<T> {
    T save(T user);
    T findById(Long id);
    List<T> findAll();
    void deleteById(Long id);
}
