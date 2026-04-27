package com.nagarseva.backend.controller;

import com.nagarseva.backend.dto.RegisterComplaintRequest;
import com.nagarseva.backend.dto.RegisterComplaintResponse;
import com.nagarseva.backend.dto.RegisterUserResponse;
import com.nagarseva.backend.service.ComplaintService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@AllArgsConstructor
public class ComplaintController {

    private ComplaintService complaintService;

    @PostMapping("/citizen/complaint")
    public ResponseEntity<RegisterComplaintResponse> createNewComplaint(@Valid @ModelAttribute RegisterComplaintRequest registerComplaintRequest) throws IOException {
        RegisterComplaintResponse resp = complaintService.addNewComplaint(registerComplaintRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

}
