package com.nagarseva.backend.service;

import com.nagarseva.backend.dto.LoginUserRequest;
import com.nagarseva.backend.dto.LoginUserResponse;
import com.nagarseva.backend.dto.RegisterCitizenRequest;
import com.nagarseva.backend.dto.RegisterCitizenResponse;
import com.nagarseva.backend.entity.User;
import com.nagarseva.backend.entity.Ward;
import com.nagarseva.backend.enums.Role;
import com.nagarseva.backend.exception.InvalidWardException;
import com.nagarseva.backend.exception.UserAlreadyExistsException;
import com.nagarseva.backend.repository.UserRepository;
import com.nagarseva.backend.repository.WardRepository;
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
    private WardRepository wardRepository;
    private EmailService emailService;

    public RegisterCitizenResponse createNewCitizenAccount(RegisterCitizenRequest registerCitizenRequest) {

        if (userRepository.existsByEmail(registerCitizenRequest.getEmail())) {
            throw new UserAlreadyExistsException("Username already exists.");
        }

        Ward ward = wardRepository.findById(registerCitizenRequest.getWardId()).orElseThrow(
                () -> new InvalidWardException("Invalid wardId: ward does not exist.")
        );

        User citizen = new User();
        citizen.setEmail(registerCitizenRequest.getEmail());
        citizen.setPassword(passwordEncoder.encode(registerCitizenRequest.getPassword()));
        citizen.setFullName(registerCitizenRequest.getFullName());
        citizen.setRole(Role.CITIZEN);
        citizen.setCitizensWard(ward);
        citizen.setActive(true);

        User savedCitizen = userRepository.save(citizen);

        emailService.sendRegistrationSuccessEmail(savedCitizen.getFullName(), savedCitizen.getEmail());

        RegisterCitizenResponse response = new RegisterCitizenResponse();
        response.setSuccess(true);
        response.setMessage("Citizen Account Created Successfully.");

        return response;
    }

    public LoginUserResponse authenticateUser(LoginUserRequest loginUserRequest) {

        Authentication auth = new UsernamePasswordAuthenticationToken(
                loginUserRequest.getEmail(),
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
