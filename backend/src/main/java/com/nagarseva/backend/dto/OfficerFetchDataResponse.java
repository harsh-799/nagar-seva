package com.nagarseva.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OfficerFetchDataResponse {
    private Integer id;
    private String name;
    private String department;
    private String profileImage;
    private Long activeComplaints;
    private Long resolvedComplaints;
    private Long pendingComplaints;
}
