package com.example.projecthub.dto.user;

import com.example.projecthub.model.Status;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;

@Data
public class UpdateUserStatusRequest {

    @NonNull
    private Status status;

    @NotBlank
    private String reason;
}
