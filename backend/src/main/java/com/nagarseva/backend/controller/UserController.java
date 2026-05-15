package com.nagarseva.backend.controller;

import com.nagarseva.backend.dto.*;
import com.nagarseva.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @PostMapping("/admin/user")
    public ResponseEntity<RegisterUserResponse> createNewUser(@Valid @RequestBody RegisterUserRequest registerUserRequest) {
        RegisterUserResponse resp = userService.addNewUser(registerUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @PutMapping("/change-password")
    public ResponseEntity<PasswordUpdationResponse> updatePassword(@Valid @RequestBody PasswordUpdationRequest passwordUpdationRequest) {
        PasswordUpdationResponse resp =  userService.updateUserPassword(passwordUpdationRequest);
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    @PutMapping("/reset-password")
    public ResponseEntity<PasswordUpdationResponse> resetPassword(@Valid @RequestBody PasswordResetRequest passwordResetRequest) {
        PasswordUpdationResponse resp = userService.resetUserPassword(passwordResetRequest);
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    @GetMapping("/admin/officers")
    public ResponseEntity<OfficerFetchResponse> showAllOfficerAdmin(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(required = false) String department
    ) {
        OfficerFetchResponse resp = userService.getAllOfficer(page,size,department);
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

}
