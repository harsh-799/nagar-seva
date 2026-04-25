package com.nagarseva.backend.repository;

import com.nagarseva.backend.entity.User;
import com.nagarseva.backend.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByRole(Role role);
}
