package com.EngageEd.EngageEd.dto.user;

import com.EngageEd.EngageEd.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String fullName;
    private String email;
    private UserRole role;
}
