package com.nagarseva.backend.service;

import com.cloudinary.Cloudinary;
import com.nagarseva.backend.dto.*;
import com.nagarseva.backend.entity.*;
import com.nagarseva.backend.enums.ImageType;
import com.nagarseva.backend.enums.IssueType;
import com.nagarseva.backend.enums.Role;
import com.nagarseva.backend.enums.Status;
import com.nagarseva.backend.exception.*;
import com.nagarseva.backend.repository.ComplaintRepository;
import com.nagarseva.backend.repository.ImageMetaRepository;
import com.nagarseva.backend.repository.UserRepository;
import com.nagarseva.backend.repository.WardRepository;
import com.nagarseva.backend.security.CustomUserDetails;
import com.nagarseva.backend.validation.ImageValidator;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

@Service
@AllArgsConstructor
public class ComplaintService {

    private ComplaintRepository complaintRepository;
    private WardRepository wardRepository;
    private Cloudinary cloudinary;
    private ImageValidator imageValidator;
    private UserRepository userRepository;
    private FlowTransitionValidationService flowValidation;
    private ImageMetaRepository imageMetaRepository;

    private Complaint getComplaintOrThrow(int complaintId) {
        return complaintRepository.findById(complaintId).orElseThrow(
                () -> new ComplaintNotExistException("No Complaint Exists by this Id")
        );
    }

    private void validateOwnership(Complaint complaint, int userId) {
        if (!complaint.getCreatedBy().getId().equals(userId))
            throw new UserMismatchException("Access denied: You cannot edit another user's complaints");
    }

    private void validateComplaintEditable(Complaint complaint) {
        if (!complaint.getStatus().equals(Status.CREATED))
            throw new ComplaintModificationForbiddenException("Complaint has already been verified by admin and cannot be edited further.");
    }

    private void validateCitizen(User user) {
        if (!user.getRole().equals(Role.CITIZEN)) {
            throw new InvalidUserRoleException("Invalid User! Only Citizen are allowed");
        }
    }

    private void validateOfficer(User user) {
        if (!user.getRole().equals(Role.OFFICER)) {
            throw new InvalidUserRoleException("Invalid User! Only Officer are allowed");
        }
    }

    private User fetchAuthenticatedUser() {
        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        return user.getUser();

    }

    private void updateStatusHistory(Status status, Complaint complaint, LocalDateTime currentTime, String remark, String contactDetails) {

        if (complaint.getStatus() != null) {
            flowValidation.validateTransition(complaint.getStatus(), status);
        }

        List<ComplaintStatusHistory> complaintStatusHistories = complaint.getComplaintStatusHistory();
        if (complaintStatusHistories == null)
            complaintStatusHistories = new ArrayList<>();

        ComplaintStatusHistory complaintStatus = new ComplaintStatusHistory();
        complaintStatus.setComplaint(complaint);
        complaintStatus.setStatus(status);

        complaintStatus.setChangedAt(currentTime);

        if (remark != null && !remark.isBlank() &&
                (status == Status.PENDING_VERIFICATION || status == Status.REOPENED)) {
            complaintStatus.setRemark(remark);
        }

        if (contactDetails != null && !contactDetails.isBlank() && status.equals(Status.REOPENED))
            complaintStatus.setContactDetails(contactDetails);

        complaintStatusHistories.add(complaintStatus);

        complaint.setStatus(status);
        complaint.setComplaintStatusHistory(complaintStatusHistories);
        complaint.setLastUpdatedAt(currentTime);

    }

    @Transactional
    public RegisterComplaintResponse addNewComplaint(RegisterComplaintRequest registerComplaintRequest) {
        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Ward ward = user.getUser().getCitizensWard();
        List<MultipartFile> files = registerComplaintRequest.getImages();

        validateCitizen(user.getUser());

        if (registerComplaintRequest.getWardId() != null) {
            ward = wardRepository.findById(registerComplaintRequest.getWardId()).orElseThrow(
                    () -> new InvalidWard("Invalid WardId!")
            );
        }

        // Validation for complaint Images
        if (files != null && !files.isEmpty()) {

            if (files.size() > 3)
                throw new MaxImageUploadExceededException("Maximum 3 Images are allowed");

            imageValidator.validate(files);
        }

        Complaint raiseComplaint = new Complaint();
        raiseComplaint.setTitle(registerComplaintRequest.getTitle());
        raiseComplaint.setIssueType(registerComplaintRequest.getIssueType());
        raiseComplaint.setDescription(registerComplaintRequest.getDesc());
        raiseComplaint.setWard(ward);

        LocalDateTime currentTime = LocalDateTime.now();

        updateStatusHistory(Status.CREATED, raiseComplaint, currentTime,null,null);

        raiseComplaint.setCreatedBy(user.getUser());
        raiseComplaint.setCreatedAt(currentTime);

        List<ImageMeta> images = new ArrayList<>();
        Complaint savedComplaint = null;

        try {
            if (files != null && !files.isEmpty()) {
                for (MultipartFile file : files) {
                    ImageMeta imageMeta = uploadFile(file);
                    imageMeta.setComplaint(raiseComplaint);
                    imageMeta.setImageType(ImageType.BEFORE);
                    images.add(imageMeta);
                }
            }
            raiseComplaint.setImages(images);
            savedComplaint = complaintRepository.save(raiseComplaint);
        } catch (Exception e) {
            for (ImageMeta img: images) {
                try {
                    deleteFile(img);
                } catch (IOException e1) {
                    System.out.println("Cloudinary cleanup failed for complaint");
                    e1.printStackTrace();
                }
            }
            throw new ComplaintCreationFailedException("Complaint creation failed: Complaint could not be persisted due to an unexpected error.");
        }
        RegisterComplaintResponse response = new RegisterComplaintResponse();
        response.setComplaintId(savedComplaint.getId());
        response.setSuccess(true);
        response.setMessage("Complaint raised successfully.");

        return response;

    }

    public ImageMeta uploadFile(MultipartFile file) throws IOException {
        Map<String, Object> uploadResult = cloudinary.uploader().upload(
                file.getBytes(), Map.of("folder", "complaints")
        );

        String uploadedImageUrl = uploadResult.get("secure_url").toString();
        String uploadedImagePublicId = uploadResult.get("public_id").toString();

        ImageMeta imageMeta = new ImageMeta();
        imageMeta.setImageUrl(uploadedImageUrl);
        imageMeta.setImagePublicId(uploadedImagePublicId);
        return imageMeta;
    }

    public UpdateComplaintResponse updateComplaintCitizen(int complaintId, UpdateComplaintRequest updateComplaintRequest, List<MultipartFile> files) throws IOException {
        CustomUserDetails contextUser = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        User user = contextUser.getUser();

        validateCitizen(user);
        Complaint complaint = getComplaintOrThrow(complaintId);

        validateOwnership(complaint, user.getId());
        validateComplaintEditable(complaint);

        if (updateComplaintRequest.getWardId() != null) {
            Ward updatedWard = wardRepository.findById(updateComplaintRequest.getWardId()).orElseThrow(
                    () -> new InvalidWard("Invalid WardId! No ward exists with this Id.")
            );

            complaint.setWard(updatedWard);
        }

        if (updateComplaintRequest.getTitle() != null) {
            complaint.setTitle(updateComplaintRequest.getTitle());
        }

        if (updateComplaintRequest.getDesc() != null) {
            complaint.setDescription(updateComplaintRequest.getDesc());
        }

        if (updateComplaintRequest.getIssueType() != null) {
            complaint.setIssueType(updateComplaintRequest.getIssueType());
        }

        List<String> updatedImages = updateComplaintRequest.getImagePublicIds();
        Set<String> updatedSet = updatedImages == null
                ? Collections.emptySet()
                : new HashSet<>(updatedImages);

        List<ImageMeta> currentImages = complaint.getImages();

        if (currentImages != null) {
            Iterator<ImageMeta> it = currentImages.iterator();

            while (it.hasNext()) {
                ImageMeta img = it.next();
                if (!updatedSet.contains(img.getImagePublicId())) {
                    Map<String, Object> res = cloudinary.uploader().destroy(img.getImagePublicId(), Collections.emptyMap());

                    if (!res.get("result").equals("ok") && !res.get("result").equals("not found"))
                        throw new ImageDeletionFailedException("Can't Delete the Image.");
                    it.remove();
                }
            }
        }

        if (currentImages == null) {
            currentImages = new ArrayList<>();
            complaint.setImages(currentImages);
        }

        if (files != null) {
            if (currentImages.size() + files.size() > 3)
                throw new MaxImageUploadExceededException("Maximum 3 Images are allowed");

            imageValidator.validate(files);
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    ImageMeta imageMeta = uploadFile(file);
                    imageMeta.setComplaint(complaint);
                    imageMeta.setImageType(ImageType.BEFORE);
                    currentImages.add(imageMeta);
                }
            }
        }


        complaint.setLastUpdatedAt(LocalDateTime.now());
        complaintRepository.save(complaint);

        UpdateComplaintResponse response = new UpdateComplaintResponse();
        response.setSuccess(true);
        response.setMessage("Updated Successfully");

        return response;
    }

    public ComplaintDetailsResponse showComplaintsById(int complaintId) {
        Complaint complaint = getComplaintOrThrow(complaintId);

        ComplaintDetailsResponse response = new ComplaintDetailsResponse();
        response.setSuccess(true);
        response.setMessage("Data fetched successfully.");

        response.setComplaintId(complaint.getId());
        response.setTitle(complaint.getTitle());
        response.setDescription(complaint.getDescription());
        response.setIssueType(complaint.getIssueType());
        response.setIssueStatus(complaint.getStatus());

        // Though we ensure that every complaint has valid ward but still it's better to avoid NPE
        Ward complaintWard = complaint.getWard();

        if (complaintWard != null) {
            WardResponse wardResponse = new WardResponse();
            wardResponse.setWardId(complaintWard.getId());
            wardResponse.setWardName(complaintWard.getWardName());
            response.setWard(wardResponse);
        } else {
            response.setWard(null);
        }

        // Same goes here, It's must that every ward has councillor but still handling NPE
        if (complaintWard.getCouncillor() != null) {
            response.setWardCouncillorName(complaintWard.getCouncillor().getFullName());
        } else {
            response.setWardCouncillorName(null);
        }

        // Useful, when issue is not assigned to any of the Officier by admin yet.
        if (complaint.getAssignedTo() != null) {
            response.setAssignedTo(complaint.getAssignedTo().getFullName());
        } else {
            response.setAssignedTo(null);
        }

        List<ImageMeta> complaintImages = complaint.getImages();
        List<ImageResponse> imageResponses = new ArrayList<>();

        if (complaintImages == null)
            complaintImages = new ArrayList<>();

        for (ImageMeta img: complaintImages) {
            ImageResponse imgResp = new ImageResponse();
            imgResp.setUrl(img.getImageUrl());
            imgResp.setPublicId(img.getImagePublicId());
            imageResponses.add(imgResp);
        }

        response.setImages(imageResponses);

        addCompletionDetails(complaint, response, complaintImages);

        addReopenedDetails(complaint, response);

        return response;

    }

    public void deleteFile(ImageMeta img) throws IOException {
        Map<String,Object> res = cloudinary.uploader().destroy(img.getImagePublicId(), Collections.emptyMap());

        if (!res.get("result").equals("ok") && !res.get("result").equals("not found"))
            throw new ImageDeletionFailedException("Can't Delete the Image.");
    }

    @Transactional
    public DeleteComplaintResponse deleteComplaintById(int complaintId) {
        Complaint complaint = getComplaintOrThrow(complaintId);
        User user = fetchAuthenticatedUser();

        validateComplaintEditable(complaint);
        validateCitizen(user);
        validateOwnership(complaint,user.getId());

        List<ImageMeta> complaintImages = complaint.getImages();
        complaintRepository.delete(complaint);

        try {
            if (complaintImages != null) {
                for (ImageMeta img : complaintImages) {
                    deleteFile(img);
                }
            }

        } catch (Exception e) {
            System.out.println("Cloudinary cleanup failed for complaintId: " + complaintId);
            e.printStackTrace();
        }

        DeleteComplaintResponse response = new DeleteComplaintResponse();
        response.setSuccess(true);
        response.setMessage("Complaint deleted successfully.");
        response.setComplaintId(complaintId);
        return response;
    }

    public ComplaintPageResponse showUserComplaints(int page, int size, Status status, IssueType issueType) {
        User user = fetchAuthenticatedUser();

        validateCitizen(user);

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending()
        );

        Page<Complaint> complaintsPage = complaintRepository.findByUserIdWithFilters(user.getId(),issueType,status, pageable);

        List<Complaint> userComplaintList = complaintsPage.getContent();

        List<ComplaintRecordResponse> complaintRecordResponsesList = new ArrayList<>();

        for (Complaint complaint : userComplaintList) {
            ComplaintRecordResponse complaintRecordResponse = new ComplaintRecordResponse();
            complaintRecordResponse.setComplaintId(complaint.getId());
            complaintRecordResponse.setTitle(complaint.getTitle());
            complaintRecordResponse.setIssueType(complaint.getIssueType());
            complaintRecordResponse.setIssueStatus(complaint.getStatus());
            complaintRecordResponse.setCreatedAt(complaint.getCreatedAt());
            complaintRecordResponsesList.add(complaintRecordResponse);
        }

        ComplaintPageResponse response = new ComplaintPageResponse();
        response.setSuccess(true);
        response.setMessage("Complaints fetched successfully.");
        response.setComplaints(complaintRecordResponsesList);
        response.setPage(complaintsPage.getNumber());
        response.setSize(complaintsPage.getSize());
        response.setTotalElements(complaintsPage.getTotalElements());
        response.setIsLast(complaintsPage.isLast());

        return response;
    }

    public ComplaintPageResponse showOfficerComplaints(int page, int size, Status status, Integer wardId) {
        User user = fetchAuthenticatedUser();

        validateOfficer(user);

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending()
        );

        Page<Complaint> complaintsPage = complaintRepository.findByOfficerIdAndFilters(user.getId(), wardId, status, pageable);

        List<Complaint> officerComplaintList = complaintsPage.getContent();

        List<ComplaintRecordResponse> complaintRecordResponsesList = new ArrayList<>();

        for (Complaint complaint : officerComplaintList) {
            ComplaintRecordResponse complaintRecordResponse = new ComplaintRecordResponse();
            complaintRecordResponse.setComplaintId(complaint.getId());
            complaintRecordResponse.setTitle(complaint.getTitle());
            complaintRecordResponse.setIssueType(complaint.getIssueType());
            complaintRecordResponse.setIssueStatus(complaint.getStatus());
            complaintRecordResponse.setCreatedAt(complaint.getCreatedAt());
            complaintRecordResponse.setWardId(complaint.getWard().getId());
            complaintRecordResponsesList.add(complaintRecordResponse);
        }

        ComplaintPageResponse response = new ComplaintPageResponse();
        response.setSuccess(true);
        response.setMessage("Complaints fetched successfully.");
        response.setComplaints(complaintRecordResponsesList);
        response.setPage(complaintsPage.getNumber());
        response.setSize(complaintsPage.getSize());
        response.setTotalElements(complaintsPage.getTotalElements());
        response.setIsLast(complaintsPage.isLast());

        return response;
    }

    public ComplaintStartResponse initiateComplaintWork(int complaintId) {
        User officer = fetchAuthenticatedUser();

        validateOfficer(officer);
        Complaint complaint = getComplaintOrThrow(complaintId);

        if (complaint.getAssignedTo() == null)
            throw new ComplaintNotAssignedToOfficerException("Complaint is not assigned to any officer. Cannot initiate work until assignment is complete.");

        if (!complaint.getAssignedTo().getId().equals(officer.getId()))
            throw new OfficerMismatchException("Complaint is assigned to a different officer. You cannot initiate work on this complaint.");

        LocalDateTime currentTime = LocalDateTime.now();
        updateStatusHistory(Status.IN_PROGRESS, complaint, currentTime, null,null);

        Complaint updatedComplaint = complaintRepository.save(complaint);

        ComplaintStartResponse response = new ComplaintStartResponse();
        response.setSuccess(true);
        response.setComplaintId(updatedComplaint.getId());
        response.setMessage("Officer has begun working on the complaint. Status updated to IN_PROGRESS.");

        return response;
    }

    @Transactional
    public ComplaintCompletionResponse markComplaintCompletedByOfficer(int complaintId, List<MultipartFile> images, String remark) {
        User officer = fetchAuthenticatedUser();

        validateOfficer(officer);
        Complaint complaint = getComplaintOrThrow(complaintId);

        if (complaint.getAssignedTo() == null)
            throw new ComplaintNotAssignedToOfficerException("Complaint is not assigned to any officer. Cannot initiate work until assignment is complete.");

        if (!complaint.getAssignedTo().getId().equals(officer.getId()))
            throw new OfficerMismatchException("Complaint is assigned to a different officer. You cannot initiate work on this complaint.");

        if (complaint.getStatus() != Status.IN_PROGRESS && complaint.getStatus() != Status.REOPENED) {
            throw new ComplaintStatusMismatchException(
                    "Complaint must be IN_PROGRESS or REOPENED to mark as completed"
            );
        }

        if (images == null || images.isEmpty())
            throw new ComplaintCompletionImagesMissingException("Completion work images are required before marking the complaint as completed. Please upload the necessary files.");

        imageValidator.validate(images);

        if (images.size() > 3)
            throw new MaxImageUploadExceededException("Maximum 3 Images are allowed");

        LocalDateTime currentTime = LocalDateTime.now();

        List<ImageMeta> currentComplaintImages = complaint.getImages();

        if (currentComplaintImages == null) {
            currentComplaintImages = new ArrayList<>();
        }

        Complaint updatedComplaint = null;
        List<ImageMeta> completionImages = new ArrayList<>();

        flowValidation.validateTransition(complaint.getStatus(), Status.PENDING_VERIFICATION);
        try {
            for (MultipartFile file: images) {
                ImageMeta imgMeta = uploadFile(file);
                imgMeta.setComplaint(complaint);
                imgMeta.setImageType(ImageType.AFTER);
                imgMeta.setCycleNumber(complaint.getCycleNumber());
                completionImages.add(imgMeta);
            }
            currentComplaintImages.addAll(completionImages);
            updateStatusHistory(Status.PENDING_VERIFICATION, complaint, currentTime, remark, null);
            complaint.setImages(currentComplaintImages);
            updatedComplaint = complaintRepository.save(complaint);
        } catch (Exception e) {

            try {
                for (ImageMeta img : completionImages) {
                    deleteFile(img);
                }
            } catch (IOException e1) {
                System.out.println("Cloudinary Cleanup failed");
            }

            e.printStackTrace();
            throw new ComplaintSubmissionFailedException("Complaint submission failed: Please try again later.");
        }

        ComplaintCompletionResponse response = new ComplaintCompletionResponse();
        response.setSuccess(true);
        response.setMessage("Complaint marked as completed. Forwarded to citizen for verification.");
        response.setStatus(complaint.getStatus());
        response.setComplaintId(updatedComplaint.getId());

        return response;
    }

    @Transactional
    public ComplaintResolutionResponse approveWorkDoneByCitizen(int complaintId) {
        User user = fetchAuthenticatedUser();
        Complaint complaint = getComplaintOrThrow(complaintId);

        validateCitizen(user);
        validateOwnership(complaint, user.getId());

        if (complaint.getStatus() != Status.PENDING_VERIFICATION)
            throw new ComplaintApprovalFailedException("Action denied: Complaint approval is only allowed when status is PENDING_VERIFICATION.");

        LocalDateTime currentTime = LocalDateTime.now();

        updateStatusHistory(Status.CLOSED, complaint, currentTime, null,null);

        complaint.setClosedAt(currentTime);

        Complaint approvedComplaint = complaintRepository.save(complaint);

        ComplaintResolutionResponse response = new ComplaintResolutionResponse();
        response.setComplaintId(approvedComplaint.getId());
        response.setCitizenName(approvedComplaint.getCreatedBy().getFullName());
        response.setSuccess(true);
        response.setMessage("Work completed and approved by citizen.");
        response.setStatus(approvedComplaint.getStatus());
        if (approvedComplaint.getAssignedTo() != null) {
            response.setOfficerName(approvedComplaint.getAssignedTo().getFullName());
        }
        response.setResolvedAt(approvedComplaint.getClosedAt());

        return response;
    }

    @Transactional
    public ComplaintRejectionResponse rejectWorkDoneByCitizen(int complaintId, ComplaintRejectionRequest complaintRejectionRequest) {
        User user = fetchAuthenticatedUser();
        Complaint complaint = getComplaintOrThrow(complaintId);

        validateCitizen(user);
        validateOwnership(complaint, user.getId());

        if (complaint.getStatus() != Status.PENDING_VERIFICATION)
            throw new ComplaintRejectionFailedException("Action denied: Complaint Rejection is only allowed when status is PENDING_VERIFICATION.");

        LocalDateTime currentTime = LocalDateTime.now();

        updateStatusHistory(Status.REOPENED, complaint, currentTime, complaintRejectionRequest.getRemark(), complaintRejectionRequest.getContactDetails());

        Complaint rejectedComplaint = complaintRepository.save(complaint);

        ComplaintRejectionResponse response = new ComplaintRejectionResponse();
        response.setComplaintId(rejectedComplaint.getId());
        response.setSuccess(true);
        response.setCitizenName(rejectedComplaint.getCreatedBy().getFullName());

        if (rejectedComplaint.getAssignedTo() != null) {
            response.setOfficerName(rejectedComplaint.getAssignedTo().getFullName());
        }

        response.setStatus(rejectedComplaint.getStatus());
        response.setRejectedAt(rejectedComplaint.getLastUpdatedAt());
        response.setMessage("Complaint rejected. Your feedback and contact details recorded.");

        return response;
    }

    public ComplaintDetailsResponse addReopenedDetails(Complaint complaint, ComplaintDetailsResponse response) {

        if (complaint.getStatus() != Status.REOPENED) {
            return response;
        }

        List<ComplaintStatusHistory> complaintStatusHistories = complaint.getComplaintStatusHistory();

        if (complaintStatusHistories != null && !complaintStatusHistories.isEmpty()) {
            ComplaintStatusHistory latestReopenedStatusHistory =
                    complaintStatusHistories.stream()
                            .filter(type -> type.getStatus() == Status.REOPENED)
                            .max(Comparator.comparing(ComplaintStatusHistory::getChangedAt))
                            .orElse(null);

            if (latestReopenedStatusHistory != null) {
                String latestReopenedCitizenRemark = latestReopenedStatusHistory.getRemark();
                String latestReopenedCitizenContactDetails = latestReopenedStatusHistory.getContactDetails();

               if (latestReopenedCitizenRemark != null && !latestReopenedCitizenRemark.isBlank()) {
                   response.setCitizenRemark(latestReopenedCitizenRemark);
               }

               if (latestReopenedCitizenContactDetails != null && !latestReopenedCitizenContactDetails.isBlank()) {
                   response.setCitizenContactDetails(latestReopenedCitizenContactDetails);
               }

             }
        }
        return response;
    }

    private void addCompletionDetails(Complaint complaint, ComplaintDetailsResponse response, List<ImageMeta> complaintImages) {
        if (complaint.getStatus() == Status.PENDING_VERIFICATION || complaint.getStatus() == Status.CLOSED ||complaint.getStatus() == Status.AUTO_CLOSED) {
            List<String> afterImageUrls = complaintImages
                    .stream()
                    .filter(img ->
                            img.getImageType() == ImageType.AFTER)
                    .map(ImageMeta::getImageUrl)
                    .toList();

            response.setAfterImages(afterImageUrls);

            ComplaintStatusHistory history = complaint.getComplaintStatusHistory()
                    .stream()
                    .filter(type -> type.getStatus() == Status.PENDING_VERIFICATION)
                    .max(Comparator.comparing(ComplaintStatusHistory::getChangedAt))
                    .orElse(null);

            if (history != null && history.getRemark() != null && !history.getRemark().isBlank())
                response.setRemarks(history.getRemark());
        }
    }
}
