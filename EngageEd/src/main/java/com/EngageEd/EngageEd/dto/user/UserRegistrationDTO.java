package com.EngageEd.EngageEd.dto.user;

import com.EngageEd.EngageEd.model.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRegistrationDTO {
    @NotBlank(message = "Full name is required")
    private String fullName;

    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    private String password;

    @NotNull(message = "Role is required")
    private UserRole role;
}
