package com.toystorage.backend.repository.auth;

import com.toystorage.backend.models.auth.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {

    Optional<UserSession> findBySessionToken(String sessionToken);

    List<UserSession> findByUser_Id(Long userId);
}