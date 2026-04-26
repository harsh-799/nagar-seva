package com.nagarseva.backend.controller;

import com.nagarseva.backend.dto.RegisterWardRequest;
import com.nagarseva.backend.dto.RegisterWardResponse;
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
public class WardController {

    private WardService wardService;

    @PostMapping("/admin/ward")
    public ResponseEntity<RegisterWardResponse> createNewWard(@Valid @RequestBody RegisterWardRequest registerWardRequest) {
        RegisterWardResponse resp = wardService.addNewWard(registerWardRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

}
