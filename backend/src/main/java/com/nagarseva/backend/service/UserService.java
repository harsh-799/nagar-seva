package com.nagarseva.backend.service;

import com.nagarseva.backend.dto.*;
import com.nagarseva.backend.entity.User;
import com.nagarseva.backend.enums.Role;
import com.nagarseva.backend.exception.InvalidPasswordException;
import com.nagarseva.backend.exception.InvalidUserCreation;
import com.nagarseva.backend.exception.UsernameAlreadyTaken;
import com.nagarseva.backend.repository.UserRepository;
import com.nagarseva.backend.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.EnumSet;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public PasswordUpdationResponse updateUserPassword(PasswordUpdationRequest passwordUpdationRequest) {
        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        User updatedUser = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new UsernameNotFoundException("User Not Found")
        );

        if (!user.isDefaultPassword()) {
            if (passwordUpdationRequest.getOldPassword() == null || passwordUpdationRequest.getOldPassword().isEmpty())
                throw new InvalidPasswordException("Kindly Enter the old password");

            boolean passwordMatches = passwordEncoder.matches(passwordUpdationRequest.getOldPassword(), updatedUser.getPassword());

            if (!passwordMatches)
                throw new InvalidPasswordException("Entered password does not match the current password. Please try again.");
        }

        updatedUser.setPassword(passwordEncoder.encode(passwordUpdationRequest.getNewPassword()));
        updatedUser.setDefaultPassword(false);

        userRepository.save(updatedUser);

        PasswordUpdationResponse response = new PasswordUpdationResponse();
        response.setSuccess(true);
        response.setMessage("Password Updated Successfully");

        return response;
    }

    public RegisterUserResponse addNewUser(RegisterUserRequest registerUserRequest) {
        if (userRepository.existsByUsername(registerUserRequest.getUsername())) {
            throw new UsernameAlreadyTaken("Username Already Taken.");
        }

        if (registerUserRequest.getRole().equals(Role.CITIZEN)) {
            throw new InvalidUserCreation("Action not allowed: Admins are not permitted to create Citizen accounts");
        }

        User user = new User();
        user.setUsername(registerUserRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerUserRequest.getPassword()));
        user.setFullName(registerUserRequest.getFullName());
        user.setRole(registerUserRequest.getRole());
        user.setDefaultPassword(true);
        user.setActive(true);

        userRepository.save(user);

        RegisterUserResponse response = new RegisterUserResponse();
        response.setSuccess(true);
        response.setUsername(registerUserRequest.getUsername());
        response.setMessage("Account Created Successfully.");
        response.setRole(registerUserRequest.getRole());

        return response;
    }
}
