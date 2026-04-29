package com.nagarseva.backend.dto;

import com.nagarseva.backend.enums.IssueType;
import com.nagarseva.backend.enums.Status;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class ComplaintRecordResponse {
    private Integer complaintId;
    private String title;
    private IssueType issueType;
    private Status issueStatus;
    private LocalDateTime createdAt;
}
