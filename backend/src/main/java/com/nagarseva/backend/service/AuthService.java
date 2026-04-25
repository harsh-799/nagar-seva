package com.nagarseva.backend.service;

import com.nagarseva.backend.dto.LoginUserRequest;
import com.nagarseva.backend.dto.LoginUserResponse;
import com.nagarseva.backend.dto.RegisterCitizenRequest;
import com.nagarseva.backend.dto.RegisterCitizenResponse;
import com.nagarseva.backend.entity.User;
import com.nagarseva.backend.enums.Role;
import com.nagarseva.backend.exception.UsernameAlreadyTaken;
import com.nagarseva.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtService jwtService;

    public RegisterCitizenResponse createNewCitizenAccount(RegisterCitizenRequest registerCitizenRequest) {

        if (userRepository.existsByUsername(registerCitizenRequest.getUsername())) {
            throw new UsernameAlreadyTaken("Username Already Taken.");
        }

        User citizen = new User();
        citizen.setUsername(registerCitizenRequest.getUsername());
        citizen.setPassword(passwordEncoder.encode(registerCitizenRequest.getPassword()));
        citizen.setFullName(registerCitizenRequest.getFullName());
        citizen.setRole(Role.CITIZEN);
        citizen.setActive(true);

        User savedCitizen = userRepository.save(citizen);

        RegisterCitizenResponse response = new RegisterCitizenResponse();
        response.setSuccess(true);
        response.setUsername(savedCitizen.getUsername());
        response.setMessage("Citizen Account Created Successfully.");

        return response;
    }

    public LoginUserResponse authenticateUser(LoginUserRequest loginUserRequest) {

        Authentication auth = new UsernamePasswordAuthenticationToken(
                loginUserRequest.getUsername(),
                loginUserRequest.getPassword()
        );

        Authentication authResult = authenticationManager.authenticate(auth);

        String authenticatedRole = authResult.getAuthorities()
                .stream()
                .findFirst()
                .get()
                .getAuthority();

        LoginUserResponse response = new LoginUserResponse();
        response.setSuccess(true);
        response.setMessage("Logged In Successfully");
        response.setToken(jwtService.generateToken(authResult.getName(), authenticatedRole));

        return response;

    }

}
