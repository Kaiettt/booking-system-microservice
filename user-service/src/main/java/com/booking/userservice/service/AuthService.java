package com.booking.userservice.service;

import com.booking.userservice.dto.response.LoginResponse;
import com.booking.userservice.entity.User;
import com.booking.userservice.util.SecurityUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
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
        String refreshToken = SecurityUtil.createRefreshToken(username, userResponse);

        user.setRefreshToken(refreshToken);
        this.userService.updateUser(user);

        ResponseCookie springCookie = SecurityUtil.CreateCookieForRefreshToken(refreshToken);

        return LoginResponse.builder()
                .user(userResponse)
                .accessToken(accessToken)
                .springCookie(springCookie)
                .build();
    }
}
