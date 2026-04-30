package com.nagarseva.backend.repository;

import com.nagarseva.backend.entity.ComplaintStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplaintStatusHistoryRepository extends JpaRepository<ComplaintStatusHistory, Integer> {
}
