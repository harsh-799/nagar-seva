package com.nagarseva.backend.controller;

import com.nagarseva.backend.dto.LoginUserRequest;
import com.nagarseva.backend.dto.LoginUserResponse;
import com.nagarseva.backend.dto.RegisterCitizenRequest;
import com.nagarseva.backend.dto.RegisterCitizenResponse;
import com.nagarseva.backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthController {

    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterCitizenResponse> registerCitizen(@Valid @RequestBody RegisterCitizenRequest registerCitizenRequest) {
        return authService.createNewCitizenAccount(registerCitizenRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginUserResponse> loginUser(@Valid @RequestBody LoginUserRequest loginUserRequest) {
        return authService.authenticateUser(loginUserRequest);
    }

}
