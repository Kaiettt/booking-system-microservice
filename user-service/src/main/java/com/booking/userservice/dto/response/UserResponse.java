package com.booking.userservice.dto.response;

import com.booking.userservice.enumerate.Gender;
import com.booking.userservice.enumerate.Role;
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
public class UserResponse {
    private UUID id;
    private String email;
    private LocalDateTime dateOfBirth;
    private Gender gender;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String username;
    private Role role;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;

}
