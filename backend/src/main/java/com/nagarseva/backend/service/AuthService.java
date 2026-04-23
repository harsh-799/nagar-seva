package com.nagarseva.backend.service;

import com.nagarseva.backend.dto.RegisterCitizenRequest;
import com.nagarseva.backend.dto.RegisterCitizenResponse;
import com.nagarseva.backend.entity.User;
import com.nagarseva.backend.enums.Role;
import com.nagarseva.backend.exception.UsernameAlreadyTaken;
import com.nagarseva.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<RegisterCitizenResponse> createNewCitizenAccount(RegisterCitizenRequest registerCitizenRequest) {

        if (userRepository.existsByUsername(registerCitizenRequest.getUsername())) {
            throw new UsernameAlreadyTaken("Username Already Taken.");
        }

        User citizen = new User();
        citizen.setUsername(registerCitizenRequest.getUsername());
        citizen.setPassword(passwordEncoder.encode(registerCitizenRequest.getPassword()));
        citizen.setFullName(registerCitizenRequest.getFullName());
        citizen.setRole(Role.CITIZEN);
        citizen.setActive(true);

        userRepository.save(citizen);

        RegisterCitizenResponse response = new RegisterCitizenResponse();
        response.setSuccess(true);
        response.setUsername(registerCitizenRequest.getUsername());
        response.setMessage("Citizen Account Created Successfully.");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


}
