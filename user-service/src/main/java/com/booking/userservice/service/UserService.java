package com.booking.userservice.service;

import com.booking.userservice.dto.request.UserCreateRequest;
import com.booking.userservice.dto.response.UserResponse;
import com.booking.userservice.entity.User;
import com.booking.userservice.mapper.UserMapper;
import com.booking.userservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    @Async
    public void updateUser(User user){
        this.userRepository.save(user);
    }
    public UserResponse createUser(UserCreateRequest request){
        User user = this.userMapper.toEntity(request);
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        return this.userMapper.toResponse(this.userRepository.save(user));
    }

}
