package com.nagarseva.backend.service;

import com.nagarseva.backend.dto.RegisterWardRequest;
import com.nagarseva.backend.dto.RegisterWardResponse;
import com.nagarseva.backend.dto.WardCouncillorAssignResponse;
import com.nagarseva.backend.entity.User;
import com.nagarseva.backend.entity.Ward;
import com.nagarseva.backend.enums.Role;
import com.nagarseva.backend.exception.*;
import com.nagarseva.backend.repository.UserRepository;
import com.nagarseva.backend.repository.WardRepository;
import com.nagarseva.backend.security.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WardService {

    private WardRepository wardRepository;
    private UserRepository userRepository;

    private User fetchAuthenticatedUser() {
        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        return user.getUser();
    }

    private void validateAdmin(User user) {
        if (!user.getRole().equals(Role.ADMIN)) {
            throw new InvalidUserRoleException("Invalid User! Only Officer are allowed");
        }
    }

    private void validateCouncillor(User user) {
        if (!user.getRole().equals(Role.COUNCILLOR)) {
            throw new InvalidUserRoleException("Invalid User! Only Ward Councillor are allowed");
        }
    }

    public RegisterWardResponse addNewWard(RegisterWardRequest registerWardRequest) {

        if (wardRepository.existsById(registerWardRequest.getWardId())) {
            throw new WardAlreadyExistsException("Ward already exists. Please use a unique ward name or ID.");
        }

        User user = fetchAuthenticatedUser();

        validateAdmin(user);

        Ward ward = new Ward();
        ward.setId(registerWardRequest.getWardId());
        ward.setWardName(registerWardRequest.getWardName());

        Ward savedWard = wardRepository.save(ward);

        RegisterWardResponse response = new RegisterWardResponse();
        response.setSuccess(true);
        response.setWardId(savedWard.getId());
        response.setMessage("Ward created successfully.");

        return response;
    }

    public WardCouncillorAssignResponse setWardCouncillor(int wardId, int councillorId) {
        User admin = fetchAuthenticatedUser();

        Ward ward = wardRepository.findById(wardId).orElseThrow(
                () -> new InvalidWardException("No ward exists with this id")
        );

        if (ward.getCouncillor() != null) {
            throw new WardAlreadyAssignedToDifferentCouncillorException("Ward already has a councillor assigned. Cannot assign a different councillor.");
        }

        User user = userRepository.findById(councillorId).orElseThrow(
                () -> new UserNotFoundException("No User found with this id")
        );

        validateAdmin(admin);
        validateCouncillor(user);

        if (wardRepository.existsByCouncillor_Id(councillorId)) {
            throw new CouncillorAlreadyAssignedException("Councillor already assigned to different ward");
        }

        ward.setCouncillor(user);

        Ward savedWard = wardRepository.save(ward);

        WardCouncillorAssignResponse response = new WardCouncillorAssignResponse();
        response.setSuccess(true);
        response.setMessage("Councillor added successfully");
        response.setWardId(savedWard.getId());
        response.setCouncillorName(savedWard.getCouncillor().getFullName());

        return response;
    }
}
