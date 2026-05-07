package com.nagarseva.backend.service;

import com.nagarseva.backend.dto.*;
import com.nagarseva.backend.entity.User;
import com.nagarseva.backend.enums.Role;
import com.nagarseva.backend.exception.InvalidPasswordException;
import com.nagarseva.backend.exception.InvalidUserCreationException;
import com.nagarseva.backend.exception.MissingDepartmentException;
import com.nagarseva.backend.exception.UserAlreadyExistsException;
import com.nagarseva.backend.repository.UserRepository;
import com.nagarseva.backend.security.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public PasswordUpdationResponse updateUserPassword(PasswordUpdationRequest passwordUpdationRequest) {
        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        User updatedUser = userRepository.findByEmail(user.getUsername()).orElseThrow(
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
        if (userRepository.existsByEmail(registerUserRequest.getEmail())) {
            throw new UserAlreadyExistsException("Username Already Taken.");
        }

        if (registerUserRequest.getRole().equals(Role.CITIZEN)) {
            throw new InvalidUserCreationException("Action not allowed: Admins are not permitted to create Citizen accounts");
        }

        User user = new User();
        user.setEmail(registerUserRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerUserRequest.getPassword()));
        user.setFullName(registerUserRequest.getFullName());
        user.setRole(registerUserRequest.getRole());
        user.setDefaultPassword(true);
        user.setActive(true);

        if (registerUserRequest.getRole().equals(Role.OFFICER) && registerUserRequest.getDepartment() == null)
            throw new MissingDepartmentException("Department must be provided while creating a OFFICER.");

        if (registerUserRequest.getRole().equals(Role.OFFICER)) {
            user.setDepartment(registerUserRequest.getDepartment());
        }

        User savedUser = userRepository.save(user);

        RegisterUserResponse response = new RegisterUserResponse();
        response.setSuccess(true);
        response.setEmail(savedUser.getEmail());
        response.setMessage("Account Created Successfully.");
        response.setRole(registerUserRequest.getRole());

        return response;
    }
}
