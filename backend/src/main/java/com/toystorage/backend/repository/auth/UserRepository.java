package com.toystorage.backend.repository.auth;

import com.toystorage.backend.models.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository
        extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    Optional<User> findByCitizenId(String citizenId);

    Boolean existsByEmail(String email);
}