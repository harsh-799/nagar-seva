package com.nagarseva.backend.controller;

import com.nagarseva.backend.dto.*;
import com.nagarseva.backend.service.UserService;
import com.nagarseva.backend.service.WardService;
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
    private WardService wardService;

    @PostMapping("/admin/user")
    public ResponseEntity<RegisterUserResponse> createNewUser(@Valid @RequestBody RegisterUserRequest registerUserRequest) {
        RegisterUserResponse resp = userService.addNewUser(registerUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @PostMapping("/admin/ward")
    public ResponseEntity<RegisterWardResponse> createNewWard(@Valid @RequestBody RegisterWardRequest registerWardRequest) {
        RegisterWardResponse resp = wardService.addNewWard(registerWardRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }
}
