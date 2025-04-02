package com.EngageEd.EngageEd.dto.mapper;

import com.EngageEd.EngageEd.dto.user.UserResponseDTO;
import com.EngageEd.EngageEd.model.User;

public class UserMapper {
    public static UserResponseDTO toDTO(User user) {
        return new UserResponseDTO(user.getId(), user.getEmail(), user.getFullName(), user.getRole());
    }
}
