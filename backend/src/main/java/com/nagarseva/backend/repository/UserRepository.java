package com.nagarseva.backend.repository;

import com.nagarseva.backend.entity.User;
import com.nagarseva.backend.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByRole(Role role);

    @Query("""
            SELECT u 
            FROM User u
            WHERE u.role = :role
            AND (:department IS NULL OR u.department = :department)
            """)
    Page<User> findAllOfficers(
            @Param("department") String department,
            @Param("role") Role role,
            Pageable pageable
    );
}
