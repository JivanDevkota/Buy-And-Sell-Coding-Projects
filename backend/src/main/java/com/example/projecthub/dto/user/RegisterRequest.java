package com.example.projecthub.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "username cannot be empty")
    @Size(min = 3, max = 40, message = "username must be more than 2 words")
    private String username;

    @NotBlank(message = "email cannot be empty")
    @Size(min = 5,max = 20,message = "email must be with @ keyword")
    private String email;

    @NotBlank(message = "password cannot be empty")
    @Size(min = 6, max = 20, message = "Password must be more than 6 ")
    private String password;
}
