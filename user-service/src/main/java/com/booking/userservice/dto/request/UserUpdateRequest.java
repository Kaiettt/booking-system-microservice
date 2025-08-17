package com.booking.userservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserUpdateRequest {
    @NotNull
    private UUID id;
    private String email;
    private LocalDateTime dateOfBirth;
    private String gender;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String username;
    private String password;
    private String role;
}
