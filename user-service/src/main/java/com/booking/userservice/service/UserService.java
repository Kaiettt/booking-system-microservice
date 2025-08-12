package com.booking.userservice.service;

import com.booking.userservice.dto.request.UserCreateRequest;
import com.booking.userservice.dto.response.UserResponse;
import com.booking.userservice.entity.User;
import com.booking.userservice.enumerate.Gender;
import com.booking.userservice.enumerate.Role;
import com.booking.userservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository  userRepository;
    private final PasswordEncoder  passwordEncoder;
    public User getUserByUserName(String userName) {
        User user = this.userRepository.findByUsername(userName);
        if(user == null){
             throw new UsernameNotFoundException("Username not found");
        }
        return user;
    }

    public User updateUser(User user){
        return this.userRepository.save(user);
    }

}
