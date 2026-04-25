package com.nagarseva.backend.controller;

import com.nagarseva.backend.dto.RegisterCitizenRequest;
import com.nagarseva.backend.dto.RegisterCitizenResponse;
import com.nagarseva.backend.dto.RegisterUserRequest;
import com.nagarseva.backend.dto.RegisterUserResponse;
import com.nagarseva.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AdminController {

    private UserService userService;

    @PostMapping("/admin/user")
    public ResponseEntity<RegisterUserResponse> createNewUser(@Valid @RequestBody RegisterUserRequest registerUserRequest) {
        RegisterUserResponse resp = userService.addNewUser(registerUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }
}
