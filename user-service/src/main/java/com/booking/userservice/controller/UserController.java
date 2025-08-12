package com.booking.userservice.controller;

import com.booking.userservice.dto.request.UserCreateRequest;
import com.booking.userservice.dto.response.UserResponse;
import com.booking.userservice.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    public final UserService userService;
}
