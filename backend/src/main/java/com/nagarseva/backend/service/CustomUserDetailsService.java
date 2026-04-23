package com.nagarseva.backend.service;

import com.nagarseva.backend.entity.User;
import com.nagarseva.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userRecords = userRepository.findByUsername(username).orElseThrow(
                () -> new RuntimeException("User Not Found")
        );

        return org.springframework.security.core.userdetails.User.builder()
                .username(userRecords.getUsername())
                .password(userRecords.getPassword())
                .roles(String.valueOf(userRecords.getRole()))
                .build();

    }
}
