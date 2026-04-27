package com.nagarseva.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nagarseva.backend.entity.ImageMeta;
import com.nagarseva.backend.entity.Ward;
import com.nagarseva.backend.enums.IssueType;
import com.nagarseva.backend.enums.Status;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class ComplaintDetailsResponse {
    private Boolean success;
    private String message;
    private Integer complaintId;
    private String title;
    private String description;
    private IssueType issueType;
    private Status issueStatus;
    private WardResponse ward;
    private String wardCouncillorName;
    private String assignedTo;
    private List<ImageResponse> images;
}
