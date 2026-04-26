package com.nagarseva.backend.repository;

import com.nagarseva.backend.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface WardRepository extends JpaRepository<Ward, Integer> {

    Optional<Ward> findByWardName(String wardName);

    boolean existsByWardName(String wardName);

    boolean existsByCouncillor_Id(Integer id);
}
