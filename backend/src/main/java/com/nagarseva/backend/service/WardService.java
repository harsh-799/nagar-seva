package com.nagarseva.backend.service;

import com.nagarseva.backend.dto.RegisterWardRequest;
import com.nagarseva.backend.dto.RegisterWardResponse;
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

    public RegisterWardResponse addNewWard(RegisterWardRequest registerWardRequest) {

        if (wardRepository.existsById(registerWardRequest.getWardId())) {
            throw new WardAlreadyExistsException("Ward already exists. Please use a unique ward name or ID.");
        }

        User user = fetchAuthenticatedUser();

        if (user.getRole() != Role.ADMIN) {
            throw new InvalidUserRoleException("Access denied: Only ADMIN users are authorized to assign wards.");
        }

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
}
