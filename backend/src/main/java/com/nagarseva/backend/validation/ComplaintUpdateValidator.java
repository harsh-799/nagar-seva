package com.nagarseva.backend.validation;

import com.nagarseva.backend.dto.UpdateComplaintRequest;
import com.nagarseva.backend.exception.NoFieldProvidedException;
import org.springframework.stereotype.Component;

@Component
public class ComplaintUpdateValidator {
    public void validate(UpdateComplaintRequest updateComplaintRequest) {

        if (
                (updateComplaintRequest.getTitle() == null || updateComplaintRequest.getTitle().isBlank())
                        && (updateComplaintRequest.getDesc() == null || updateComplaintRequest.getDesc().isBlank())
                        && (updateComplaintRequest.getIssueType() == null)
                        && (updateComplaintRequest.getWardId() == null)
        ) {
            throw new NoFieldProvidedException("Validation failed: At least one field must be provided, but none were given.");
        }
    }
}
