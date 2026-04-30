package com.nagarseva.backend.repository;

import com.nagarseva.backend.entity.Complaint;
import com.nagarseva.backend.entity.Ward;
import com.nagarseva.backend.enums.IssueType;
import com.nagarseva.backend.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, Integer> {

    List<Complaint> findByIssueType(IssueType issueType);

    List<Complaint> findByWard_Id(Integer wardId);

    @Query("""
            SELECT c FROM Complaint c
            WHERE c.createdBy.id = :userId
            AND (:issueType IS NULL OR c.issueType = :issueType)
            AND (:issueStatus IS NULL OR c.status = :issueStatus)
            """)
    Page<Complaint> findByUserIdWithFilters(
            @Param("userId")Integer userId,
            @Param("issueType") IssueType issueType,
            @Param("issueStatus") Status issueStatus,
            Pageable pageable);

    @Query("""
            SELECT c FROM Complaint c
            WHERE c.assignedTo.id = :userId
            AND (:ward IS NULL OR c.ward.id = :ward)
            AND (:issueStatus IS NULL OR c.status = :issueStatus)
            """)
    Page<Complaint> findByOfficerIdAndFilters(
            @Param("userId")Integer userId,
            @Param("ward") Integer wardId,
            @Param("issueStatus") Status issueStatus,
            Pageable pageable);
}
