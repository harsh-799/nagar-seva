package com.nagarseva.backend.service;

import com.nagarseva.backend.dto.PasswordUpdationRequest;
import com.nagarseva.backend.dto.PasswordUpdationResponse;
import com.nagarseva.backend.entity.User;
import com.nagarseva.backend.exception.InvalidPasswordException;
import com.nagarseva.backend.repository.UserRepository;
import com.nagarseva.backend.security.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<PasswordUpdationResponse> updateUserPassword(PasswordUpdationRequest passwordUpdationRequest) {
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

        PasswordUpdationResponse resp = new PasswordUpdationResponse();
        resp.setSuccess(true);
        resp.setMessage("Password Updated Successfully");

        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

}
