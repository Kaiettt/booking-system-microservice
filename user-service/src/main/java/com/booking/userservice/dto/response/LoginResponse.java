package com.booking.userservice.dto.response;


import com.booking.userservice.enumerate.Role;
import lombok.*;
import org.springframework.http.ResponseCookie;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LoginResponse {
    private String accessToken;
    private UserLogin user;
    @JsonIgnore
    private ResponseCookie springCookie;
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class UserLogin{
        private UUID id;
        private String userName;
        private String fullName;
        private Role role;
    }

}
