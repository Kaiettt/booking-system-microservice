package com.booking.userservice.config;

import com.booking.userservice.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import org.springframework.security.core.userdetails.User;

import java.util.Collections;

@AllArgsConstructor
@Component("userDetailsService")
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.booking.userservice.entity.User user = this.userService.getUserByUserName(username);
        User user_detail = new User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())));
        return user_detail;
    }
}
