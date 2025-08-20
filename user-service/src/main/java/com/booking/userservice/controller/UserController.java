package com.booking.userservice.controller;

import com.booking.userservice.common.UserContext;
import com.booking.userservice.dto.request.UserCreateRequest;
import com.booking.userservice.dto.request.UserUpdateRequest;
import com.booking.userservice.dto.response.UserResponse;
import com.booking.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    public final UserService userService;
    @PostMapping
    ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserCreateRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.createUser(request));
    }
    @PutMapping
    ResponseEntity<UserResponse> updateUser(@RequestBody @Valid UserUpdateRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.updateUser(request));
    }
    @GetMapping("{id}")
    ResponseEntity<UserResponse> getUser(@PathVariable UUID id){

        return ResponseEntity.status(HttpStatus.OK).body(this.userService.getUserDetail(id));
    }
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<List<UserResponse>> getUsers(){

        return ResponseEntity.status(HttpStatus.OK).body(this.userService.getAllUsers());
    }
    @DeleteMapping("{id}")
    ResponseEntity<Map<String, String>> deleteUser(@PathVariable UUID id){
        boolean deactivated = userService.deleteUser(id);

        if (!deactivated) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "User not found with id: " + id));
        }

        return ResponseEntity.ok(Map.of("message", "User deactivated successfully"));
    }
}
