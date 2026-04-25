package com.nagarseva.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterWardRequest {
    @NotNull(message = "WardId is empty")
    private Integer wardId;
    @NotBlank(message = "WardName is empty")
    private String wardName;
    @NotNull(message = "WardCouncillor Id is empty")
    private Integer councillorId;
}
