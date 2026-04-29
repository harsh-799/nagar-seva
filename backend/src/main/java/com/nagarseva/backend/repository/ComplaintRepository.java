package com.nagarseva.backend.repository;

import com.nagarseva.backend.entity.Complaint;
import com.nagarseva.backend.enums.IssueType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, Integer> {

    List<Complaint> findByIssueType(IssueType issueType);

    List<Complaint> findByWard_Id(Integer wardId);

    Page<Complaint> findByCreatedBy_Id(Integer userId, Pageable pageable);


}
