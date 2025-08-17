package com.booking.userservice.service;

import com.booking.userservice.dto.request.UserCreateRequest;
import com.booking.userservice.dto.request.UserUpdateRequest;
import com.booking.userservice.dto.response.UserResponse;
import com.booking.userservice.entity.User;
import com.booking.userservice.mapper.UserMapper;
import com.booking.userservice.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Service
public class UserService {
    private final UserMapper  userMapper;
    private final UserRepository  userRepository;
    private final PasswordEncoder  passwordEncoder;
    public User getUserByUserName(String userName) {
        User user = this.userRepository.findByUsername(userName);
        if(user == null){
             throw new UsernameNotFoundException("Username not found");
        }
        return user;
    }
    public void updateUser(User user){
        this.userRepository.save(user);
    }
    public UserResponse createUser(UserCreateRequest request){
        User user = this.userMapper.toEntity(request);
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        return this.userMapper.toResponse(this.userRepository.save(user));
    }

    public User getUserByRefreshToken(String refreshToken) {
        return this.userRepository.findByRefreshToken(refreshToken);
    }

    public UserResponse updateUser(UserUpdateRequest request) {
        User user = this.userRepository.findById(request.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        this.userMapper.toEntityFromUpdateDto(request, user);
        return this.userMapper.toResponse(this.userRepository.save(user));
    }

    public UserResponse getUserDetail(UUID id) {

        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return this.userMapper.toResponse(user);
    }

    public List<UserResponse> getAllUsers() {
        List<User> users = this.userRepository.findAll();
        return this.userMapper.toResponseList(users);
    }

    public boolean deleteUser(UUID id) {
        try {
            int isSuccess = this.userRepository.softDeleteById(id);
            if (isSuccess == 0) {
                log.warn("No user found with id: {}", id);
                return false;
            }
            return true;
        } catch (Exception e) {
            log.error("Error deleting user with id {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

}
