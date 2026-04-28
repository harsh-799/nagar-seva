package com.nagarseva.backend.controller;

import com.nagarseva.backend.dto.*;
import com.nagarseva.backend.service.ComplaintService;
import com.nagarseva.backend.validation.ComplaintUpdateValidator;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
public class ComplaintController {

    private ComplaintService complaintService;
    private ComplaintUpdateValidator complaintUpdateValidator;

    @PostMapping("/citizen/complaint")
    public ResponseEntity<RegisterComplaintResponse> createNewComplaint(@Valid @ModelAttribute RegisterComplaintRequest registerComplaintRequest) throws IOException {
        RegisterComplaintResponse resp = complaintService.addNewComplaint(registerComplaintRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @PutMapping("/complaint/{id}")
    public ResponseEntity<UpdateComplaintResponse> updateComplaint(@PathVariable(name = "id") int complaintId, @Valid @ModelAttribute UpdateComplaintRequest updateComplaintRequest, @RequestParam(name = "newimages", required = false) List<MultipartFile> multipartFile) throws IOException {
        complaintUpdateValidator.validate(updateComplaintRequest,multipartFile);
        UpdateComplaintResponse resp = complaintService.updateComplaintCitizen(complaintId,updateComplaintRequest,multipartFile);
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    @GetMapping("/complaint/{id}")
    public ResponseEntity<ComplaintDetailsResponse> showSpecificComplaints(@PathVariable(name = "id") int complaintId) {
        ComplaintDetailsResponse resp = complaintService.showComplaintsById(complaintId);
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    @DeleteMapping("/complaint/{id}")
    public ResponseEntity<DeleteComplaintResponse> deleteComplaint(@PathVariable(name = "id") int complaintId) throws IOException {
        DeleteComplaintResponse resp = complaintService.deleteComplaintById(complaintId);
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }


}
