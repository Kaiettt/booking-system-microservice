package com.booking.userservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserCreateRequest {
    @NotBlank
    private String email;
    private LocalDateTime dateOfBirth;
    private String gender;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String role;
}