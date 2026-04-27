package com.nagarseva.backend.service;

import com.cloudinary.Cloudinary;
import com.nagarseva.backend.dto.RegisterComplaintRequest;
import com.nagarseva.backend.dto.RegisterComplaintResponse;
import com.nagarseva.backend.entity.Complaint;
import com.nagarseva.backend.entity.ImageMeta;
import com.nagarseva.backend.entity.Ward;
import com.nagarseva.backend.enums.Role;
import com.nagarseva.backend.enums.Status;
import com.nagarseva.backend.exception.*;
import com.nagarseva.backend.repository.ComplaintRepository;
import com.nagarseva.backend.repository.ImageMetaRepository;
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
import java.util.Map;
import java.util.Objects;

@Service
@AllArgsConstructor
public class ComplaintService {

    private ComplaintRepository complaintRepository;
    private WardRepository wardRepository;
    private Cloudinary cloudinary;

    public RegisterComplaintResponse addNewComplaint(RegisterComplaintRequest registerComplaintRequest) {
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
        if (files != null && !files.isEmpty()) {

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

        List<ImageMeta> images = new ArrayList<>();

        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                ImageMeta imageMeta = uploadFile(file);
                imageMeta.setComplaint(raiseComplaint);
                images.add(imageMeta);
            }
        }

        raiseComplaint.setImages(images);
        Complaint savedComplaint = complaintRepository.save(raiseComplaint);

        RegisterComplaintResponse response = new RegisterComplaintResponse();
        response.setComplaintId(savedComplaint.getId());
        response.setSuccess(true);
        response.setMessage("Complaint raised successfully.");

        return response;

    }

    public ImageMeta uploadFile(MultipartFile file) {
        try {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(), Map.of("folder", "complaints")
            );

            String uploadedImageUrl = uploadResult.get("secure_url").toString();
            String uploadedImagePublicId = uploadResult.get("public_id").toString();

            ImageMeta imageMeta = new ImageMeta();
            imageMeta.setImageUrl(uploadedImageUrl);
            imageMeta.setImagePublicId(uploadedImagePublicId);
            return imageMeta;

        } catch (IOException e) {
            throw new FileUploadException("Failed to upload image");
        }
    }
}
