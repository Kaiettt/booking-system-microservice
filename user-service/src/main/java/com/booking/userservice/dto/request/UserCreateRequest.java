package com.booking.userservice.dto.request;

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
    private String email;
    private LocalDateTime dateOfBirth;
    private String gender;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String userName;
    private String password;
    private String role;
}