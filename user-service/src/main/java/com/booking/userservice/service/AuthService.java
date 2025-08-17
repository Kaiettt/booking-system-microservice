package com.booking.userservice.service;

import com.booking.userservice.dto.response.LoginResponse;
import com.booking.userservice.entity.User;
import com.booking.userservice.util.SecurityUtil;
import jakarta.persistence.EntityExistsException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthService {
    private UserService userService;
    public LoginResponse handleLogin(String username) {

        User user = this.userService.getUserByUserName(username);

        LoginResponse.UserLogin userResponse = LoginResponse.UserLogin.builder()
                .id(user.getId())
                .userName(username)
                .fullName(user.getFirstName() + " " + user.getLastName())
                .role(user.getRole())
                .build();

        String accessToken = SecurityUtil.createToken(username, user);
        String refreshToken = SecurityUtil.createRefreshToken(username,userResponse);

        user.setRefreshToken(refreshToken);
        this.userService.updateUser(user);

        ResponseCookie springCookie = SecurityUtil.CreateCookieForRefreshToken(refreshToken);

        return LoginResponse.builder()
                .user(userResponse)
                .accessToken(accessToken)
                .springCookie(springCookie)
                .build();
    }

    public LoginResponse getAccessToken(String refresh_token) throws EntityExistsException {
        User user = this.userService.getUserByRefreshToken(refresh_token);
        if (user == null) {
            throw new EntityExistsException("Refresh Token is invalid");
        }

        // build UserLogin response with builder
        LoginResponse.UserLogin userResponse = LoginResponse.UserLogin.builder()
                .id(user.getId())
                .userName(user.getEmail())
                .fullName(user.getFirstName() + " " + user.getLastName())
                .role(user.getRole())
                .build();

        // create new access token
        String accessToken = SecurityUtil.createToken(user.getEmail(), user);

        // create new refresh token
        String refreshToken = SecurityUtil.createRefreshToken(user.getEmail(), userResponse);
        user.setRefreshToken(refreshToken);
        this.userService.updateUser(user);

        // build cookie
        ResponseCookie springCookie = ResponseCookie.from("refresh-token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(SecurityUtil.jwtRefreshTokenExpiration)
                .build();

        // build and return LoginResponse
        return LoginResponse.builder()
                .user(userResponse)
                .accessToken(accessToken)
                .springCookie(springCookie)
                .build();
    }

}
