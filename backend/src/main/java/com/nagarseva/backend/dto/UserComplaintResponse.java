package com.nagarseva.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserComplaintResponse {
    private Boolean success;
    private String message;
    List<ComplaintRecordResponse> complaints;
}
