package com.nagarseva.backend.controller;

import com.nagarseva.backend.dto.RegisterWardRequest;
import com.nagarseva.backend.dto.RegisterWardResponse;
import com.nagarseva.backend.dto.WardCouncillorAssignResponse;
import com.nagarseva.backend.dto.WardResponse;
import com.nagarseva.backend.service.WardService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class WardController {

    private WardService wardService;

    @PostMapping("/admin/ward")
    public ResponseEntity<RegisterWardResponse> createNewWard(@Valid @RequestBody RegisterWardRequest registerWardRequest) {
        RegisterWardResponse resp = wardService.addNewWard(registerWardRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @PutMapping("/admin/ward/{id}/assign-wc")
    public ResponseEntity<WardCouncillorAssignResponse> assignWardCouncillor(
            @PathVariable(name = "id") int wardId,
            @RequestParam(name = "councillorId") int councillorId
            ) {
        WardCouncillorAssignResponse resp = wardService.setWardCouncillor(wardId, councillorId);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @GetMapping("/admin/wards")
    public ResponseEntity<List<WardResponse>> getWardForAdmin() {
        List<WardResponse> resp = wardService.getWardDetails();
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    @GetMapping("/citizen/wards")
    public ResponseEntity<List<WardResponse>> getWardForCitizen() {
        List<WardResponse> resp = wardService.getWardDetails();
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    @GetMapping("/officer/wards")
    public ResponseEntity<List<WardResponse>> getWardForOfficer() {
        List<WardResponse> resp = wardService.getWardDetails();
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

}
