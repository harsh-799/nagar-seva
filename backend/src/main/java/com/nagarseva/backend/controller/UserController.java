package com.nagarseva.backend.controller;

import com.nagarseva.backend.dto.PasswordUpdationRequest;
import com.nagarseva.backend.dto.PasswordUpdationResponse;
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
public class UserController {

    private UserService userService;

    @PostMapping("/change-password")
    public ResponseEntity<PasswordUpdationResponse> updatePassword(@Valid @RequestBody PasswordUpdationRequest passwordUpdationRequest) {
        PasswordUpdationResponse resp =  userService.updateUserPassword(passwordUpdationRequest);
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

}
