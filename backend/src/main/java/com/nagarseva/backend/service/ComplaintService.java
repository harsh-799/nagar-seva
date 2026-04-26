package com.nagarseva.backend.service;

import com.nagarseva.backend.dto.RegisterComplaintRequest;
import com.nagarseva.backend.dto.RegisterComplaintResponse;
import com.nagarseva.backend.entity.Complaint;
import com.nagarseva.backend.entity.Ward;
import com.nagarseva.backend.enums.Role;
import com.nagarseva.backend.enums.Status;
import com.nagarseva.backend.exception.*;
import com.nagarseva.backend.repository.ComplaintRepository;
import com.nagarseva.backend.repository.WardRepository;
import com.nagarseva.backend.security.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ComplaintService {

    private ComplaintRepository complaintRepository;
    private WardRepository wardRepository;

    public RegisterComplaintResponse addNewComplaint(RegisterComplaintRequest registerComplaintRequest) throws IOException {
        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Role userRole = user.getUser().getRole();
        Ward ward = user.getUser().getCitizensWard();
        List<MultipartFile> files = registerComplaintRequest.getImages();

        if (!userRole.equals(Role.CITIZEN))
            throw new InvalidUserRoleException("Only Citizens are allowed to raise Issues");

        if (registerComplaintRequest.getWardId() != null) {
            ward = wardRepository.findById(registerComplaintRequest.getWardId()).orElseThrow(
                    () -> new InvalidWard("Invalid WardId!")
            );
        }

        // Validation for complaint Images
        if (files != null || !files.isEmpty()) {

            if (files.size() > 3)
                throw new MaxImageUploadExceededException("Maximum 3 Images are allowed");

            for (MultipartFile file : files) {
                if (file.isEmpty())
                    throw new CorruptedImageException("Images are corruped or empty.");

                String contentType = file.getContentType();
                System.out.println(contentType);
                if (contentType == null || !contentType.startsWith("image/"))
                    throw new UnsupportedFileTypeException("Only images files are allowed");

                long maxSize = 5 * 1024 * 1024;
                if (file.getSize() > maxSize) {
                    throw new ImageSizeExceededException("File size should not exceed 5MB");
                }
            }
        }

        Complaint raiseComplaint = new Complaint();
        raiseComplaint.setTitle(registerComplaintRequest.getTitle());
        raiseComplaint.setIssueType(registerComplaintRequest.getIssueType());
        raiseComplaint.setDescription(registerComplaintRequest.getDesc());
        raiseComplaint.setWard(ward);
        raiseComplaint.setStatus(Status.CREATED);
        raiseComplaint.setCreatedBy(user.getUser());
        raiseComplaint.setCreatedAt(LocalDateTime.now());
        raiseComplaint.setLastUpdatedAt(LocalDateTime.now());

        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            String path = "C:/NagarSeva/uploads/" + fileName;
            file.transferTo(new File(path));
            imageUrls.add(path);
        }

        raiseComplaint.setImageUrls(imageUrls);
        Complaint savedComplaint = complaintRepository.save(raiseComplaint);

        RegisterComplaintResponse response = new RegisterComplaintResponse();
        response.setComplaintId(savedComplaint.getId());
        response.setSuccess(true);
        response.setMessage("Complaint raised successfully.");

        return response;

    }
}
