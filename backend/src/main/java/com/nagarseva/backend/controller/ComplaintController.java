package com.nagarseva.backend.controller;

import com.nagarseva.backend.dto.*;
import com.nagarseva.backend.entity.Ward;
import com.nagarseva.backend.enums.IssueType;
import com.nagarseva.backend.enums.Status;
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

    @PutMapping("/citizen/complaint/{id}")
    public ResponseEntity<UpdateComplaintResponse> updateComplaint(@PathVariable(name = "id") int complaintId, @Valid @ModelAttribute UpdateComplaintRequest updateComplaintRequest, @RequestParam(name = "newimages", required = false) List<MultipartFile> multipartFile) throws IOException {
        complaintUpdateValidator.validate(updateComplaintRequest,multipartFile);
        UpdateComplaintResponse resp = complaintService.updateComplaintCitizen(complaintId,updateComplaintRequest,multipartFile);
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    @GetMapping("/citizen/complaint/{id}")
    public ResponseEntity<ComplaintDetailsResponse> showSpecificComplaintsCitizen(@PathVariable(name = "id") int complaintId) {
        ComplaintDetailsResponse resp = complaintService.showComplaintsById(complaintId);
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    @GetMapping("/admin/complaint/{id}")
    public ResponseEntity<ComplaintDetailsResponse> showSpecificComplaintsAdmin(@PathVariable(name = "id") int complaintId) {
        ComplaintDetailsResponse resp = complaintService.showComplaintsById(complaintId);
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    @DeleteMapping("/citizen/complaint/{id}")
    public ResponseEntity<DeleteComplaintResponse> deleteComplaint(@PathVariable(name = "id") int complaintId) throws IOException {
        DeleteComplaintResponse resp = complaintService.deleteComplaintById(complaintId);
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    @GetMapping("/citizen/complaints")
    public ResponseEntity<ComplaintPageResponse> getUserComplaints(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) IssueType issueType
            ) {
        ComplaintPageResponse resp = complaintService.showUserComplaints(page, size, status, issueType);
        return ResponseEntity.status(HttpStatus.OK).body(resp);

    }

    @GetMapping("/officer/complaints")
    public ResponseEntity<ComplaintPageResponse> getOfficerComplaints(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Integer wardId
    ) {
        ComplaintPageResponse resp = complaintService.showOfficerComplaints(page, size, status, wardId);
        return ResponseEntity.status(HttpStatus.OK).body(resp);

    }

    @GetMapping("/officer/complaint/{id}")
    public ResponseEntity<ComplaintDetailsResponse> showSpecificComplaintsOfficer(@PathVariable(name = "id") int complaintId) {
        ComplaintDetailsResponse resp = complaintService.showComplaintsById(complaintId);
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    @PutMapping("/officer/complaint/{id}/start")
    public ResponseEntity<ComplaintStartResponse> beginComplaintProcessing(
            @PathVariable(name = "id") int complaintId
    ) {
        ComplaintStartResponse resp = complaintService.initiateComplaintWork(complaintId);
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    @PutMapping("/officer/complaint/{id}/finish")
    public ResponseEntity<ComplaintCompletionResponse> finishComplaintProcessing(
            @PathVariable(name = "id") int complaintId,
            @RequestParam(name = "images") List<MultipartFile> images,
            @RequestParam(name = "remark", required = false) String remarks

    ) {
        ComplaintCompletionResponse resp = complaintService.markComplaintCompletedByOfficer(complaintId, images, remarks);
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    @PutMapping("/citizen/complaints/{id}/accept")
    public ResponseEntity<ComplaintResolutionResponse> approveComplaintCompletionByCitizen(
            @PathVariable(name = "id") int complaintId
    ) {
        ComplaintResolutionResponse resp = complaintService.approveWorkDoneByCitizen(complaintId);
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    @PutMapping("/citizen/complaints/{id}/reject")
    public ResponseEntity<ComplaintRejectionResponse> rejectComplaintCompletionByCitizen(
            @PathVariable(name = "id") int complaintId,
            @Valid
            @RequestBody ComplaintRejectionRequest complaintRejectionRequest
    ) {
        ComplaintRejectionResponse resp = complaintService.rejectWorkDoneByCitizen(complaintId, complaintRejectionRequest);
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }


}
