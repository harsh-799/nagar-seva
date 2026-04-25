package com.nagarseva.backend.service;

import com.nagarseva.backend.dto.RegisterWardRequest;
import com.nagarseva.backend.dto.RegisterWardResponse;
import com.nagarseva.backend.entity.User;
import com.nagarseva.backend.entity.Ward;
import com.nagarseva.backend.enums.Role;
import com.nagarseva.backend.exception.UserNotFoundException;
import com.nagarseva.backend.exception.WardAlreadyExistsException;
import com.nagarseva.backend.exception.WardCouncillorRoleMismatchException;
import com.nagarseva.backend.repository.UserRepository;
import com.nagarseva.backend.repository.WardRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WardService {

    private WardRepository wardRepository;
    private UserRepository userRepository;

    public RegisterWardResponse addNewWard(RegisterWardRequest registerWardRequest) {

        User wardCouncillor = userRepository.findById(registerWardRequest.getCouncillorId()).orElseThrow(
                () -> new UserNotFoundException("No WardCouncillor Exists with this Id")
        );

        if (!wardCouncillor.getRole().equals(Role.COUNCILLOR))
            throw new WardCouncillorRoleMismatchException("Invalid role: User must be a Ward Councillor");

        if (wardRepository.existsByWardName(registerWardRequest.getWardName()))
            throw new WardAlreadyExistsException("Ward already exists with this name");

        Ward ward = new Ward();
        ward.setId(registerWardRequest.getWardId());
        ward.setWardName(registerWardRequest.getWardName());
        ward.setCouncillor(wardCouncillor);

        Ward savedWard = wardRepository.save(ward);

        RegisterWardResponse response = new RegisterWardResponse();
        response.setSuccess(true);
        response.setWardId(savedWard.getId());
        response.setMessage("Ward created successfully.");

        return response;
    }
}
